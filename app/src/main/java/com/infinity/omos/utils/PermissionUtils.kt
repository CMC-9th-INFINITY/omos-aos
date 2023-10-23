package com.infinity.omos.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
fun requestPermission(
    context: Context,
    permissionState: PermissionState,
    onGrantedAction: () -> Unit
) {
    if (permissionState.status.isGranted) {
        onGrantedAction()
    } else if (!permissionState.status.isGranted && !permissionState.status.shouldShowRationale) {
        permissionState.launchPermissionRequest()
    } else {
        Toast.makeText(context, "[권한] -> [사진 및 동영상] -> [허용] 권한을 허용해주세요.", Toast.LENGTH_LONG).show()
        openAppSettings(context)
    }
}

fun openAppSettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}