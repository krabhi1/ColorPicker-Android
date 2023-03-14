package com.abhishek.colorpicker

import android.content.res.Resources
import android.graphics.*
import android.util.Size
import android.util.TypedValue
import android.view.View

object Utils {
    fun setWrapSize( widthInPixel:Int,heightInPixel:Int,widthMeasureSpec: Int, heightMeasureSpec: Int): Size {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        //Measure Width
        val width = when (widthMode) {
            View.MeasureSpec.EXACTLY -> {
                //Must be this size
                widthSize
            }
            View.MeasureSpec.AT_MOST -> {
                //Can't be bigger than...
                Math.min(widthInPixel, widthSize)
            }
            else -> {
                //Be whatever you want
                widthInPixel
            }
        }
        //Measure Height
        val height = when (heightMode) {
            View.MeasureSpec.EXACTLY -> {
                //Must be this size
                heightSize
            }
            View.MeasureSpec.AT_MOST -> {
                //Can't be bigger than...
                Math.min(heightInPixel, heightSize)
            }
            else -> {
                //Be whatever you want
                heightInPixel
            }
        }
        return Size(width,height)
    }
    fun fillPaint(color:Int= Color.BLACK):Paint=Paint().apply {
        style=Paint.Style.FILL
        this.color=color
    }
    fun strokePaint(color:Int= Color.BLACK):Paint=Paint().apply {
        style=Paint.Style.STROKE
        this.color=color
    }
    fun toRGB(colorWithAlpha: Int):Int{
        return Color.rgb(Color.red(colorWithAlpha), Color.green(colorWithAlpha), Color.blue(colorWithAlpha))
    }
    fun colorWithAlpha(a:Int,color: Int):Int{
        return Color.argb(a,Color.red(color), Color.green(color), Color.blue(color))
    }
}
fun View.getBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    draw(canvas)
    return bitmap
}
fun RectF.fromXYWH(x:Float,y:Float,w:Float,h:Float): RectF {
    left=x
    top=y
    right=x+w
    bottom=y+h
    return this
}

fun Int.toHSV(): FloatArray {
    val hsv = FloatArray(3)
    Color.colorToHSV(this, hsv)
    return hsv
}

fun Int.colorInfo(): String {
    val hsl = this.toHSV()
    return "argba ${Color.alpha(this)} ${Color.red(this)} ${Color.green(this)} ${Color.blue(this)} " +
            "hsv ${hsl[0]} ${hsl[1]} ${hsl[2]}"
}

fun View.getRectF() = RectF().fromXYWH(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
val Number.toPx get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics).toInt()