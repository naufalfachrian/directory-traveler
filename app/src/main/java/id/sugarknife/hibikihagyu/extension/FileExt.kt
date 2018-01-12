package id.sugarknife.hibikihagyu.extension

import android.content.Context
import java.io.File

fun File.visibleListFiles(context: Context): List<File> {
    val preferences = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
    var files: List<File> = listFiles().toList()
    if (!preferences.showHiddenFiles) {
        files = files.filter { !it.isHidden }
    }
    return files
}
