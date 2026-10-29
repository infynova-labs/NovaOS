package com.infynova.launcher

import android.os.Bundle
import android.os.SystemProperties
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.util.Log
import android.view.WindowManager
import io.novaos.ui.theme.NovaOSTheme
import com.infynova.launcher.NovaHomeScreen

/**
 * NovaLauncherActivity: The entry point for the NovaOS MVP Launcher.
 * Hosts the Compose-based home screen.
 */
class NovaLauncherActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Optimize for NovaOS High Refresh Rate
        window.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
        
        SystemProperties.set("debug.nova.render_priority", "1")

        setContent {
            NovaOSTheme {
                NovaHomeScreen(
                    onSwipeUp = { Log.d("NovaLauncher", "Swipe Up: App Drawer") },
                    onSwipeDown = { Log.d("NovaLauncher", "Swipe Down: Quick Toggles") }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Ensure smooth transitions
        SystemProperties.set("debug.nova.render_priority", "1")
    }

    override fun onPause() {
        super.onPause()
        SystemProperties.set("debug.nova.render_priority", "0")
    }
}
