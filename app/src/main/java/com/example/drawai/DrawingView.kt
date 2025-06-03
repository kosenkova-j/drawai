package com.example.drawai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var drawPath = Path()
    private var drawPaint: Paint = Paint().apply {
        color = Color.BLACK  // Цвет по умолчанию
        style = Paint.Style.STROKE
        strokeWidth = 8f     // Толщина по умолчанию
        isAntiAlias = true
    }
    private val drawPaths = mutableListOf<Path>()
    private val undoPaths = mutableListOf<Path>()

    // Установка цвета рисования
    fun setColor(color: Int) {
        drawPaint.color = color
        invalidate()
    }

    // Установка толщины кисти
    fun setBrushSize(size: Float) {
        drawPaint.strokeWidth = size
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Рисуем все сохраненные пути
        drawPaths.forEach { canvas.drawPath(it, drawPaint) }
        // Рисуем текущий путь
        canvas.drawPath(drawPath, drawPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                drawPath.moveTo(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                drawPath.lineTo(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                drawPaths.add(drawPath)
                drawPath = Path()
                invalidate()
            }
        }
        return true
    }

    fun clearCanvas() {
        drawPaths.clear()
        undoPaths.clear()
        drawPath = Path()
        invalidate()
    }

    fun getBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)  // Белый фон
        drawPaths.forEach { canvas.drawPath(it, drawPaint) }
        return bitmap
    }
}