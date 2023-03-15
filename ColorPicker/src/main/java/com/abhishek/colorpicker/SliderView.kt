package com.abhishek.colorpicker

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Size
import android.view.MotionEvent
import android.view.View

open class SliderView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val wheelPaint = Utils.fillPaint(Color.WHITE).apply {
        isAntiAlias = true
        setShadowLayer(
            5f,
            0f, 0f,
            Color.GRAY
        )
    }
    protected var currentRatio = 0f
    private var ratioChangeListener: ((t: Float) -> Unit)? = null
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = Utils.setWrapSize(100.toPx + paddingLeft + paddingRight, 20.toPx + paddingTop + paddingBottom, widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(size.width, size.height)
    }

    protected fun getRadius(): Int {
        val maxRadius = 30.toPx
        var radius = (measuredHeight - paddingTop - paddingBottom) * 0.6f
        if (radius > maxRadius) {
            radius = maxRadius.toFloat()
        }
        return radius.toInt()
    }

    protected fun maxTrackSize() = measuredWidth - getRadius() * 2f - paddingLeft - paddingRight
    private fun updateRatio(e: MotionEvent) {
        val rad = getRadius()
        val maxSize = maxTrackSize()
        val t = if (e.x < rad) {
            0f
        } else if (e.x > maxSize + rad) {
            1f
        } else {
            (e.x - rad) / maxSize
        }
        setRatio(t)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                updateRatio(e)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                updateRatio(e)
                return true
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return super.onTouchEvent(e)
    }

    override fun onDraw(canvas: Canvas) {
        drawWheel(canvas)
    }


    private var halfHeight = 0F
    private fun drawWheel(canvas: Canvas) {
        //draw wheel
        halfHeight = measuredHeight * 0.5f

        canvas.drawCircle(getRadius() + paddingLeft + maxTrackSize() * currentRatio, halfHeight, getRadius().toFloat(), wheelPaint)
    }

    //public
    fun setOnChangeListener(callback: (t: Float) -> Unit) {
        ratioChangeListener = callback
    }

    fun getRatio() = currentRatio
    fun setRatio(t: Float) {
        if (t != currentRatio && t >= 0f && t <= 1f) {
            currentRatio = t
            invalidate()
            ratioChangeListener?.invoke(t)
        }
    }
}