package miao.kmirror.miaolibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val mViewBinding get() = _binding!!
    private var mIsLazyLoad = true
    private var mInitLazyLoaded = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return mViewBinding.root
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

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    protected fun isLazyLoad(lazyLoad: Boolean) {
        mIsLazyLoad = lazyLoad
    }

}