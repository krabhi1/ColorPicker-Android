package com.abhishek.colorpicker

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Size

class AlphaSlider(context: Context, attrs: AttributeSet) : SliderView(context, attrs) {
    private var currentColor = Color.RED
    private val gridPaint = Paint().apply {
        style = Paint.Style.FILL
    }
    private val boxSize = Size(10, 10)
    private val gradientPaint = Paint().apply {
        style = Paint.Style.FILL
    }


    private val rect = RectF()
    private val roundSize = resources.getDimensionPixelSize(R.dimen.color_picker_radi).toFloat()
    private val clipPath = Path()
    override fun onDraw(canvas: Canvas) {

        //drawGrid
        val x = getRadius().toFloat() + paddingLeft
        val y = paddingTop.toFloat()
        val w = maxTrackSize()
        val h = (measuredHeight - paddingTop - paddingBottom).toFloat()
        rect.fromXYWH(x, y, w, h)
        canvas.save()
        clipPath.reset()
        clipPath.addRoundRect(rect, roundSize, roundSize, Path.Direction.CCW)
        canvas.clipPath(clipPath)

        canvas.translate(x, y)
        drawGrid(canvas, w.toInt())
        canvas.restore()
        canvas.drawRoundRect(rect, roundSize, roundSize, gradientPaint)
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        onSize()
    }

    private fun onSize() {
        updateShader()
    }

    private fun updateShader() {
        gradientPaint.shader = LinearGradient(
            0f, 0f,
            measuredWidth.toFloat(), 0f, 0, Utils.toRGB(currentColor), Shader.TileMode.CLAMP
        )
    }

    private fun drawGrid(canvas: Canvas, w: Int) {
        val colorA = Color.WHITE
        val colorB = Color.GRAY
        val countX = w / boxSize.width
        val countY = measuredHeight / boxSize.height
        for (y in 0..countY) {
            for (x in 0..countX) {
                val rect = RectF().apply {
                    left = x * boxSize.width.toFloat()
                    top = y * boxSize.height.toFloat()
                    right += left + boxSize.width
                    bottom += top + boxSize.height
                }
                gridPaint.color = if ((x + y) % 2 == 0) colorA else colorB
                canvas.drawRect(rect, gridPaint)
            }
        }
    }

    //public
    fun setColor(color: Int) {
        if (currentColor != color) {
            currentColor = color
            updateShader()
            invalidate()
        }
    }

    fun setAlpha(alpha: Int) {
        //just set ratio
        setRatio(alpha / 255f)
    }

    fun getAlphaValue(): Int {
        return (currentRatio * 255).toInt()
    }
}