package io.novaos.core.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.File
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Singleton

/**
 * NovaVaultManager: Manages encrypted app containers and cloaking logic.
 */
@Singleton
class NovaVaultManager @Inject constructor() {

    companion object {
        private const val VAULT_FILE = "/data/system/nova_vault.list"
        @JvmStatic
        val INSTANCE: NovaVaultManager by lazy { NovaVaultManager() }
        
        @JvmStatic
        fun getInstance() = INSTANCE
    }

    private val KEY_ALIAS = "NovaVaultKey"
    private val ANDROID_KEYSTORE = "AndroidKeyStore"
    
    private val _cloakedPackages = mutableSetOf<String>()

    init {
        getOrCreateKey()
        loadVault()
    }

    private fun loadVault() {
        val file = File(VAULT_FILE)
        if (file.exists()) {
            _cloakedPackages.clear()
            file.readLines().forEach { _cloakedPackages.add(it.trim()) }
        }
    }

    private fun saveVault() {
        try {
            val file = File(VAULT_FILE)
            file.writeText(_cloakedPackages.joinToString("\n"))
        } catch (e: Exception) {
            // Log error
        }
    }

    /**
     * Cloaks an app by removing it from the launcher and suppressing notifications.
     */
    fun cloakApp(packageName: String) {
        _cloakedPackages.add(packageName)
        saveVault()
    }

    /**
     * Uncloaks an app and restores its visibility.
     */
    fun uncloakApp(packageName: String) {
        _cloakedPackages.remove(packageName)
        saveVault()
    }

    fun isAppCloaked(packageName: String): Boolean {
        return _cloakedPackages.contains(packageName)
    }

    /**
     * Encrypts sensitive data using the hardware-backed keystore.
     */
    fun encryptData(data: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_GCM}/${KeyProperties.ENCRYPTION_PADDING_NONE}")
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        return cipher.doFinal(data)
    }

    private fun getOrCreateKey() {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            keyGenerator.init(
                KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setUserAuthenticationRequired(true) // Requires Biometric/PIN
                    .build()
            )
            keyGenerator.generateKey()
        }
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }
}
