package com.infynova.launcher.overlay

import android.content.Context
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.infynova.launcher.R

/**
 * DynamicIslandOverlay — Morphs system status bar area into an interactive HUD.
 */
class DynamicIslandOverlay(private val context: Context) {

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var islandView: FrameLayout? = null
    private var currentParams: WindowManager.LayoutParams? = null

    enum class IslandState { IDLE, COMPACT, EXPANDED, CHARGING, MEDIA }

    fun show(state: IslandState = IslandState.COMPACT, data: Bundle? = null) {
        if (islandView == null) {
            createView()
        }
        updateState(state, data)
    }

    private fun createView() {
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            80, // Base height
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            y = 12
        }
        currentParams = params

        islandView = FrameLayout(context).apply {
            setBackgroundResource(R.drawable.launcher_glass_bar)
            clipToOutline = true
        }

        windowManager.addView(islandView, params)
    }

    fun updateState(state: IslandState, data: Bundle? = null) {
        val view = islandView ?: return
        val params = currentParams ?: return

        view.removeAllViews()

        when (state) {
            IslandState.IDLE -> {
                params.width = 200
                params.height = 80
            }
            IslandState.COMPACT -> {
                params.width = 400
                params.height = 80
                addTextView("Nova Intelligence Active")
            }
            IslandState.CHARGING -> {
                params.width = 500
                params.height = 100
                val level = data?.getInt("level", 0) ?: 0
                addTextView("Charging: $level%")
            }
            IslandState.MEDIA -> {
                params.width = 600
                params.height = 120
                val title = data?.getString("title", "Unknown Track")
                addTextView("Playing: $title")
            }
            IslandState.EXPANDED -> {
                params.width = 800
                params.height = 300
                addTextView("Advanced System Controls")
            }
        }

        windowManager.updateViewLayout(view, params)
    }

    private fun addTextView(textStr: String) {
        val tv = TextView(context).apply {
            text = textStr
            setTextColor(0xFFFFFFFF.toInt())
            textSize = 14f
            gravity = Gravity.CENTER
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        islandView?.addView(tv)
    }

    fun hide() {
        islandView?.let {
            windowManager.removeView(it)
            islandView = null
        }
    }
}
