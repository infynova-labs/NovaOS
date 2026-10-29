package com.novaos.settings.privacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import io.novaos.core.privacy.NovaPrivacyManager
import io.novaos.core.privacy.ui.PrivacyHubContent
import io.novaos.ui.theme.NovaOSTheme

class NovaPrivacyHubFragment : Fragment() {

    private val privacyManager = NovaPrivacyManager.getInstance().apply { 
        // Initial state update
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        privacyManager.updateState(requireContext())
        return ComposeView(requireContext()).apply {
            setContent {
                val isSpoofModeEnabled by privacyManager.isSpoofModeEnabled.collectAsState()
                
                NovaOSTheme {
                    PrivacyHubContent(
                        isSpoofModeEnabled = isSpoofModeEnabled,
                        onSpoofModeToggle = { privacyManager.toggleSpoofMode(context, it) }
                    )
                }
            }
        }
    }
}
