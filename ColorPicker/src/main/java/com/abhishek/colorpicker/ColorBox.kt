package com.abhishek.colorpicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class ColorBox(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var colorHolder = ColorHolder()
    private val paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLACK
    }
    private val rect = RectF()
    override fun onDraw(canvas: Canvas) {
        makeRects { rect, i, j ->
            paint.color = colorHolder.colors[i + j * 5]
            if (paint.color == 0) paint.color = Color.BLACK
            canvas.drawRect(rect, paint)
            return@makeRects false
        }
    }

    private fun makeRects(callback: (rect: RectF, i: Int, j: Int) -> Boolean) {
        val w = measuredWidth
        val h = measuredHeight
        val spaceV = 4.toPx //in dp to pixel
        val totalVSpace = 3 * spaceV
        val size = (h - totalVSpace) / 2
        val totalHSpace = w - 5 * size
        val spaceH = totalHSpace / 6.0f.toInt()
        var x = 0
        var y = spaceV
        for (j in 0..1) {
            x = spaceH
            for (i in 0..4) {
                rect.fromXYWH(x.toFloat(), y.toFloat(), size.toFloat(), size.toFloat())
                if (callback(rect, i, j)) break
                x += size + spaceH
            }
            y += size * (1 + j) + spaceV
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = Utils.setWrapSize(200.toPx, 80.toPx, widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(size.width, size.height)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                makeRects { rect, i, j ->
                    if (rect.isInside(e.x, e.y)) {
                        onColorClick(i + j * 5)
                        return@makeRects true
                    }
                    return@makeRects false
                }
                return true
            }
        }
        return super.onTouchEvent(e)
    }

    private fun onColorClick(index: Int) {
        colorClickListener?.invoke(colorHolder.colors[index])
    }

    fun setColorHolder(holder: ColorHolder) {
        colorHolder = holder
        invalidate()
    }

    private var colorClickListener: ColorCallback? = null
    fun setColorClickListener(callback: ColorCallback) {
        colorClickListener = callback
    }
}
typealias ColorCallback = (color: Int) -> Unit