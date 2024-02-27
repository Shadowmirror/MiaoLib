package miao.kmirror.miaolibrary.ktx

import android.content.Context
import android.os.Parcelable
import com.google.gson.Gson
import java.io.Serializable
import kotlin.math.roundToInt

//dp转px
fun Number.dp2px(context: Context): Int {
    return (this.toFloat() * context.resources.displayMetrics.density).roundToInt()
}

//sp转px
fun Number.sp2px(context: Context): Int {
    return (this.toFloat() * context.resources.displayMetrics.scaledDensity).roundToInt()
}

//px转dp
fun Number.px2dp(context: Context): Int {
    return (this.toFloat() / context.resources.displayMetrics.density).roundToInt()
}

//px转sp
fun Number.px2sp(context: Context): Int {
    return (this.toFloat() / context.resources.displayMetrics.scaledDensity).roundToInt()
}


fun IntArray.toStringArray(): String {
    return this.joinToString(",")
}

fun String.toIntArray(): IntArray {
    val stringArray = this.split(",").toTypedArray()
    return IntArray(stringArray.size) { stringArray[it].toInt() }
}


fun FloatArray.toStringArray(): String {
    return this.joinToString(",")
}

fun String.toFloatArray(): FloatArray {
    val stringArray = this.split(",").toTypedArray()
    return FloatArray(stringArray.size) { stringArray[it].toFloat() }
}

fun List<Number>.toStringList(): String {
    return this.joinToString(",")
}

fun String.toNumberList(): List<Number> {
    val stringArray = this.split(",").toTypedArray()
    return stringArray.map { it.toDouble() }
}


//对Serializable扩展一个deepClone方法
//使继承自Serializable的对象可以序列化然后反序列化，得到一个新对象
fun Serializable.deepClone(): Serializable {
    return Gson().fromJson(Gson().toJson(this), this.javaClass)
}

//对Parcelable扩展一个deepClone方法
//使实现Parcelable接口的对象可以序列化然后反序列化，得到一个新对象
fun Parcelable.deepClone(): Parcelable {
    return Gson().fromJson(Gson().toJson(this), this.javaClass)
}


fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId: Int = this.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = this.resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun Context.getNavigationBarHeight(): Int {
    var result = 0
    val resourceId: Int = this.resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = this.resources.getDimensionPixelSize(resourceId)
    }
    return result
}