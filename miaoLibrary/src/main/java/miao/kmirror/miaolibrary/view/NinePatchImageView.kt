package miao.kmirror.miaolibrary.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import miao.kmirror.miaolibrary.R
import kotlin.math.max
import kotlin.math.min

class NinePatchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var bitmap: Bitmap? = null
    private var originalWidth = 0
    private var originalHeight = 0

    // 分割线像素位置
    private var leftSplit = 0
    private var topSplit = 0
    private var rightSplit = 0
    private var bottomSplit = 0

    // 绘制数据
    private val srcRects = arrayOfNulls<Rect>(9)
    private val dstRects = arrayOfNulls<Rect>(9)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isFilterBitmap = true // 缩放平滑
        isDither = true       // 颜色平滑
    }

    init {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NinePatchView)

            // 获取图片
            val drawable = typedArray.getDrawable(R.styleable.NinePatchView_np_src)
            if (drawable is BitmapDrawable) {
                this.bitmap = drawable.bitmap
                originalWidth = bitmap!!.width
                originalHeight = bitmap!!.height
            }

            // 获取分割线配置
            leftSplit = typedArray.getInt(R.styleable.NinePatchView_np_leftSplit, 0)
            topSplit = typedArray.getInt(R.styleable.NinePatchView_np_topSplit, 0)
            rightSplit = typedArray.getInt(R.styleable.NinePatchView_np_rightSplit, 0)
            bottomSplit = typedArray.getInt(R.styleable.NinePatchView_np_bottomSplit, 0)

            typedArray.recycle()
        }
    }

    // 提供给 Compose 动态更新的接口
    fun setImageResource(@DrawableRes resId: Int) {
        val drawable = context.getDrawable(resId)
        if (drawable is BitmapDrawable) {
            this.bitmap = drawable.bitmap
            originalWidth = bitmap!!.width
            originalHeight = bitmap!!.height
            requestLayout()
            invalidate()
        }
    }

    fun setSplits(left: Int, top: Int, right: Int, bottom: Int) {
        this.leftSplit = left
        this.topSplit = top
        this.rightSplit = right
        this.bottomSplit = bottom
        requestLayout()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (bitmap == null) return
        calculateRects(measuredWidth, measuredHeight)
    }

    private fun calculateRects(viewWidth: Int, viewHeight: Int) {
        if (originalWidth == 0 || originalHeight == 0) return

        val availableWidth = viewWidth - paddingLeft - paddingRight
        val availableHeight = viewHeight - paddingTop - paddingBottom

        if (availableWidth <= 0 || availableHeight <= 0) return

        // --- 逻辑判断模式切换 ---

        // 1. FitCenter 模式：所有 split 为 0
        if (leftSplit == 0 && rightSplit == 0 && topSplit == 0 && bottomSplit == 0) {
            val scale = min(availableWidth.toFloat() / originalWidth, availableHeight.toFloat() / originalHeight)
            val sw = (originalWidth * scale).toInt()
            val sh = (originalHeight * scale).toInt()
            val l = paddingLeft + (availableWidth - sw) / 2
            val t = paddingTop + (availableHeight - sh) / 2

            srcRects[0] = Rect(0, 0, originalWidth, originalHeight)
            dstRects[0] = Rect(l, t, l + sw, t + sh)
            clearOtherRects()
            return
        }

        // 2. Horizontal 模式：top/bottom 为 0，左右有 split
        if (topSplit == 0 && bottomSplit == 0) {
            val baseScale = availableHeight.toFloat() / originalHeight
            val dstLeftWidth = (leftSplit * baseScale).toInt()
            val dstRightWidth = ((originalWidth - rightSplit) * baseScale).toInt()
            val dstStretchWidth = max(0, availableWidth - dstLeftWidth - dstRightWidth)

            val dX = intArrayOf(0, dstLeftWidth, dstLeftWidth + dstStretchWidth, availableWidth)
            val dY = intArrayOf(0, 0, availableHeight, availableHeight)
            val sX = intArrayOf(0, leftSplit, rightSplit, originalWidth)
            val sY = intArrayOf(0, 0, originalHeight, originalHeight)
            fillRects(dX, dY, sX, sY)
            return
        }

        // 3. Vertical 模式：left/right 为 0，上下有 split
        if (leftSplit == 0 && rightSplit == 0) {
            val baseScale = availableWidth.toFloat() / originalWidth
            val dstTopHeight = (topSplit * baseScale).toInt()
            val dstBottomHeight = ((originalHeight - bottomSplit) * baseScale).toInt()
            val dstStretchHeight = max(0, availableHeight - dstTopHeight - dstBottomHeight)

            val dY = intArrayOf(0, dstTopHeight, dstTopHeight + dstStretchHeight, availableHeight)
            val dX = intArrayOf(0, 0, availableWidth, availableWidth)
            val sY = intArrayOf(0, topSplit, bottomSplit, originalHeight)
            val sX = intArrayOf(0, 0, originalWidth, originalWidth)
            fillRects(dX, dY, sX, sY)
            return
        }

        // 4. FitXY 模式：特定配置 (top=0, bottom=height, left=0, right=width)
        if (topSplit == 0 && bottomSplit == originalHeight && leftSplit == 0 && rightSplit == originalWidth) {
            srcRects[0] = Rect(0, 0, originalWidth, originalHeight)
            dstRects[0] = Rect(paddingLeft, paddingTop, paddingLeft + availableWidth, paddingTop + availableHeight)
            clearOtherRects()
            return
        }

        // 5. 默认逻辑：九宫格全部生效
        val sX = intArrayOf(0, leftSplit, rightSplit, originalWidth)
        val sY = intArrayOf(0, topSplit, bottomSplit, originalHeight)

        // 修正坐标计算
        val dX = intArrayOf(0, leftSplit, availableWidth - (originalWidth - rightSplit), availableWidth)
        val dY = intArrayOf(0, topSplit, availableHeight - (originalHeight - bottomSplit), availableHeight)

        fillRects(dX, dY, sX, sY)
    }

    private fun clearOtherRects() {
        for (i in 1..8) {
            srcRects[i] = null
            dstRects[i] = null
        }
    }

    private fun fillRects(dX: IntArray, dY: IntArray, sX: IntArray, sY: IntArray) {
        var index = 0
        val offsetX = paddingLeft
        val offsetY = paddingTop
        for (row in 0..2) {
            for (col in 0..2) {
                srcRects[index] = Rect(sX[col], sY[row], sX[col + 1], sY[row + 1])
                dstRects[index] = Rect(
                    offsetX + dX[col], offsetY + dY[row],
                    offsetX + dX[col + 1], offsetY + dY[row + 1]
                )
                index++
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val bmp = this.bitmap ?: return
        for (i in 0..8) {
            val src = srcRects[i]
            val dst = dstRects[i]
            if (src != null && dst != null && !src.isEmpty && !dst.isEmpty) {
                canvas.drawBitmap(bmp, src, dst, paint)
            }
        }
    }
}

// --- Compose 封装组件 ---

@SuppressLint("LocalContextResourcesRead")
@Composable
fun NinePatchView(
    @DrawableRes drawableRes: Int,
    modifier: Modifier = Modifier,
    leftSplit: Int = 0,
    topSplit: Int = 0,
    rightSplit: Int = 0,
    bottomSplit: Int = 0,
) {
    val context = LocalContext.current

    // remember 保证不会在重组时重复创建实例
    val unifiedView = remember(drawableRes) {
        NinePatchView(context).apply {
            setImageResource(drawableRes)
            setSplits(leftSplit, topSplit, rightSplit, bottomSplit)
        }
    }

    AndroidView(
        factory = { unifiedView },
        modifier = modifier,
        update = { view ->
            // 参数变化时更新 Split
            view.setSplits(leftSplit, topSplit, rightSplit, bottomSplit)
            // 如果 drawableRes 变化也需要更新图片
            view.setImageResource(drawableRes)
        }
    )
}