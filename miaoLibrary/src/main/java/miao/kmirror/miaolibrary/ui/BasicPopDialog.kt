package miao.kmirror.miaolibrary.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import miao.kmirror.miaolibrary.R
import miao.kmirror.miaolibrary.ktx.initViewBinding

abstract class BasicPopDialog<VB : ViewBinding>(private val mActivity: BasicActivity<*>) : LifecycleObserver {
    protected var mPopupWindow: PopupWindow
    protected var mViewBinding: VB = initViewBinding(this, LayoutInflater.from(mActivity), null, false)
    private var systemUiVisibility = 0


    companion object {
        private val mPopDialogList: ArrayList<BasicPopDialog<*>> = arrayListOf()
        fun getPopDialogList(): List<BasicPopDialog<*>> = mPopDialogList
    }

    init {
        mPopupWindow =
            PopupWindow(mViewBinding.root, getLayoutWidth(), getLayoutHeight(), true).apply {
                animationStyle = R.style.ScaleAnimStyle
                isFocusable = false
                isClippingEnabled = true
                isOutsideTouchable = false
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setOnDismissListener {
                    onDismiss()
                }
            }
        systemUiVisibility = 0
    }


    open fun show() {
        showPopWindow(
            mActivity.window.decorView, getShowGravity(),
            getShowLocationX(), getShowLocationY()
        )
    }


    open fun show(
        locationView: View, gravity: Int = Gravity.NO_GRAVITY, offsetX: Int, offsetY: Int
    ) {
        locationView.post {
            val location = IntArray(2)
            locationView.getLocationOnScreen(location)
            showPopWindow(locationView, gravity, location[0] + offsetX, location[1] + offsetY)
        }
    }

    protected open fun showPopWindow(locationView: View, gravity: Int, x: Int, y: Int) {
        if (mActivity.isFinishing || mActivity.isDestroyed || mPopupWindow.isShowing) {
            return
        }
        getShowAlpha().apply {
            if (this != 1F) {
                setBackgroundAlpha(this)
            }
        }
        locationView.post {
            runCatching {
                mPopupWindow.showAtLocation(locationView, gravity, x, y)
            }.onSuccess {
                onShow()
            }.onFailure {
                onShowFail()
            }
        }
    }

    protected open fun onShow() {
        mPopDialogList.add(this)
        systemUiVisibility = mActivity.window.decorView.systemUiVisibility
        mActivity.apply {
            lifecycle.removeObserver(this@BasicPopDialog)
            lifecycle.addObserver(this@BasicPopDialog)
        }
    }

    open fun isShowing() = mPopupWindow.isShowing
    open fun dismiss() {
        if (mPopupWindow.isShowing) {
            mPopupWindow.dismiss()
        }
    }


    open fun onBackPressed() {
        if (mPopupWindow.isShowing) {
            mPopupWindow.dismiss()
        }
    }


    protected open fun onDismiss() {
        mPopDialogList.remove(this)
        if (systemUiVisibility != 0) {
            mActivity.window.decorView.systemUiVisibility = systemUiVisibility
        }
        getShowAlpha().apply {
            if (this != 1F) {
                setBackgroundAlpha(1F)
            }
        }
        mActivity.lifecycle.removeObserver(this)
    }

    protected open fun setBackgroundAlpha(bgAlpha: Float) {
        val bgAlphaSet = bgAlpha.coerceAtMost(1F).coerceAtLeast(0F)
        val activityWindow = mActivity.window
        val lp = activityWindow.attributes
        lp.alpha = bgAlphaSet
        activityWindow.attributes = lp
        activityWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    protected open fun getLayoutHeight() = WindowManager.LayoutParams.MATCH_PARENT
    protected open fun getLayoutWidth() = WindowManager.LayoutParams.MATCH_PARENT
    protected open fun getShowAlpha(): Float = 1f
    protected open fun getShowGravity() = Gravity.CENTER
    protected open fun getShowLocationX() = 0
    protected open fun getShowLocationY() = 0
    protected open fun onShowFail() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @CallSuper
    protected open fun onActivityDestroy() {
        dismiss()
    }
}