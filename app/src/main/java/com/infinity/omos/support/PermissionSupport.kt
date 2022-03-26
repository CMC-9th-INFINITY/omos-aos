package com.infinity.omos.support

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class PermissionSupport(private val context: Context, private val activity: Activity) {
    lateinit var permissionList: ArrayList<String>
    private val MULTIPLE_PERMISSIONS = 1023
    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) // 요청할 권한

    // 허용 받아야 할 권한 체크
    fun checkPermission(): Boolean {
        var result: Int
        permissionList = ArrayList()

        for (pm in permissions) {
            result = ContextCompat.checkSelfPermission(context, pm)
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm)
            }
        }
        return permissionList.isEmpty()
    }

    // 권한 요청
    fun requestPermission() {
        ActivityCompat.requestPermissions(
            activity,
            (permissionList.toTypedArray()), MULTIPLE_PERMISSIONS
        )
    }

    fun permissionResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ): Boolean {
        if (requestCode == MULTIPLE_PERMISSIONS && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == -1) {
                    // 사용자가 거부한 권한이 존재하면,
                    return false
                }
            }
        }
        return true
    }
}