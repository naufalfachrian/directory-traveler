package id.sugarknife.hibikihagyu.extension

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

fun Application.hasStoragePermission(): Boolean {
    return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
}