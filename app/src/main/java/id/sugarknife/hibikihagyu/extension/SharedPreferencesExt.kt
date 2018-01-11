package id.sugarknife.hibikihagyu.extension

import android.content.SharedPreferences

private val hiddenFilePreferences = "Hidden-File-Preferences"

var SharedPreferences.showHiddenFiles: Boolean
    get() {
        return getBoolean(hiddenFilePreferences, false)
    }
    set(value) {
        edit().putBoolean(hiddenFilePreferences, value)
                .apply()
    }