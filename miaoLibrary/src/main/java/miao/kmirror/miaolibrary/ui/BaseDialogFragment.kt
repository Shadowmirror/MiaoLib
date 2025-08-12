package miao.kmirror.miaolibrary.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.annotation.LayoutRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import miao.kmirror.miaolibrary.R


/**
 * Dialog 配置参数封装，统一管理弹窗行为与外观
 */
data class DialogConfig(
    val cancelableOutside: Boolean = true,      // 点击外部是否关闭弹窗
    val interceptBack: Boolean = false,          // 是否拦截返回键（true 拦截）
    val dimAmount: Float = 0.5f,                 // 背景遮罩透明度
    val backgroundDrawable: Int? = null,         // 背景资源 id（覆盖 dimAmount）
    val backgroundColor: Int? = null,            // 背景颜色（覆盖 dimAmount）

    val showStatusBar: Boolean = true,          // 是否显示状态栏，false = 隐藏
    val showNavigationBar: Boolean = true       // 是否显示导航栏，false = 隐藏
)

/**
 * 基础弹窗Fragment，支持 ViewBinding、Compose、ViewModel 注入等多种功能
 *
 * @param VB ViewBinding 类型
 * @param VM ViewModel 类型
 */
abstract class BaseDialogFragment<VB : ViewBinding, VM : ViewModel> : DialogFragment() {

    /** 弹窗配置参数，子类必须提供 */
    abstract val config: DialogConfig

    /** ViewModel 实例，子类必须提供 */
    protected abstract val viewModel: VM

    /** ViewBinding 实例，XML 模式下使用 */
    protected lateinit var binding: VB

    /**
     * 返回布局资源 id (XML 模式必须实现)
     * Compose 模式返回 null
     */
    @LayoutRes
    open fun getLayoutRes(): Int? = null

    /**
     * XML 模式初始化 ViewBinding，必须实现
     * @param root XML 根布局View
     * @return 绑定的 ViewBinding 实例
     */
    open fun initBinding(root: View): VB {
        throw NotImplementedError("XML 模式必须重写 initBinding")
    }

    /**
     * 初始化绑定的视图，设置点击监听、绑定 ViewModel LiveData 等
     * XML 模式下使用
     */
    open fun initViewBinding(binding: VB) {}

    /**
     * Compose 模式下初始化 Compose 内容，返回 Composable Lambda
     * Compose 模式下重写此方法即可
     */
    open fun initComposeView(): (@Composable () -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Miao_TransparentDialog)
        // 控制点击外部是否取消
        isCancelable = config.cancelableOutside
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 设置窗口样式
        dialog?.window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)  // 无标题
            // 设置背景透明或自定义背景
            if (config.backgroundDrawable != null) {
                setBackgroundDrawableResource(config.backgroundDrawable!!)
            } else if (config.backgroundColor != null) {
                setBackgroundDrawable(config.backgroundColor!!.toDrawable())
            } else {
                setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            }
            // 遮罩透明度
            setDimAmount(config.dimAmount)
        }


        // Compose 模式优先
        initComposeView()?.let {
            return ComposeView(requireContext()).apply { setContent { it() } }
        }

        // XML + ViewBinding 模式
        getLayoutRes()?.let {
            val root = inflater.inflate(it, container, false)
            binding = initBinding(root)
            initViewBinding(binding)
            return root
        }

        throw IllegalStateException("必须实现 getLayoutRes() 或 initComposeView()，否则无法创建视图")
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            // 设置宽高全屏
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

//            // 隐藏状态栏和导航栏，沉浸式模式
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                insetsController?.let { controller ->
//                    if (!config.showStatusBar) {
//                        controller.hide(WindowInsets.Type.statusBars())
//                    }
//                    if (!config.showNavigationBar) {
//                        controller.hide(WindowInsets.Type.navigationBars())
//                    }
//                    // 通过系统手势（例如从隐藏系统栏的屏幕边缘滑动）暂时显示隐藏的系统栏。这些临时系统栏会叠加在应用的内容之上，可能具有一定程度的透明度，并且会在短时间超时后自动隐藏。
//                    controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//                }
//            } else {
//                @Suppress("DEPRECATION")
//                var flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
//                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//
//                if (!config.showStatusBar) {
//                    flags = flags or View.SYSTEM_UI_FLAG_FULLSCREEN
//                }
//                if (!config.showNavigationBar) {
//                    flags = flags or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
//                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                }
//
//                decorView.systemUiVisibility = flags
//            }
        }
    }


    override fun onResume() {
        super.onResume()
        // 是否拦截返回键事件
        if (config.interceptBack) {
            dialog?.setOnKeyListener { _, keyCode, _ -> keyCode == KeyEvent.KEYCODE_BACK }
        }
    }

    /**
     * 安全显示弹窗，避免FragmentManager状态保存异常
     */
    fun showSafely(manager: androidx.fragment.app.FragmentManager, tag: String = this::class.java.simpleName) {
        if (!manager.isStateSaved) show(manager, tag)
    }
}