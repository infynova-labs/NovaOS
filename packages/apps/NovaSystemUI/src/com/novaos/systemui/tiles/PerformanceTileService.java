package com.novaos.systemui.tiles;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

public class PerformanceTileService extends TileService {
    private static final String TAG = "PerformanceTileService";
    private int mProfile = 1; // 0: Battery, 1: Balanced, 2: Performance

    @Override
    public void onClick() {
        mProfile = (mProfile + 1) % 3;
        android.os.SystemProperties.set("persist.nova.power.profile", String.valueOf(mProfile));
        updateTileState();
        Log.d(TAG, "Performance Profile toggled to: " + mProfile);
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        try {
            mProfile = Integer.parseInt(android.os.SystemProperties.get("persist.nova.power.profile", "1"));
        } catch (NumberFormatException e) {
            mProfile = 1;
        }
        updateTileState();
    }

    private void updateTileState() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(Tile.STATE_ACTIVE);
            switch (mProfile) {
                case 0:
                    tile.setLabel("Battery Saver");
                    break;
                case 1:
                    tile.setLabel("Balanced Mode");
                    break;
                case 2:
                    tile.setLabel("Performance Mode");
                    break;
            }
            tile.updateTile();
        }
    }
}
