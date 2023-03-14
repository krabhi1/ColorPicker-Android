package com.abhishek.colorpicker

import android.content.Context
import android.graphics.*
import android.util.AttributeSet

class HueSlider(context: Context, attrs: AttributeSet) :SliderView(context,attrs) {
    private val huePaint= Paint()
    private var bitmap:Bitmap?=null
    private val hueRect=RectF()
    override fun onDraw(canvas: Canvas) {
        if (bitmap!=null){
            canvas.drawBitmap(bitmap!!,null,getRectF(),null)
        }
        super.onDraw(canvas)
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        onSize()
    }
    private fun onSize(){
        updatePaint()
        //update bitmap
        bitmap=Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        updateBitmap()
    }

    private fun updateBitmap() {
        if(bitmap!=null){
            val spaceH=getRadius().toFloat()
            val spaceV=2.toPx.toFloat()
            val w=maxTrackSize()
            hueRect.fromXYWH(
                spaceH,spaceV,w,
                measuredHeight.toFloat()-(spaceV*2)
            )
            val canvas=Canvas(bitmap!!)
            canvas.drawRect(hueRect,huePaint)
        }
    }

    private fun updatePaint(){
        val w = measuredWidth-getRadius()*2
        val h = measuredHeight
        val hueIncrement = 360f / w
        var currentHue = 0f

        val hsv = FloatArray(3)
        hsv[1] = 1f
        hsv[2] = 1f

        val colors = IntArray(w)

        for (i in 0 until w) {
            hsv[0] = currentHue
            colors[i] = Color.HSVToColor(hsv)
            currentHue += hueIncrement
        }

        // Create a linear gradient from the start color to the end color
        huePaint.shader = LinearGradient(
            getRadius().toFloat(), 0f,w.toFloat()+getRadius(), 0f,
            colors, null,
            Shader.TileMode.CLAMP
        )
    }
    fun setHue(hue:Int){
        //just set hue
        println("hue $hue")
        setRatio(hue / 360f)
    }
    fun getColor():Int{
        return Color.HSVToColor(floatArrayOf(360*currentRatio,1f,1f))
    }
}