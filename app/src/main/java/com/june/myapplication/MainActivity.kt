package com.june.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.june.myapplication.data.Repository
import com.june.myapplication.databinding.ActivityMainBinding
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
        requestLocationPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancellationTokenSource?.cancel()
        scope.cancel()
    }

//[START Permission]
    //TODO 앱 최초 실행 시 권한을 '허용 안함' 두 번 실행하면 다음 부터는 권한 메시지를 띄우지 않고 앱이 종료되는 문제가 있음
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // ACCESS_COARSE_LOCATION : 도시 Block 단위의 정밀도의 위치 정보를 얻을 수 있음
        // ACCESS_FINE_LOCATION : ACCESS_COARSE_LOCATION 보다 더 정밀한 위치 정보를 얻을 수 있음
        val isLocationPermissionGranted: Boolean =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        /*
        * shouldShowBackgroundPermissionRationale(permission: String)
        * 사용자가 권한을 처음 본 경우, 다시 묻지 않음을 선택한 경우, 권한을 허용한 경우 false 반환
        * 사용자가 이전에 권한 요청을 거부한 경우 true 를 반환
        * 앱을 사용하려면 권한이 필요함을 사용자에게 알려주는 안내를 추가함
        */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //위치 정보 권한이 없는 경우
            if (!isLocationPermissionGranted) {
                finish()
            }
            //위지 정보 권한이 있는 경우
            else {
                val isBackgroundLocationPermissionGranted: Boolean =
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
                val shouldShowBackgroundPermissionRationale: Boolean =
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

                //사용자가 이전에 권한을 거부한 경우
                if (!isBackgroundLocationPermissionGranted && shouldShowBackgroundPermissionRationale) {
                    showBackgroundLocationPermissionRationaleDialog()
                }
                //권한이 있는 경우
                else {
                    fetchAirQualityData()
                }
            }
        //AOS R 미만 디바이스 권한 요청
        } else {
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

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_ACCESS_LOCATION_PERMISSIONS
        )
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestBackgroundLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ),
            REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS
        )
    }

    // 앱을 사용하려면 권한이 필요함을 사용자에게 알려주는 안내
    @RequiresApi(Build.VERSION_CODES.R)
    private fun showBackgroundLocationPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setMessage("홈 위젯을 사용하려면 위치 접근 권한이 ${packageManager.backgroundPermissionOptionLabel} 상태여야 합니다.")
            .setPositiveButton("설정하기") { dialog, _ ->
                requestBackgroundLocationPermissions()
                dialog.dismiss()
            }
            .setNegativeButton("그냥두기") { dialog, _ ->
                fetchAirQualityData()
                dialog.dismiss()
            }
            .show()
    }
//[END Permission]



//TODO 스터디 https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient
//[START Location]
    /** fetchAirQualityData
    * //TODO 메서드 설명
    *
    * fusedLocationProviderClient
    *기기 위치 정보를 가져오는 여러 메서드를 제공

    *a) getLastLocation() :
    *위치 추정치를 더 빠르게 가져오고 앱에서 비롯될 수 있는 배터리 사용량을 최소화합니다.
    *그러나 최근에 다른 클라이언트가 적극적으로 위치를 사용하지 않은 경우 위치 정보가 최신이 아닐 수 있습니다.

    *b) getCurrentLocation() :
    *최신 위치를 가져오는 데 권장되는 방법
    *더 최신 상태이고 정확한 위치를 더 일관되게 가져옵니다.
    *그러나 이 메서드를 사용하면 기기에서 활성 위치 계산이 발생할 수 있습니다.
    *cf.requestLocationUpdates()를 사용하여 직접 위치 업데이트를 시작하고 관리하는 것과 같은 메서드보다 안전합니다.
    *cf.requestLocationUpdates()를 호출하는 경우 위치를 사용할 수 없거나 최신 위치를 가져온 후 요청이 정확하게 중지되지 않으면 앱이 전력을 많이 소모할 수도 있습니다.
    */
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
                    val monitoringStation = Repository.getNearbyMonitoringStation(location.latitude, location.longitude)
                    //Log.d(TAG, "monitoringStation: ${monitoringStation}")
                }
            }
            //사용자의 위저 데이터를 가져오지 못한 경우
            .addOnFailureListener { e ->
                Toast.makeText(this, "Exception : $e", Toast.LENGTH_SHORT).show()
            }
    }
//[END Location]





//Constant
    companion object {
        const val REQUEST_ACCESS_LOCATION_PERMISSIONS = 100
        const val REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS = 101
        const val TAG = "testLog"
    }
}