package miao.kmirror.miaolibrary.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.core.graphics.withClip
import miao.kmirror.miaolibrary.R
import miao.kmirror.miaolibrary.ktx.dp2px
import miao.kmirror.miaolibrary.ktx.width
import kotlin.math.ceil

class HorizontalSeekBar(context: Context, attributeSet: AttributeSet) : BaseProgress(context, attributeSet) {
    private var mMaxProgress = 100
    private var mProgress = 0
    private var mThumbX = -1
    private var mThumbY = -1

    private var mThumbDrawable: Drawable? = null
    private var mThumbWidth: Float = -1f
    private var mThumbHeight: Float = -1f

    private var mTrackDrawable: Drawable? = null
    private var mTrackHeight: Float = -1f

    private var mProgressDrawable: Drawable? = null
    private var mProgressHeight: Float = -1f

    init {
        attributeSet.let {
            val typeArray = context.obtainStyledAttributes(it, R.styleable.BaseProgress)
            mMaxProgress = typeArray.getInt(R.styleable.BaseProgress_maxProgress, 100)
            mProgress = typeArray.getInt(R.styleable.BaseProgress_progress, 0)

            mThumbDrawable = typeArray.getDrawable(R.styleable.BaseProgress_thumbDrawable)
            mThumbDrawable?.apply {
                mThumbWidth = this.intrinsicWidth.toFloat()
                mThumbHeight = this.intrinsicHeight.toFloat()
            }

            val tempThumbHeight = typeArray.getDimension(R.styleable.BaseProgress_thumbHeight, -1f)
            if (tempThumbHeight > 0) {
                mThumbHeight = tempThumbHeight
            }

            val tempThumbWidth = typeArray.getDimension(R.styleable.BaseProgress_thumbWidth, -1f)
            if (tempThumbWidth > 0) {
                mThumbWidth = tempThumbWidth
            }

            mTrackDrawable = typeArray.getDrawable(R.styleable.BaseProgress_trackDrawable)
            val tempTrackHeight = typeArray.getDimension(R.styleable.BaseProgress_trackHeight, -1f)
            if (tempTrackHeight > 0) {
                mTrackHeight = tempTrackHeight
            } else {
                mTrackHeight = mThumbHeight / 3
            }
            mProgressDrawable = typeArray.getDrawable(R.styleable.BaseProgress_progressDrawable)
            val tempProgressHeight = typeArray.getDimension(R.styleable.BaseProgress_progressHeight, -1f)
            if (tempProgressHeight > 0) {
                mProgressHeight = tempProgressHeight
            } else {
                mProgressHeight = mThumbHeight / 3
            }
            typeArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthMeasureSpec, (mThumbHeight * 1.2).toInt())
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mThumbY = measuredHeight / 2
        mThumbX = getStartPadding() + getTrackWidth() * mProgress / mMaxProgress
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {
            if (mTrackDrawable != null) {
                mTrackDrawable?.apply {
                    setBounds(
                        getTrackStart(),
                        getTrackTop().toInt(),
                        getTrackEnd(),
                        getTrackBottom().toInt()
                    )
                    draw(canvas)
                }
            }

            mProgressDrawable?.apply {
                setBounds(
                    getTrackStart(),
                    getTrackTop().toInt(),
                    getTrackEnd(),
                    getTrackBottom().toInt()
                )
//                draw(canvas)
            }
            canvas.withClip(
                Rect(
                    getTrackStart(),
                    getTrackTop().toInt(),
                    mThumbX,
                    getTrackBottom().toInt()
                )
            ) {
                mProgressDrawable?.draw(this)
            }

            mThumbDrawable?.apply {
                setBounds(
                    (mThumbX - mThumbWidth / 2).toInt(),
                    (mThumbY - mThumbHeight / 2).toInt(),
                    (mThumbX + mThumbWidth / 2).toInt(),
                    (mThumbY + mThumbHeight / 2).toInt()
                )
                draw(canvas)
            }

        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
                event.x.let {
                    if (it < getTrackStart()) {
                        mThumbX = getTrackStart()
                    } else if (it > getTrackEnd()) {
                        mThumbX = getTrackEnd()
                    } else {
                        mThumbX = it.toInt()
                    }
                    Log.i("mirror", "mThumbX = $mThumbX")
                    invalidate()
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
            }
        }
        return true
    }

    private fun getTrackStart() = getStartPadding()
    private fun getTrackEnd() = width - getEndPadding()
    private fun getTrackTop() = measuredHeight / 2 - mTrackHeight / 2
    private fun getTrackBottom() = measuredHeight / 2 + mTrackHeight / 2
    private fun getTrackWidth() = getTrackEnd() - getTrackStart()

    private fun getProgressStart() = getStartPadding()
    private fun getProgressEnd() = width - getEndPadding()
    private fun getProgressTop() = measuredHeight / 2 - mProgressHeight / 2
    private fun getProgressBottom() = measuredHeight / 2 + mProgressHeight / 2


    private fun getStartPadding() = ceil(mThumbWidth / 2.0).toInt() + paddingStart
    private fun getEndPadding() = ceil(mThumbWidth / 2.0).toInt() + paddingEnd
}