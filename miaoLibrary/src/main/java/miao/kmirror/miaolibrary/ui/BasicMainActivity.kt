package miao.kmirror.miaolibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BasicMainActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var mViewBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        mViewBinding = getViewBinding(layoutInflater)
        setContentView(mViewBinding.root)
        initData()
        initView()
        initLister()
    }


    abstract fun initView()
    abstract fun initData()
    abstract fun initLister()


    abstract fun getViewBinding(layoutInflater: LayoutInflater): VB

}