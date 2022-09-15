package com.june.myapplication

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

object Permission {
    private const val REQUEST_ACCESS_LOCATION_PERMISSIONS = 100
    private const val REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS = 101

    fun requestLocationPermissions(context: Context) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_ACCESS_LOCATION_PERMISSIONS
        )
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestBackgroundLocationPermissions(context: Context) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ),
            REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS
        )
    }

    /**
     * showBackgroundLocationPermissionRationaleDialog
     *
     * 앱을 사용하려면 권한이 필요함을 사용자에게 알려주는 안내
     *
     * @param context
     * @param operation setNegativeButton{} 에서 실행 시킬 함수
     */
    @RequiresApi(Build.VERSION_CODES.R)
    fun showBackgroundLocationPermissionRationaleDialog(context: Context, operation: Unit) {
        AlertDialog.Builder(context)
            .setMessage("홈 위젯을 사용하려면 위치 접근 권한이" +
                    " ${context.packageManager.backgroundPermissionOptionLabel} 상태여야 합니다.")
            .setPositiveButton("설정하기") { dialog, _ ->
                requestBackgroundLocationPermissions(context)
                dialog.dismiss()
            }
            .setNegativeButton("그냥두기") { dialog, _ ->
                operation
                dialog.dismiss()
            }
            .show()
    }
}