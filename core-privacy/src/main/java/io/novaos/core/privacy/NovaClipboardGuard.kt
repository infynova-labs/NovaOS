package io.novaos.core.privacy

import android.content.ClipboardManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * NovaClipboardGuard: Blocks background clipboard access.
 * Requires an explicit user interaction (e.g., tap on a tooltip) to grant buffer access.
 */
@Singleton
class NovaClipboardGuard @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        @Volatile private var instance: NovaClipboardGuard? = null
        
        @JvmStatic
        fun getInstance(context: Context): NovaClipboardGuard {
            return instance ?: synchronized(this) {
                instance ?: NovaClipboardGuard(context.applicationContext).also { instance = it }
            }
        }
    }

    private val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    
    private val _pendingAccessRequest = MutableStateFlow<String?>(null)
    val pendingAccessRequest = _pendingAccessRequest.asStateFlow()

    /**
     * Intercepts a clipboard read request.
     * Returns the content only if the user has explicitly authorized it.
     */
    fun requestClipboardAccess(requestingPackage: String): String? {
        // If the app is in the background, we trigger the "Physical Tap" UI
        if (isPackageInBackground(requestingPackage)) {
            _pendingAccessRequest.value = requestingPackage
            return null // Block immediate access
        }
        
        return clipboardManager.primaryClip?.getItemAt(0)?.text?.toString()
    }

    fun grantAccess() {
        _pendingAccessRequest.value = null
        // Trigger the actual paste event or return data to the waiting app
    }

    fun denyAccess() {
        _pendingAccessRequest.value = null
    }

    private fun isPackageInBackground(packageName: String): Boolean {
        // Implementation using ActivityManager to check process state
        return true // Defaulting to true for demonstration
    }
}
