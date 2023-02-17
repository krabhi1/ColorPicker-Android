package com.abhishek.colorpicker

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Size

class AlphaSlider(context: Context, attrs: AttributeSet) :SliderView(context,attrs) {
    private var currentColor= Color.BLACK
    private val gridPaint= Paint().apply {
        style=Paint.Style.FILL
    }
    private val boxSize= Size(10,10)
    private val gradientPaint=Paint().apply {
        style=Paint.Style.FILL
    }
    override fun onDraw(canvas: Canvas) {
        //drawGrid
        val spaceH=getRadius().toFloat()
        val spaceV=2.toPx.toFloat()
        val w=maxTrackSize()
        val h=measuredHeight-spaceV*2f
        val rect=RectF().fromXYWH(
            spaceH,spaceV,w,h
        )
        canvas.save()
        canvas.clipRect(rect)
        canvas.translate(spaceH,spaceV)
        drawGrid(canvas,w.toInt())
        canvas.restore()
        canvas.drawRect(rect,gradientPaint)
        super.onDraw(canvas)
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        onSize()
    }
    private fun onSize(){
        gradientPaint.shader= LinearGradient(
            0f,0f,
            measuredWidth.toFloat(),0f,0,Utils.toRGB(currentColor),Shader.TileMode.CLAMP
        )
    }
    private fun drawGrid(canvas: Canvas,w:Int) {
        val colorA=Color.WHITE
        val colorB=Color.GRAY
        val countX=w/ boxSize.width
        val countY=measuredHeight/ boxSize.height
        for (y in 0..countY){
            for (x in 0..countX){
                val rect= RectF().apply {
                    left=x*boxSize.width.toFloat()
                    top=y*boxSize.height.toFloat()
                    right+=left+boxSize.width
                    bottom+=top+boxSize.height
                }
                gridPaint.color=if((x+y)%2==0)colorA else colorB
                canvas.drawRect(rect,gridPaint)
            }
        }
    }
    //public
    fun setColor(color:Int){
        if (currentColor!=color){
            currentColor=color
            invalidate()
        }
    }
    fun setAlpha(alpha:Int){
       //just set ratio
        setRatio(alpha/255f)
    }
    fun getAlphaValue(): Int {
        return (currentRatio*255).toInt()
    }
}