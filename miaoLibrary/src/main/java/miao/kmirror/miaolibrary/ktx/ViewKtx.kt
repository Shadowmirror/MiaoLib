package miao.kmirror.miaolibrary.ktx

import android.os.SystemClock
import android.view.View
import android.view.ViewGroup


/**
 * 设置 View 高
 * */
fun View.height(height: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    params.height = height
    layoutParams = params
    return this
}

/**
 * 设置 View 宽
 * */
fun View.width(width: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    params.width = width
    layoutParams = params
    return this
}

/**
 * 设置 View 宽高
 * */
fun View.widthAndHeight(width: Int, height: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    params.width = width
    params.height = height
    layoutParams = params
    return this
}

/**
 * 设置 View margin
 * */
fun View.margin(
    leftMargin: Int = Int.MAX_VALUE,
    topMargin: Int = Int.MAX_VALUE,
    rightMargin: Int = Int.MAX_VALUE,
    bottomMargin: Int = Int.MAX_VALUE
): View {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    if (leftMargin != Int.MAX_VALUE)
        params.leftMargin = leftMargin
    if (topMargin != Int.MAX_VALUE)
        params.topMargin = topMargin
    if (rightMargin != Int.MAX_VALUE)
        params.rightMargin = rightMargin
    if (bottomMargin != Int.MAX_VALUE)
        params.bottomMargin = bottomMargin
    layoutParams = params
    return this
}


// View 的防抖点击事件扩展函数
fun View.click(debounceTime: Long = 1000L, onClick: (View) -> Unit) {
    var lastClickTime = 0L
    this.setOnClickListener { view ->
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastClickTime >= debounceTime) {
            lastClickTime = currentTime
            onClick(view)
        }
    }
}

// 全局防抖点击事件
object GlobalDebouncedClickListener : View.OnClickListener {
    private const val GLOBAL_DEBOUNCE_TIME = 1000L // 默认全局防抖时间间隔
    private var lastClickTime = 0L

    override fun onClick(view: View) {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastClickTime >= GLOBAL_DEBOUNCE_TIME) {
            lastClickTime = currentTime
            // 执行点击事件
            view.performClick()
        }
    }
}

// 设置全局点击事件防抖
fun View.clickGlobal() {
    this.setOnClickListener(GlobalDebouncedClickListener)
}


fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.inVisible() {
    visibility = View.INVISIBLE
}
val View.isGone: Boolean
    get() {
        return visibility == View.GONE
    }

val View.isVisible: Boolean
    get() {
        return visibility == View.VISIBLE
    }

val View.isInvisible: Boolean
    get() {
        return visibility == View.INVISIBLE
    }
