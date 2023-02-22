package com.abhishek.colorpicker

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

open class ColorComposer(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var composeColor=Color.RED
    private lateinit var vShader: LinearGradient
    private lateinit var hShader: LinearGradient
    private val paint= Paint()
    private  var bitmap: Bitmap?=null
    //RATIO
    private var ratioPoint=PointF(0.5f,0.5f)
    private val cursorPaint=Paint().apply {
        style=Paint.Style.STROKE
        color=Color.GRAY
    }
    private var oldColor=Color.BLACK
    private var colorChangeListener:(()->Unit)?=null
    fun setColorChangeListener(f:()->Unit){
        colorChangeListener=f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size=Utils.setWrapSize(100.toPx,100.toPx,widthMeasureSpec,heightMeasureSpec)
        setMeasuredDimension(size.width,size.height)
    }

    override fun onDraw(canvas: Canvas) {
        if (bitmap!=null){
            canvas.drawBitmap(bitmap!!,null,getRectF(),null)
        }
        //draw cursor
        canvas.drawCircle(
            ratioPoint.x*measuredWidth,
            ratioPoint.y*measuredHeight,
            5.toPx.toFloat(),
            cursorPaint
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        onSize()
    }
    private fun onSize() {
        bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        updateShaderAndBitmap()
    }

    private fun updateShaderAndBitmap() {
        this.vShader = LinearGradient(
            0f, 0f, 0f, measuredHeight.toFloat(),
            Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP
        )
        this.hShader = LinearGradient(
            0f, 0f, measuredWidth.toFloat(), 0f,
            Color.WHITE, Utils.toRGB(composeColor), Shader.TileMode.CLAMP
        )
        paint.shader = ComposeShader(vShader, hShader, PorterDuff.Mode.MULTIPLY)
        if (bitmap != null) {
            val canvas=Canvas(bitmap!!)
            canvas.drawPaint(paint)
        }
    }
    private fun updateRatioPoint(e: MotionEvent) {
        val p=PointF(
            e.x/measuredWidth.toFloat(),
            e.y/measuredHeight.toFloat(),
        )
        if(p.x<0)p.x=0f
        if(p.x>1)p.x=1f

        if(p.y<0)p.y=0f
        if(p.y>1)p.y=1f

        setRatioPoint(p)
    }


    override fun onTouchEvent(e: MotionEvent): Boolean {
        when(e.action){
            MotionEvent.ACTION_DOWN->{
                updateRatioPoint(e)
                return true
            }
            MotionEvent.ACTION_MOVE->{
                updateRatioPoint(e)
                return true
            }
            MotionEvent.ACTION_UP->{
                return true
            }

        }

        return super.onTouchEvent(e)
    }
    private fun updateCurrentColor() {
        val current = getColor()
        println("$oldColor $current")

        if (oldColor != current) {
            oldColor = current
            invalidate()
            colorChangeListener?.invoke()
        }

    }
    private fun setRatioPoint(p:PointF){
        if(p.x !=ratioPoint.x || p.y!=ratioPoint.y){
            ratioPoint=p
            updateCurrentColor()
            invalidate()
        }
    }
    fun setComposeColor(color:Int){
        if(color!=composeColor){
            composeColor=color
            updateShaderAndBitmap()
            updateCurrentColor()
        }
    }
    fun setColor(color: Int){
        if(getColor()!=color){
            //update
            val hsv=color.toHSV()
            setComposeColor(Color.HSVToColor(floatArrayOf(hsv[0], 1f, 1f)))
            setRatioPoint(
                PointF(
                    hsv[1],
                    1 - hsv[2]
                )
            )
        }
    }
    fun getColor(): Int {
        val hsv = composeColor.toHSV()
        hsv[1] = ratioPoint.x
        hsv[2] = 1f-ratioPoint.y
        return Color.HSVToColor(hsv)
    }

}