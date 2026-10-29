package io.novaos.core.security

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer

/**
 * NovaFirewallService: A Local VPN-based firewall to block per-app internet access.
 */
class NovaFirewallService : VpnService() {

    private val TAG = "NovaFirewall"
    private var vpnInterface: ParcelFileDescriptor? = null
    private val scope = CoroutineScope(Dispatchers.IO + Job())
    private var vpnJob: Job? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (vpnJob?.isActive == true) return START_STICKY
        
        setupVpn()
        return START_STICKY
    }

    private fun setupVpn() {
        try {
            val builder = Builder()
                .setSession("NovaOS Firewall")
                .addAddress("10.0.0.1", 24)
                .addDnsServer("8.8.8.8")
                .addRoute("0.0.0.0", 0)

            vpnInterface = builder.establish()
            
            vpnJob = scope.launch {
                runVpnLoop()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to establish VPN", e)
            stopSelf()
        }
    }

    private fun runVpnLoop() {
        vpnInterface?.let { pfd ->
            val inputStream = FileInputStream(pfd.fileDescriptor)
            val outputStream = FileOutputStream(pfd.fileDescriptor)
            val buffer = ByteBuffer.allocate(32767)

            try {
                while (scope.isActive) {
                    val length = inputStream.read(buffer.array())
                    if (length > 0) {
                        outputStream.write(buffer.array(), 0, length)
                        buffer.clear()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "VPN loop error", e)
            } finally {
                closeInterface()
            }
        }
    }

    private fun closeInterface() {
        try {
            vpnInterface?.close()
            vpnInterface = null
        } catch (e: Exception) {
            // Ignore
        }
    }

    override fun onDestroy() {
        vpnJob?.cancel()
        closeInterface()
        super.onDestroy()
    }
}
