package com.infynova.launcher.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.GridLayout
import android.widget.ImageView
import com.infynova.launcher.R

/**
 * LargeFolderWidget — 2x2 grid widget for direct app launches.
 */
class LargeFolderWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : GridLayout(context, attrs) {

    init {
        rowCount = 2
        columnCount = 2
        setPadding(16, 16, 16, 16)
        setBackgroundResource(R.drawable.large_folder_bg)
        
        // Add 4 mock icons for now
        for (i in 0 until 4) {
            val icon = ImageView(context).apply {
                setImageResource(android.R.drawable.sym_def_app_icon)
                layoutParams = LayoutParams().apply {
                    width = 120
                    height = 120
                    setMargins(8, 8, 8, 8)
                }
            }
            addView(icon)
        }
    }
}
