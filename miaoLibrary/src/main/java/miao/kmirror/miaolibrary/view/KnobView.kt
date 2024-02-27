package miao.kmirror.miaolibrary.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.core.graphics.withClip
import miao.kmirror.miaolibrary.R
import kotlin.math.atan2

class KnobView(context: Context, attributeSet: AttributeSet) : BaseProgress(context, attributeSet) {

    private var mMaxProgress = 100f
    private var mMinProgress = 0f
    private var mProgress: Float = 0f
    private var mThumbDrawable: Drawable? = null
    private var mTrackDrawable: Drawable? = null
    private var mProgressDrawable: Drawable? = null
    private var mProgressLayerDrawable: LayerDrawable? = null

    private var mAngle = 0f
    private var mStartAngle = 0f
    private var mEndAngle = 360f

    init {
        attributeSet.let {
            val typeArray = context.obtainStyledAttributes(it, R.styleable.BaseProgress)
            mMaxProgress = typeArray.getFloat(R.styleable.BaseProgress_maxProgress, 100f)
            mMinProgress = typeArray.getFloat(R.styleable.BaseProgress_minProgress, 0f)
            mProgress = typeArray.getFloat(R.styleable.BaseProgress_progress, 0f)

            mProgressLayerDrawable = typeArray.getDrawable(R.styleable.BaseProgress_progressLayerDrawable) as LayerDrawable?

            mProgressLayerDrawable?.apply {
                for (index in 0 until numberOfLayers) {
                    when (getId(index)) {
                        R.id.thumbDrawable -> mThumbDrawable = getDrawable(index)
                        R.id.trackDrawable -> mTrackDrawable = getDrawable(index)
                        R.id.progressDrawable -> mProgressDrawable = getDrawable(index)
                    }
                }
            }
            typeArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mSeekBarListener?.onStartChanging(getProgress())
            }

            MotionEvent.ACTION_MOVE -> {
                parent.requestDisallowInterceptTouchEvent(true)
                updateAngle(event.x, event.y)
                mSeekBarListener?.onChanging(getProgress())
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mSeekBarListener?.onStopChanging(getProgress())
            }
        }
        return true
    }

    private fun updateAngle(pointX: Float, pointY: Float): Float {
        mAngle = Math.toDegrees(atan2(pointY - height / 2, pointX - width / 2).toDouble()).toFloat()
        mAngle += 270
        if (mAngle < 0) {
            mAngle += 360 // 将角度转换为正值
        } else if (mAngle >= 360) {
            mAngle -= 360
        }
        Log.i("mirror", "mAngle = $mAngle")
        return mAngle
    }

    fun getProgress() = mProgress
    fun getProgressRange() = mMaxProgress - mMinProgress

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