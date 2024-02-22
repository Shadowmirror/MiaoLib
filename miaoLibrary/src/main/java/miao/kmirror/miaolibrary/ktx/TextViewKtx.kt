package miao.kmirror.miaolibrary.ktx

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.TextView

fun TextView.setTextViewColor(startColor: String, endColor: String) {
    val linearGradient = LinearGradient(
        0f, 0f, 0f, this.textSize,
        intArrayOf(
            Color.parseColor(startColor),
            Color.parseColor(endColor)
        ), floatArrayOf(0f,1f), Shader.TileMode.CLAMP
    )
    this.paint.shader = linearGradient
    this.invalidate()
}