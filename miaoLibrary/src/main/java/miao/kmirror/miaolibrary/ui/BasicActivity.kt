package miao.kmirror.miaolibrary.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import miao.kmirror.miaolibrary.ktx.initViewBinding

abstract class BasicActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var mViewBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        mViewBinding = initViewBinding<VB>(this, layoutInflater)
        setContentView(mViewBinding.root)
        initData()
        initView()
        initLister()
    }


    abstract fun initView()
    abstract fun initData()
    abstract fun initLister()


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (BasicPopDialog.getPopDialogList().isNotEmpty()) {
            return false
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
        if (BasicPopDialog.getPopDialogList().isNotEmpty()) {
            BasicPopDialog.getPopDialogList().last().onBackPressed()
        } else {
            super.onBackPressed()
        }
    }
}