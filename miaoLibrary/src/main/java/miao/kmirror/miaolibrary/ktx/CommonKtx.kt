package miao.kmirror.miaolibrary.ktx

import android.content.Context
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