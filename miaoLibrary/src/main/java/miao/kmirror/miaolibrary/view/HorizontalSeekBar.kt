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
    private var mMinProgress = 0
    private var mProgress: Float = 0f
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
            mMinProgress = typeArray.getInt(R.styleable.BaseProgress_minProgress, 0)
            mProgress = typeArray.getFloat(R.styleable.BaseProgress_progress, 0f)

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
        mThumbX = (getStartPadding() + getProgressWidth() * mProgress / mMaxProgress).toInt()
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
                    getProgressStart(),
                    getProgressTop().toInt(),
                    getProgressEnd(),
                    getProgressBottom().toInt()
                )
//                draw(canvas)
            }
            canvas.withClip(
                Rect(
                    getProgressStart(),
                    getProgressTop().toInt(),
                    mThumbX,
                    getProgressBottom().toInt()
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
            MotionEvent.ACTION_DOWN -> {
                event.x.let {
                    moveThumb(it)
                }
                mSeekBarListener?.onStartChanging(getProgress())
            }

            MotionEvent.ACTION_MOVE -> {
                parent.requestDisallowInterceptTouchEvent(true)
                event.x.let {
                    moveThumb(it)
                }
                mSeekBarListener?.onChanging(getProgress())
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                event.x.let {
                    moveThumb(it)
                }
                mSeekBarListener?.onStopChanging(getProgress())
            }
        }
        return true
    }

    private fun moveThumb(eventX: Float) {
        if (eventX < getProgressStart()) {
            mThumbX = getProgressStart()
        } else if (eventX > getProgressEnd()) {
            mThumbX = getProgressEnd()
        } else {
            mThumbX = eventX.toInt()
        }
        invalidate()
        mProgress = ((mThumbX.toFloat() - getProgressStart()) / getProgressWidth() * getProgressRange() + mMinProgress).apply {
            this.coerceAtMost(mMaxProgress.toFloat()).coerceAtLeast(mMinProgress.toFloat())
        }
    }


    fun getProgress() = mProgress

    fun getProgressRange() = mMaxProgress - mMinProgress

    private fun getTrackStart() = getStartPadding()
    private fun getTrackEnd() = width - getEndPadding()
    private fun getTrackTop() = measuredHeight / 2 - mTrackHeight / 2
    private fun getTrackBottom() = measuredHeight / 2 + mTrackHeight / 2
    private fun getTrackWidth() = getTrackEnd() - getTrackStart()

    private fun getProgressStart() = getStartPadding()
    private fun getProgressEnd() = width - getEndPadding()
    private fun getProgressTop() = measuredHeight / 2 - mProgressHeight / 2
    private fun getProgressBottom() = measuredHeight / 2 + mProgressHeight / 2
    private fun getProgressWidth() = getProgressEnd() - getProgressStart()


    private fun getStartPadding() = ceil(mThumbWidth / 2.0).toInt() + paddingStart
    private fun getEndPadding() = ceil(mThumbWidth / 2.0).toInt() + paddingEnd

    private var mSeekBarListener: SeekBarListener? = null
    fun setSeekBarListener(seekBarListener: SeekBarListener) {
        mSeekBarListener = seekBarListener
    }

    interface SeekBarListener {
        fun onChanging(progress: Float)
        fun onStartChanging(progress: Float)
        fun onStopChanging(progress: Float)
    }
}