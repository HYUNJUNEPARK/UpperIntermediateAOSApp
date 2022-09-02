package com.june.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.june.myapplication.databinding.ActivityMainBinding
import com.june.myapplication.retrofit.Repository
import fastcampus.aop.part4.chapter06.data.models.airquality.Grade
import fastcampus.aop.part4.chapter06.data.models.airquality.MeasuredValue
import fastcampus.aop.part4.chapter06.data.models.monitoringstation.MonitoringStation
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var cancellationTokenSource: CancellationTokenSource? = null
    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        Permission.requestLocationPermissions(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        cancellationTokenSource?.cancel()
        scope.cancel()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        /* ACCESS_COARSE_LOCATION : 도시 Block 단위의 정밀도의 위치 정보를 얻을 수 있음
           ACCESS_FINE_LOCATION   : ACCESS_COARSE_LOCATION 보다 더 정밀한 위치 정보를 얻을 수 있음 */
        val isLocationPermissionGranted: Boolean =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //위치 정보 권한이 없는 경우
            if (!isLocationPermissionGranted) {
                finish()
            }
            //위지 정보 권한이 있는 경우
            else {
                val isBackgroundLocationPermissionGranted: Boolean =
                    ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

                /* shouldShowBackgroundPermissionRationale(permission: String)
                   사용자가 권한을 처음 본 경우, 다시 묻지 않음을 선택한 경우, 권한을 허용한 경우 false 반환
                   사용자가 이전에 권한 요청을 거부한 경우 true 를 반환
                   앱을 사용하려면 권한이 필요함을 사용자에게 알려주는 안내를 추가함 */
                val shouldShowBackgroundPermissionRationale: Boolean =
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

                //사용자가 이전에 권한을 거부한 경우
                if (!isBackgroundLocationPermissionGranted && shouldShowBackgroundPermissionRationale) {
                    Permission.showBackgroundLocationPermissionRationaleDialog(this, fetchAirQualityData())
                }
                //권한이 있는 경우
                else {
                    fetchAirQualityData()
                }
            }
        //AOS R 미만 디바이스 권한 요청
        }
        else {
            //위치 정보 권한이 없는 경우
            if (!isLocationPermissionGranted) {
                finish()
            }
            //위지 정보 권한이 있는 경우
            else {
                fetchAirQualityData()
            }
        }
    }


    /**
     * FusedLocationProviderClient
     * https://developers.google.com/android/reference/com/google/android/gms/location
     *
     * Build
     * implementation 'com.google.android.gms:play-services-location:20.0.0
     *
     * 설명
     * -LocationManager 보다 사용이 권장됨
     * -Google Play services Location 에서 위치정보를 가져오기 때문에 완성도를 높히기 위해서는
     * 앱에 Google Play services 가 있는 지 확인하는 기능이 필요함 (기기 환경에 따라서 해당 앱이 안깔려 있는 경우가 있음)
     *
     * getLastLocation()
     * -위치 추정치를 더 빠르게 가져오고 앱에서 비롯될 수 있는 배터리 사용량을 최소화
     * -그러나 최근에 다른 클라이언트가 적극적으로 위치를 사용하지 않은 경우 위치 정보가 최신이 아닐 수 있음
     *
     * getCurrentLocation()
     * -최신 위치를 가져오는 데 권장되는 방법으로 더 최신 상태이고 정확한 위치를 더 일관되게 가져옴
     * -그러나 이 메서드를 사용하면 기기에서 활성 위치 계산이 발생할 수 있음
     */

    //디바이스 위치정보로 가까운 측정소를 찾음
    @SuppressLint("MissingPermission")
    private fun fetchAirQualityData() {
        cancellationTokenSource = CancellationTokenSource()

        fusedLocationProviderClient
            .getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource!!.token
            )
            .addOnSuccessListener { location ->
                scope.launch {

                    Log.d(TAG, "fetchAirQualityData: ${location.latitude}")
                    val monitoringStation =
                        Repository.getNearbyMonitoringStation(location.latitude, location.longitude)
                    val measuredValue =
                        Repository.getLatestAirQualityData((monitoringStation!!.stationName!!))
                    //displayAirQualityData(monitoringStation, measuredValue!!)
                }
            }
            //사용자의 위저 데이터를 가져오지 못한 경우
            .addOnFailureListener { e ->
                Toast.makeText(this, "Exception : $e", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    fun displayAirQualityData(
        monitoringStation: MonitoringStation,
        measuredValue: MeasuredValue
    ) {
        binding.contentsLayout.animate()
            .alpha(1F)
            .start()

        binding.measuringStationNameTextView.text = monitoringStation.stationName
        binding.measuringStationAddressTextView.text = "측정소 위치: ${monitoringStation.addr}"

        //TODO 33:16'
        //어떠한 이슈 grade 파싱이 안된다면 null 이기 떄문에 이를 unknown 으로 변환하는 작업
        (measuredValue.khaiGrade ?: Grade.UNKNOWN).let { grade ->
            binding.root.setBackgroundResource(grade.colorResId)
            binding.totalGradeLabelTextView.text = grade.label
            binding.totalGradleEmojiTextView.text = grade.emoji
        }

        with(measuredValue) {
            binding.fineDustInformationTextView.text =
                "미세먼지: $pm10Value ㎍/㎥ ${(pm10Grade ?: Grade.UNKNOWN).emoji}"
            binding.ultraFineDustInformationTextView.text =
                "초미세먼지: $pm25Value ㎍/㎥ ${(pm25Grade ?: Grade.UNKNOWN).emoji}"

//            with(binding.so2Item) {
//                labelTextView.text = "아황산가스"
//                gradeTextView.text = (so2Grade ?: Grade.UNKNOWN).toString()
//                valueTextView.text = "$so2Value ppm"
//            }
//
//            with(binding.coItem) {
//                labelTextView.text = "일산화탄소"
//                gradeTextView.text = (coGrade ?: Grade.UNKNOWN).toString()
//                valueTextView.text = "$coValue ppm"
//            }
//
//            with(binding.o3Item) {
//                labelTextView.text = "오존"
//                gradeTextView.text = (o3Grade ?: Grade.UNKNOWN).toString()
//                valueTextView.text = "$o3Value ppm"
//            }
//
//            with(binding.no2Item) {
//                labelTextView.text = "이산화질소"
//                gradeTextView.text = (no2Grade ?: Grade.UNKNOWN).toString()
//                valueTextView.text = "$no2Value ppm"
//            }
        }
    }
//[END Location]

//Constant
    companion object {
        const val TAG = "testLog"
    }
}