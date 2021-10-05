package com.example.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.OverScroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import java.lang.reflect.Field
import kotlin.concurrent.fixedRateTimer


/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/10/3
 * Time: 22:42
 */
class AppBarBehavior(context: Context, attributeSet: AttributeSet) :
    AppBarLayout.Behavior(context, attributeSet) {
    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        ev: MotionEvent
    ): Boolean {
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
        }
        return super.onInterceptTouchEvent(parent, child, ev)
    }

    /**
     * 停止appbarLayout的fling事件
     */
    private fun stopAppbarLayoutFling(appBarLayout: AppBarLayout) {
        //通过反射拿到HeaderBehavior中的flingRunnable变量
        val runnable = Runnable::class.java
        val scroller = OverScroller::class.java
        val flingRunnable = runnable.getDeclaredField("flingRunnable").get(this) as Runnable
        val overScroller = scroller.getDeclaredField("scroller").get(this) as OverScroller
//        try {
//            val flingRunnableField: Field = getFlingRunnableField()
//            val scrollerField: Field = getScrollerField()
//            flingRunnableField.isAccessible = true
//            scrollerField.isAccessible = true
//            val flingRunnable = flingRunnableField[this] as Runnable
//            val overScroller = scrollerField[this] as OverScroller
//            if (flingRunnable != null) {
        appBarLayout.removeCallbacks(flingRunnable)
//                flingRunnableField[this] = null
//            }
//            if (overScroller != null && !overScroller.isFinished) {
        overScroller.abortAnimation()
//            }
//        } catch (e: NoSuchFieldException) {
//            e.printStackTrace()
//        } catch (e: IllegalAccessException) {
//            e.printStackTrace()
//        }
    }
//    private fun getScrollerField:Field(){
//        return
//    }
//    private fun getFlingRunnableField:Field(){
//
//    }
}