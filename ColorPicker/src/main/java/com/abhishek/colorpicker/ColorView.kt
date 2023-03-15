package com.abhishek.colorpicker

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.View

class ColorView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var currentColor = Color.argb(200, 255, 100, 150)
    private val gridPaint = Paint().apply {
        style = Paint.Style.FILL
    }
    private val boxSize = Size(10, 10)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = Utils.setWrapSize(100.toPx, 100.toPx, widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(size.width, size.height)

        roundedClipPath.reset()
        roundedClipPath.addRoundRect(0F, 0F, size.width.toFloat(), size.height.toFloat(), roundSize, roundSize, Path.Direction.CW)
    }

    fun setColor(color: Int) {
        if (color != currentColor) {
            currentColor = color
            invalidate()
        }
    }

    private val roundedClipPath = Path()
    private val roundSize = resources.getDimensionPixelSize(R.dimen.color_picker_radi).toFloat()

    override fun onDraw(canvas: Canvas) {
        canvas.clipPath(roundedClipPath)

        drawGrid(canvas)
        canvas.drawColor(currentColor)

    }

    private fun drawGrid(canvas: Canvas) {
        val colorA = Color.WHITE
        val colorB = Color.GRAY
        val countX = measuredWidth / boxSize.width
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

}