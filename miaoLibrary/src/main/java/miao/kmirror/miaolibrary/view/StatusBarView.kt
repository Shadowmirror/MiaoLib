package miao.kmirror.miaolibrary.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import miao.kmirror.miaolibrary.ktx.getStatusBarHeight

class StatusBarView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, context.getStatusBarHeight())
    }
}