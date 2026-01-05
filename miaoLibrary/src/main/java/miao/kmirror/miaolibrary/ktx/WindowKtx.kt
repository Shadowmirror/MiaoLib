package miao.kmirror.miaolibrary.ktx

import android.app.Activity
import android.view.View
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * 隐藏系统状态栏和导航栏（沉浸模式）
 */
fun Activity.hideSystemBars() {
    window.hideSystemBars()
}

/**
 * 隐藏系统状态栏和导航栏（沉浸模式）
 */
fun Window.hideSystemBars() {
    val windowInsetsController = WindowCompat.getInsetsController(this, decorView)
    windowInsetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
}

/**
 * 针对直接通过 WindowManager 添加的 View 隐藏系统栏
 */
fun View.hideSystemBars() {
    // 等待 View 附加到窗口后再执行，否则 getInsetsController 可能返回 null
    post {
        val window = (context as? Activity)?.window ?: return@post
        val windowInsetsController = WindowCompat.getInsetsController(window, this)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }
}

/**
 * 显示系统状态栏和导航栏
 */
fun Activity.showSystemBars() {
    window.showSystemBars()
}

/**
 * 显示系统状态栏和导航栏
 */
fun Window.showSystemBars() {
    val windowInsetsController = WindowCompat.getInsetsController(this, decorView)
    windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
}
