package com.novaos.systemui.tiles;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

public class PrivacyModeTileService extends TileService {
    private static final String TAG = "PrivacyModeTileService";
    private boolean mIsActive = false;

    @Override
    public void onClick() {
        mIsActive = !mIsActive;
        // In a real implementation, this would trigger a system-wide privacy mode
        // via a system property or a call to a privacy manager service.
        android.os.SystemProperties.set("persist.nova.privacy_mode", mIsActive ? "1" : "0");
        
        updateTileState();
        Log.d(TAG, "Privacy Mode toggled: " + mIsActive);
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        mIsActive = "1".equals(android.os.SystemProperties.get("persist.nova.privacy_mode", "0"));
        updateTileState();
    }

    private void updateTileState() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(mIsActive ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
            tile.setLabel("Privacy Mode");
            tile.updateTile();
        }
    }
}
