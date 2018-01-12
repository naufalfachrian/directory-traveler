package id.sugarknife.hibikihagyu.extension

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity

val sharedPreferencesName = "hibiki-hagyu-preferences"

fun AppCompatActivity.askStoragePermission(requestCode: Int) {
    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), requestCode)
}

val AppCompatActivity.preferences: SharedPreferences
    get() {
        return getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
    }