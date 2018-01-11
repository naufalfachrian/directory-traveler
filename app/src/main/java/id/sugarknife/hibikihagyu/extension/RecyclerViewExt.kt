package id.sugarknife.hibikihagyu.extension

import android.support.annotation.AnimRes
import android.support.v7.widget.RecyclerView
import android.view.animation.AnimationUtils

fun RecyclerView.runAnimation(@AnimRes res: Int) {
    val controller = AnimationUtils.loadLayoutAnimation(context, res)
    layoutAnimation = controller
    scheduleLayoutAnimation()
}
