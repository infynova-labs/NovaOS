package io.novaos.core.privacy

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * NovaPrivacyShield: The MVP Security Hero.
 * Consolidated manager for privacy-first OS interactions.
 */
@Singleton
class NovaPrivacyShield @Inject constructor(
    @ApplicationContext private val context: Context,
    private val privacyManager: NovaPrivacyManager,
    private val clipboardGuard: NovaClipboardGuard
) {
    companion object {
        @Volatile private var instance: NovaPrivacyShield? = null
        
        @JvmStatic
        fun getInstance(context: Context): NovaPrivacyShield {
            return instance ?: synchronized(this) {
                val pm = NovaPrivacyManager.getInstance()
                val cg = NovaClipboardGuard.getInstance(context)
                instance ?: NovaPrivacyShield(context.applicationContext, pm, cg).also { instance = it }
            }
        }
    }

    /**
     * Intercepts clipboard read requests.
     */
    fun handleClipboardAccess(packageName: String): String? {
        return clipboardGuard.requestClipboardAccess(packageName)
    }

    /**
     * Toggles the global synthetic data spoofing layer.
     */
    fun setSpoofMode(enabled: Boolean) {
        privacyManager.toggleSpoofMode(context, enabled)
    }

    fun isSpoofModeActive(): Boolean = privacyManager.isSpoofModeEnabled.value
}
