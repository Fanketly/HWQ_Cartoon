package com.example.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout


/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/10/3
 * Time: 22:42
 * 解决appbarLayout若干问题：
 *（1）快速滑动appbarLayout会出现回弹
 *（2）快速滑动appbarLayout到折叠状态下，立马下滑，会出现抖动的问题
 *（3）滑动appbarLayout，无法通过手指按下让其停止滑动
 */
class AppBarBehavior(context: Context, attributeSet: AttributeSet) :
    AppBarLayout.Behavior(context, attributeSet) {

    private var isFlinging = false
    private var shouldBlockNestedScroll = false

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        ev: MotionEvent
    ): Boolean {
        shouldBlockNestedScroll = isFlinging
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            stopAppbarLayoutFling(child)
        }
        return super.onInterceptTouchEvent(parent, child, ev)
    }

    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        stopAppbarLayoutFling(child)
        return super.onStartNestedScroll(
            parent,
            child,
            directTargetChild,
            target,
            nestedScrollAxes,
            type
        )
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            isFlinging = true
        }
        if (!shouldBlockNestedScroll)
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (!shouldBlockNestedScroll)
            super.onNestedScroll(
                coordinatorLayout,
                child,
                target,
                dxConsumed,
                dyConsumed,
                dxUnconsumed,
                dyUnconsumed,
                type,
                consumed
            )
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        abl: AppBarLayout,
        target: View,
        type: Int
    ) {
        isFlinging = false
        shouldBlockNestedScroll = false
        super.onStopNestedScroll(coordinatorLayout, abl, target, type)
    }

    /**
     * 停止appbarLayout的fling事件
     */
    private fun stopAppbarLayoutFling(appBarLayout: AppBarLayout) {
        //通过反射拿到HeaderBehavior中的flingRunnable变量
        with(this.javaClass.superclass.superclass.superclass.getDeclaredField("flingRunnable")) {
            isAccessible = true
            (get(this@AppBarBehavior) as Runnable?)?.run {
                appBarLayout.removeCallbacks(this)
                this@with[this@AppBarBehavior] = null
            }
        }
        with(this.javaClass.superclass.superclass.superclass.getDeclaredField("scroller")) {
            isAccessible = true
            (get(this@AppBarBehavior) as OverScroller?)?.run {
                if (this.isFinished) {
                    this.abortAnimation()
                }
            }
        }
    }

}