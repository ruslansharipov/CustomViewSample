package ru.sharipov.customviewsample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random
import android.util.TypedValue

class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.attr.standartCutomViewStyle
) : View(context, attrs, defStyle) {

    var colors: IntArray = intArrayOf()
    var onGameOverListener: (() -> Unit)? = null

    private val figures: MutableList<Figure> = mutableListOf()

    private val onTouchListener = OnTouchListener { view: View?, motionEvent: MotionEvent? ->
        when {
            figures.size == 10 -> {
                figures.clear()
                onGameOverListener?.invoke()
            }
            motionEvent?.action == MotionEvent.ACTION_DOWN -> {
                val figure = Figure(
                    motionEvent.x,
                    motionEvent.y,
                    randomSize,
                    randomShape,
                    randomColor
                )
                figures.add(figure)
            }
        }
        invalidate()
        false
    }

    private val paint: Paint = Paint()
    private val rectF: RectF = RectF()
    private val cornerRadiusPx = resources.getDimensionPixelSize(R.dimen.corner_radius).toFloat()
    private val defaultColor: Int

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView)
        defaultColor = typedArray.getColor(R.styleable.CustomView_defaultColor, Color.GREEN)
        typedArray.recycle()

        setOnTouchListener(onTouchListener)
    }

    private val randomColor: Int
        get() = if (colors.isNotEmpty()) {
            val size = colors.size
            val randomIndex = Random.nextInt(size)
            colors[randomIndex]
        } else {
            defaultColor
        }

    private val randomSize: Float
        get() {
            val dip = Random.nextInt(20, 50).toFloat()
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.displayMetrics)
        }

    private val randomShape: Shape
        get() {
            return when (Random.nextInt(3)) {
                0 -> Shape.CIRCLE
                1 -> Shape.RECTANGLE
                else -> Shape.ROUNDED_RECT
            }
        }

    fun setHexColors(colorHexs: Array<String>) {
        colors = colorHexs.map { hexString ->
            Color.parseColor(hexString)
        }.toIntArray()
    }

    override fun onDraw(canvas: Canvas?) {
        for (figure in figures) {
            paint.color = figure.color
            when (figure.shape) {
                Shape.CIRCLE -> canvas?.drawCircle(figure.x, figure.y, figure.size, paint)
                Shape.RECTANGLE -> {
                    setupRectangle(figure)
                    canvas?.drawRect(rectF, paint)
                }
                Shape.ROUNDED_RECT -> {
                    setupRectangle(figure)
                    canvas?.drawRoundRect(rectF, cornerRadiusPx, cornerRadiusPx, paint)
                }
            }
        }
    }

    private fun setupRectangle(figure: Figure) {
        val halfSize = figure.size / 2
        val left = figure.x - halfSize
        val top = figure.y - halfSize
        val right = figure.x + halfSize
        val bottom = figure.y + halfSize
        rectF.set(left, top, right, bottom)
    }
}

private data class Figure(
    val x: Float,
    val y: Float,
    val size: Float,
    val shape: Shape,
    val color: Int
)

private enum class Shape {
    CIRCLE, RECTANGLE, ROUNDED_RECT
}

