package com.june.myapplication.appwidget

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.june.myapplication.R
import com.june.myapplication.models.airquality.Grade
import com.june.myapplication.retrofit.Repository
import kotlinx.coroutines.launch

/*
AppWidgetProvider is an extension of BroadcastReceiver
BroadcastReceiver : 이벤트(시스템의 특정 상황 ex.부팅) 모델로 실행되는 컴포넌트


*/
class SimpleAirQualityWidgetProvider : AppWidgetProvider() {

    /**
     * onUpdate() 에서 Service 를 실행시켜야 작업이 중지되지 않음
     *
     *
     */
    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        ContextCompat.startForegroundService(
            context!!,
            Intent(context, UpdateWidgetService::class.java)
        )
    }

    /**
     * 위젯을 업데이트 하는 서비스
     *
     * TODO LifeCycleService : 코루틴의 일종 ?
     */
    class UpdateWidgetService : LifecycleService() {
        override fun onCreate() {
            super.onCreate()

            createChannelIfNeeded()

            //포어그라운드 서비스 실행
            startForeground(
                NOTIFICATION_ID,
                createNotification()
            )
        }

        override fun onDestroy() {
            super.onDestroy()
            stopForeground(true) //status bar 에 있는 notification 삭제
        }

        // 작업이 끝난 뒤에 stopSelf()/stopService() 를 호출해야 statusBar 에서 사라짐
        /*
        startForeground() 실행 시 호출됨
        START_STICKY 가 기본값
         */
        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            //[START LocationServices.~ 코드의 Add permission check 클릭하면 자동 생성]
            //1. 권한이 없는 경우
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val updateViews = RemoteViews(packageName, R.layout.widget_simple).apply {
                    setTextViewText(R.id.resultTextView, "권한 없음")
                    setViewVisibility(R.id.labelTextView, View.GONE)
                    setViewVisibility(R.id.gradeLabelTextView, View.GONE)

                }
                updateWidget(updateViews)
                stopSelf()
                return super.onStartCommand(intent, flags, startId)
            }
            //[END]

            //2. 권한이 있는 경우
            //데이터를 가져옴(실시간 위치 데이터가 아닌 마지막 위치 데이터)
            LocationServices.getFusedLocationProviderClient(this).lastLocation
                .addOnSuccessListener { location ->
                    lifecycleScope.launch {
                        try {
                            val nearbyMonitoringStation = Repository.getNearbyMonitoringStation(location.latitude, location.longitude)
                            val measuredValue = Repository.getLatestAirQualityData(nearbyMonitoringStation!!.stationName!!)
                            val updateViews = RemoteViews(packageName, R.layout.widget_simple).apply {
                                    setViewVisibility(R.id.labelTextView, View.VISIBLE)
                                    setViewVisibility(R.id.gradeLabelTextView, View.VISIBLE)

                                    val currentGrade = (measuredValue?.khaiGrade ?: Grade.UNKNOWN)
                                    setTextViewText(R.id.resultTextView, currentGrade.emoji)
                                    setTextViewText(R.id.gradeLabelTextView, currentGrade.label)
                                }
                            updateWidget(updateViews)
                        }
                        catch (e: Exception) {
                            e.printStackTrace()
                        }
                        finally {
                            stopSelf()
                        }
                    }
                }
            return super.onStartCommand(intent, flags, startId)
        }


        //포어그라운드 서비스에서 필요한 채널을 생성
        private fun createChannelIfNeeded() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                (getSystemService(NOTIFICATION_SERVICE) as? NotificationManager)
                    ?.createNotificationChannel(
                        NotificationChannel(
                            WIDGET_REFRESH_CHANNEL_ID,
                            "위젯 갱신 채널",
                            NotificationManager.IMPORTANCE_LOW
                        )
                    )
            }
        }

        /**
         *
         */
        private fun createNotification() =
            NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_baseline_refresh_24)
                .setChannelId(WIDGET_REFRESH_CHANNEL_ID)
                .build()

        //AppWidgetManager 에 WidgetProvider, RemoteViews 를 업데이트
        private fun updateWidget(updateViews: RemoteViews) {
            val widgetProvider = ComponentName(this, SimpleAirQualityWidgetProvider::class.java)
            AppWidgetManager.getInstance(this).updateAppWidget(widgetProvider, updateViews)
        }

        companion object {
            private const val NOTIFICATION_ID = 12345
            private const val WIDGET_REFRESH_CHANNEL_ID = "WIDGET_REFRESH"
        }
    }
}
