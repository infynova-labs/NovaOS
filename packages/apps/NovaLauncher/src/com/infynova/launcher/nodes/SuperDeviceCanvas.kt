package com.infynova.launcher.nodes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * SuperDeviceCanvas — Interactive node graph for cross-device routing.
 * (HarmonyOS style device matrix)
 */
class SuperDeviceCanvas @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val nodePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF007FAC.toInt()
        style = Paint.Style.FILL
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x80FFFFFF.toInt()
        strokeWidth = 4f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerX = width / 2f
        val centerY = height / 2f
        
        // Central device (This Phone)
        canvas.drawCircle(centerX, centerY, 60f, nodePaint)
        
        // Nearby devices (Simulated)
        canvas.drawLine(centerX, centerY, centerX - 200, centerY - 200, linePaint)
        canvas.drawCircle(centerX - 200, centerY - 200, 40f, nodePaint)
        
        canvas.drawLine(centerX, centerY, centerX + 200, centerY - 100, linePaint)
        canvas.drawCircle(centerX + 200, centerY - 100, 40f, nodePaint)
    }
}
