package com.infinity.omos.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.infinity.omos.R


class RoundImageView : androidx.appcompat.widget.AppCompatImageView {
    private val roundRect = RectF()
    private val maskPaint = Paint()
    private val zonePaint = Paint()
    private var rectRadius = 7f


    constructor(context: Context?) : super(context!!) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        rectRadius = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView).getInt(R.styleable.RoundImageView_radius, 7).toFloat()
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        maskPaint.isAntiAlias = true
        maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        zonePaint.isAntiAlias = true
        zonePaint.color = Color.WHITE
        val density = resources.displayMetrics.density
        rectRadius *= density
    }

    fun setRectRadius(radius: Float) {
        rectRadius = radius
        invalidate()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        roundRect.set(0f, 0f, width.toFloat() - rectRadius, height.toFloat() - rectRadius)
    }

    override fun draw(canvas: Canvas?) {
        canvas?.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG)
        canvas?.drawRoundRect(roundRect, rectRadius, rectRadius, zonePaint)
        canvas?.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG)
        super.draw(canvas)
        canvas?.restore()
    }
}