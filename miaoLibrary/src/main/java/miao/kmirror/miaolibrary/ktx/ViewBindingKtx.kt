package miao.kmirror.miaolibrary.ktx

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

inline fun <reified VB : ViewBinding> initViewBinding(view: View): VB {
    var viewBinding: VB? = null
    val viewBindingClazz = VB::class.java
    try {
        val inflate: Method = viewBindingClazz.getDeclaredMethod("bind", View::class.java)
        viewBinding = inflate.invoke(null, view) as VB
    } catch (e: Exception) {
        Log.i("KmirrorTag", viewBindingClazz.simpleName + " init failed ")
    }
    return viewBinding!!

}

inline fun <reified VB : ViewBinding> initViewBinding(
    layoutInflater: LayoutInflater, parent: ViewGroup? = null, contain: Boolean? = false
): VB {
    var viewBinding: VB? = null
    val viewBindingClazz = VB::class.java
    try {
        val inflate: Method = viewBindingClazz.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        viewBinding = inflate.invoke(null, layoutInflater, parent, contain) as VB
    } catch (e: Exception) {
        Log.i("KmirrorTag", viewBindingClazz.simpleName + " init failed ")
    }
    return viewBinding!!
}

fun <VB : ViewBinding> initViewBinding(any: Any, view: View): VB {
    var tempJavaClass: Class<in Any>? = any.javaClass
    var viewBinding: VB? = null
    while (tempJavaClass != null) {
        tempJavaClass.genericSuperclass?.let { type ->
            if (type is ParameterizedType) {
                for (actualTypeArgument in type.actualTypeArguments) {
                    try {
                        val viewBindingClazz = actualTypeArgument as Class<VB>
                        try {
                            val inflate: Method =
                                viewBindingClazz.getDeclaredMethod("bind", View::class.java)
                            viewBinding = inflate.invoke(null, view) as VB
                            tempJavaClass = null
                            break
                        } catch (e: Exception) {
                            Log.i("KmirrorTag", viewBindingClazz.simpleName + " init failed ")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        tempJavaClass = tempJavaClass?.superclass
    }
    return viewBinding!!
}

fun <VB : ViewBinding> initViewBinding(
    any: Any, layoutInflater: LayoutInflater, parent: ViewGroup? = null, contain: Boolean? = false
): VB {
    var tempJavaClass: Class<in Any>? = any.javaClass
    Log.i("KmirrorTag", "initViewBinding ${tempJavaClass?.simpleName} ")
    var viewBinding: VB? = null
    while (tempJavaClass != null) {
        tempJavaClass.genericSuperclass?.let { type ->
            if (type is ParameterizedType) {
                for (actualTypeArgument in type.actualTypeArguments) {
                    try {
                        val viewBindingClazz = actualTypeArgument as Class<VB>
                        try {
                            val inflate: Method = viewBindingClazz.getDeclaredMethod(
                                "inflate",
                                LayoutInflater::class.java,
                                ViewGroup::class.java,
                                Boolean::class.java
                            )
                            viewBinding =
                                inflate.invoke(null, layoutInflater, parent, contain) as VB
                            tempJavaClass = null
                            break
                        } catch (e: Exception) {
                            Log.i("KmirrorTag", viewBindingClazz.simpleName + " init failed ")
                        }
                    } catch (e: Exception) {
                    }
                }
            }
        }
        tempJavaClass = tempJavaClass?.superclass
    }
    return viewBinding!!
}