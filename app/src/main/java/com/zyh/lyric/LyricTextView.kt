package com.zyh.lyric

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.TextView

/**
 *Time:2019/3/28
 *Author:ZYH
 *Description:仿歌词效果
 */
class LyricTextView : TextView {

    private var beforeColor : Int = 0
    private var afterColor : Int = 0
    private lateinit var text : String
    private var textSizes : Float = 0F
    lateinit var beforePaint : Paint
    lateinit var afterPaint : Paint
    private var currentProgress: Float = 0F
    private var baseLine : Int = 0

    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(context, attrs)
    }

    private fun init(context: Context?,attrs: AttributeSet?) {
        val attributes = context!!.obtainStyledAttributes(attrs, R.styleable.LyricTextView)

        attributes?.run {
            //原来的颜色
            text = getString(R.styleable.LyricTextView_text)
            textSizes = getDimension(R.styleable.LyricTextView_textSize, 10F)
            beforeColor = getInt(R.styleable.LyricTextView_beforeColor,Color.WHITE)
            //改变过后的颜色
            afterColor = getInt(R.styleable.LyricTextView_afterColor,Color.BLACK)
            recycle()
        }

        beforePaint = getPaintColor(beforeColor)
        afterPaint = getPaintColor(afterColor)
    }


    //根据颜色获取对应画笔
    private fun getPaintColor(color : Int) : Paint{
        return Paint().apply {
            setColor(color)
            isAntiAlias = true
            isDither = true //防抖动
            textSize = textSizes
        }
    }

    private fun setCurrentProgress(currentProgress: Float) {
        this.currentProgress = currentProgress
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val middle = currentProgress * width
        val fontMetricsInt = beforePaint.fontMetricsInt
        baseLine = ((fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom + height / 2
                + paddingTop / 2) - paddingBottom / 2

        clipRect(canvas, 0f, middle, afterPaint) //之后绘制
        clipRect(canvas, middle, width.toFloat(), beforePaint)//之前绘制

    }

    private fun clipRect(canvas: Canvas, start: Float, region: Float, paint: Paint) {
        //改变的颜色
        canvas.save()
        canvas.clipRect(start + paddingLeft, 0f, region, height.toFloat())
        canvas.drawText(text, paddingLeft.toFloat(), baseLine.toFloat(), paint)
        canvas.restore()
    }

    fun start(duration: Long) {
        val animator = ValueAnimator.ofFloat(0F, 1F)
        animator.duration = duration
        animator.addUpdateListener {
            val value = it.animatedValue as Float
            setCurrentProgress(value)
        }
        animator.start()
    }
}