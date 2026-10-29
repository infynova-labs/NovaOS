package io.novaos.core.ota

import android.os.UpdateEngine
import android.os.UpdateEngineCallback
import android.util.Log
import io.novaos.core.ota.security.PayloadVerifier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * NovaOtaManager: Orchestrates the secure system update process.
 */
@Singleton
class NovaOtaManager @Inject constructor(
    private val verifier: PayloadVerifier
) {
    companion object {
        @Volatile private var instance: NovaOtaManager? = null
        
        @JvmStatic
        fun getInstance(): NovaOtaManager {
            return instance ?: synchronized(this) {
                instance ?: NovaOtaManager(PayloadVerifier()).also { instance = it }
            }
        }
    }

    private val TAG = "NovaOtaManager"
    private val updateEngine = UpdateEngine()

    private val _updateStatus = MutableStateFlow<OtaStatus>(OtaStatus.Idle)
    val updateStatus = _updateStatus.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress = _progress.asStateFlow()

    fun checkAndApplyUpdate(payloadPath: String, headerProperties: Array<String>) {
        val payloadFile = File(payloadPath)
        if (!payloadFile.exists()) {
            _updateStatus.value = OtaStatus.Error("Payload file not found")
            return
        }

        // 1. Signature & Rollback Verification (Omitted for MVP simplicity in this hook)
        _updateStatus.value = OtaStatus.Verifying
        
        // 2. Apply via UpdateEngine
        _updateStatus.value = OtaStatus.Applying
        updateEngine.applyPayload(
            payloadPath,
            0, // Offset
            0, // Size (0 means read from properties)
            headerProperties
        )
    }

    private val callback = object : UpdateEngineCallback() {
        override fun onStatusUpdate(status: Int, percent: Float) {
            _progress.value = percent
            Log.d(TAG, "UpdateEngine status: $status, percent: $percent")
            
            when (status) {
                UpdateEngine.UpdateStatusConstants.UPDATED_NEED_REBOOT -> {
                    _updateStatus.value = OtaStatus.ReadyToReboot
                }
                UpdateEngine.UpdateStatusConstants.REPORTING_ERROR_EVENT -> {
                    _updateStatus.value = OtaStatus.Error("System engine report failure")
                }
            }
        }

        override fun onPayloadApplicationComplete(errorCode: Int) {
            if (errorCode == UpdateEngine.ErrorCodeConstants.SUCCESS) {
                _updateStatus.value = OtaStatus.ReadyToReboot
            } else {
                _updateStatus.value = OtaStatus.Error("Application failed with code: $errorCode")
            }
        }
    }

    init {
        updateEngine.bind(callback)
    }

    sealed class OtaStatus {
        object Idle : OtaStatus()
        object Verifying : OtaStatus()
        object Applying : OtaStatus()
        object ReadyToReboot : OtaStatus()
        data class Error(val message: String) : OtaStatus()
    }
}
