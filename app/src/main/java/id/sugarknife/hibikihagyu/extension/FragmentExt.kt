package id.sugarknife.hibikihagyu.extension

import android.content.Context
import android.content.SharedPreferences
import android.support.v4.app.Fragment

val Fragment.preferences: SharedPreferences
    get() {
        return activity.getPreferences(Context.MODE_PRIVATE)
    }