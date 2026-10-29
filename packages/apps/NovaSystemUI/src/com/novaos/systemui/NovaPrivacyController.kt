package com.novaos.systemui

import android.content.Context
import android.os.INovaGuardian
import android.os.ServiceManager
import android.view.View
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import io.novaos.core.privacy.ui.PrivacyPill
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NovaPrivacyController @Inject constructor() {

    private fun getGuardian(): INovaGuardian? {
        val binder = ServiceManager.getService("nova_guardian")
        return INovaGuardian.Stub.asInterface(binder)
    }

    fun createPrivacyView(context: Context): View {
        return ComposeView(context).apply {
            setContent {
                var isCameraActive by remember { mutableStateOf(false) }
                var isMicActive by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    while (true) {
                        val guardian = getGuardian()
                        if (guardian != null) {
                            try {
                                isCameraActive = guardian.isCameraActive
                                isMicActive = guardian.isMicActive
                            } catch (e: Exception) {
                                // Service might be restarting
                            }
                        }
                        delay(1000) // Poll every second
                    }
                }

                PrivacyPill(
                    isCameraActive = isCameraActive,
                    isMicActive = isMicActive,
                    onRevokeClick = {
                        // Logic to revoke permissions or open PrivacyCenter
                    }
                )
            }
        }
    }
}
