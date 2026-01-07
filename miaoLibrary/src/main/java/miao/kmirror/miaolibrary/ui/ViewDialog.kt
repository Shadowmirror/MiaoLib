package miao.kmirror.miaolibrary.ui

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner


class ViewDialog(
    private val activity: Activity,
    private val cancelableOnBack: Boolean = true, // 是否允许返回键关闭
    private val showBgShadow: Boolean = true, // 是否展示默认底层蒙版
    private val bgShadowColorArgb: Int = 0x7F000000,
    private val animator: DialogAnimator = DialogAnimatorDefault.ScaleAlphaFromCenterAnimator
) : DefaultLifecycleObserver {

    private var rootView: View? = null
    private var overlay: View? = null
    private var isShowing = false
    private val container: ViewGroup by lazy {
        activity.findViewById(android.R.id.content)
    }

    // 增加一个返回键回调
    private var backCallback: OnBackPressedCallback? = null

    init {
        if (activity is ComponentActivity) {
            activity.lifecycle.addObserver(this)
        }
    }

    /** XML 布局 */
    fun show(layoutResId: Int, onViewCreated: ((View) -> Unit)? = null) {
        if (isShowing) return
        val contentView = LayoutInflater.from(activity).inflate(layoutResId, container, false)
        onViewCreated?.invoke(contentView)
        showView(contentView)

    }

    /** Compose 布局 */
    fun showCompose(content: @Composable () -> Unit) {
        if (isShowing) return
        val contentView = ComposeView(activity).apply { setContent { content() } }
        showView(contentView)
    }

    /** 核心显示逻辑，带蒙版 */
    private fun showView(view: View) {
        if (isShowing) return
        rootView = view

        // 创建蒙版层
        val overlayView = View(activity).apply {
            if (showBgShadow) {
                setBackgroundColor(bgShadowColorArgb) // 半透明黑色
            } else {
                setBackgroundColor(0x00000000) // 全透明
            }

            isClickable = true // 拦截点击事件，避免穿透
        }
        overlay = overlayView

        // 绑定 lifecycle owner
        if (activity is ComponentActivity) {
            view.setViewTreeLifecycleOwner(activity)
            view.setViewTreeViewModelStoreOwner(activity)
            view.setViewTreeSavedStateRegistryOwner(activity)


            // --- 支持 Android 15/16 预测性返回 ---
            if (cancelableOnBack) {
                backCallback = object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        dismiss()
                    }
                }
                // 将回调注册到 Activity，这样系统才能感知到“这一层”可以拦截返回
                activity.onBackPressedDispatcher.addCallback(activity, backCallback!!)
            }
        }

//        // 拦截返回键
//        val keyListener = View.OnKeyListener { _, keyCode, event ->
//            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
//                if (cancelableOnBack) dismiss()
//                true
//            } else false
//        }
        overlayView.isFocusableInTouchMode = true
        overlayView.requestFocus()
//        overlayView.setOnKeyListener(keyListener)

        view.isFocusableInTouchMode = true
        view.requestFocus()
//        view.setOnKeyListener(keyListener)

        // 添加蒙版和内容到根布局
        container.addView(
            overlayView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        container.addView(
            view, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        // 保证在最上层
        overlayView.elevation = 9999f
        view.elevation = 10000f

        isShowing = true
        animator.animateEnter(view, overlayView)
    }

    fun dismiss() {
        if (!isShowing) return

        // 移除回调，否则 Activity 正常的返回键会失效
        backCallback?.remove()
        backCallback = null

        val content = rootView
        val mask = overlay

        if (content == null || mask == null) {
            cleanup()
            return
        }

        animator.animateExit(content, mask) {
            cleanup()
        }
    }

    private fun cleanup() {
        rootView?.let { container.removeView(it) }
        overlay?.let { container.removeView(it) }
        rootView = null
        overlay = null
        isShowing = false
    }

    override fun onDestroy(owner: LifecycleOwner) {
        dismiss()
        if (activity is ComponentActivity) activity.lifecycle.removeObserver(this)
    }
}
