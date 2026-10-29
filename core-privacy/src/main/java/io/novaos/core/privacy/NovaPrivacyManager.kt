package io.novaos.core.privacy

import android.content.Context
import android.location.Location
import android.provider.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * NovaPrivacyManager: The synthetic data spoofing layer.
 * Intercepts requests for sensitive data and provides randomized payloads when Spoof Mode is active.
 */
@Singleton
class NovaPrivacyManager @Inject constructor() {

    companion object {
        private const val NOVA_SPOOF_MODE = "nova_spoof_mode"
        @Volatile private var instance: NovaPrivacyManager? = null
        
        @JvmStatic
        fun getInstance(): NovaPrivacyManager {
            return instance ?: synchronized(this) {
                instance ?: NovaPrivacyManager().also { instance = it }
            }
        }
    }

    private val _isSpoofModeEnabled = MutableStateFlow(false)
    val isSpoofModeEnabled = _isSpoofModeEnabled.asStateFlow()

    fun updateState(context: Context) {
        _isSpoofModeEnabled.value = Settings.Global.getInt(context.contentResolver, NOVA_SPOOF_MODE, 0) == 1
    }

    fun toggleSpoofMode(context: Context, enabled: Boolean) {
        Settings.Global.putInt(context.contentResolver, NOVA_SPOOF_MODE, if (enabled) 1 else 0)
        _isSpoofModeEnabled.value = enabled
    }

    /**
     * Returns a spoofed location if Spoof Mode is enabled; otherwise null.
     */
    fun getSpoofedLocation(originalLocation: Location?): Location? {
        if (!_isSpoofModeEnabled.value) return originalLocation

        return Location("network").apply {
            latitude = Random.nextDouble(-90.0, 90.0)
            longitude = Random.nextDouble(-180.0, 180.0)
            accuracy = 100f
            time = System.currentTimeMillis()
        }
    }

    /**
     * Returns synthetic contact data for apps that shouldn't see the real contact list.
     */
    fun getSyntheticContacts(): List<Map<String, String>> {
        return listOf(
            mapOf("name" to "John Doe (Synthetic)", "phone" to "555-0199"),
            mapOf("name" to "Jane Smith (Synthetic)", "phone" to "555-0123")
        )
    }

    /**
     * Logic to decide if an app's request should be spoofed.
     * In a full AOSP implementation, this would hook into AppOpsManager.
     */
    fun shouldSpoofForPackage(packageName: String): Boolean {
        // Implement allow-list / block-list logic here
        return _isSpoofModeEnabled.value
    }
}
