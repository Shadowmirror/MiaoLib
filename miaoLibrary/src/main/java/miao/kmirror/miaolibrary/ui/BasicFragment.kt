package miao.kmirror.miaolibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import miao.kmirror.miaolibrary.ktx.initViewBinding

abstract class BasicFragment<VB : ViewBinding>(private var mIsLazyLoad: Boolean = true) : Fragment() {

    protected var mViewBinding: VB? = null
    private var mInitLazyLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewBinding = initViewBinding<VB>(this, inflater, container, false)
        return mViewBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        if (!mIsLazyLoad) {
            initView()
        }
    }

    override fun onResume() {
        super.onResume()
        if (mIsLazyLoad && !mInitLazyLoaded) {
            mInitLazyLoaded = true
            initView()
        }
    }

    abstract fun initData()
    abstract fun initView()

    protected fun isLazyLoad(lazyLoad: Boolean) {
        mIsLazyLoad = lazyLoad
    }
}