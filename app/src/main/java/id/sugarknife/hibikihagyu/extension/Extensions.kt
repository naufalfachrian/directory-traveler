package id.sugarknife.hibikihagyu.extension

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.support.annotation.AnimRes
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.animation.AnimationUtils
import java.io.File


//region Extensions variables

private val sharedPreferencesName = "hibiki-hagyu-preferences"

private val hiddenFilePreferences = "Hidden-File-Preferences"

//endregion


//region Activity extensions

fun Activity.askStoragePermission(requestCode: Int) {
    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), requestCode)
}

val Activity.preferences: SharedPreferences
    get() {
        return getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
    }

//endregion


//region Application extensions

fun Application.hasStoragePermission(): Boolean {
    val storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    return ContextCompat.checkSelfPermission(this, storagePermission) == PackageManager.PERMISSION_GRANTED
}

//endregion


//region File extensions

fun File.visibleListFiles(context: Context): List<File> {
    val preferences = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
    var files: List<File> = listFiles().toList()
    if (!preferences.showHiddenFiles) {
        files = files.filter { !it.isHidden }
    }
    return files
}

//endregion


//region RecyclerView extensions

fun RecyclerView.runAnimation(@AnimRes res: Int) {
    val controller = AnimationUtils.loadLayoutAnimation(context, res)
    layoutAnimation = controller
    scheduleLayoutAnimation()
}

//endregion


//region SharedPreferences extensions

var SharedPreferences.showHiddenFiles: Boolean
    get() {
        return getBoolean(hiddenFilePreferences, false)
    }
    set(value) {
        edit().putBoolean(hiddenFilePreferences, value)
                .apply()
    }

//endregion
