package miao.kmirror.miaolibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
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
}