package com.android.systemui.statusbar.policy;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

public class NetworkTrafficView extends TextView {

    private static final String NETWORK_TRAFFIC_STATE = "network_traffic_state";
    private static final String NETWORK_TRAFFIC_MODE = "network_traffic_mode";

    private boolean mEnabled;
    private int mMode; // 0: Disabled, 1: Upload, 2: Download, 3: Both

    private long mLastTxBytes;
    private long mLastRxBytes;
    private long mLastUpdateTime;

    private final Handler mHandler = new Handler();
    private final Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            updateTraffic();
            mHandler.postDelayed(this, 1000);
        }
    };

    private final ContentObserver mObserver = new ContentObserver(mHandler) {
        @Override
        public void onChange(boolean selfChange) {
            updateSettings();
        }
    };

    public NetworkTrafficView(Context context) {
        this(context, null);
    }

    public NetworkTrafficView(Context context, AttributeSet attrs) {
        super(context, attrs);
        updateSettings();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ContentResolver res = getContext().getContentResolver();
        res.registerContentObserver(Settings.System.getUriFor(NETWORK_TRAFFIC_STATE), false, mObserver, UserHandle.USER_ALL);
        res.registerContentObserver(Settings.System.getUriFor(NETWORK_TRAFFIC_MODE), false, mObserver, UserHandle.USER_ALL);
        if (mEnabled) {
            startTrafficUpdate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getContext().getContentResolver().unregisterContentObserver(mObserver);
        stopTrafficUpdate();
    }

    private void updateSettings() {
        ContentResolver res = getContext().getContentResolver();
        mEnabled = Settings.System.getIntForUser(res, NETWORK_TRAFFIC_STATE, 0, UserHandle.USER_CURRENT) == 1;
        mMode = Settings.System.getIntForUser(res, NETWORK_TRAFFIC_MODE, 0, UserHandle.USER_CURRENT);

        if (mEnabled && mMode > 0) {
            setVisibility(View.VISIBLE);
            startTrafficUpdate();
        } else {
            setVisibility(View.GONE);
            stopTrafficUpdate();
        }
    }

    private void startTrafficUpdate() {
        mLastTxBytes = TrafficStats.getTotalTxBytes();
        mLastRxBytes = TrafficStats.getTotalRxBytes();
        mLastUpdateTime = SystemClock.elapsedRealtime();
        mHandler.removeCallbacks(mUpdateRunnable);
        mHandler.post(mUpdateRunnable);
    }

    private void stopTrafficUpdate() {
        mHandler.removeCallbacks(mUpdateRunnable);
    }

    private void updateTraffic() {
        long currentTxBytes = TrafficStats.getTotalTxBytes();
        long currentRxBytes = TrafficStats.getTotalRxBytes();
        long currentTime = SystemClock.elapsedRealtime();
        long timeDelta = currentTime - mLastUpdateTime;

        if (timeDelta <= 0) return;

        long txSpeed = (currentTxBytes - mLastTxBytes) * 1000 / timeDelta;
        long rxSpeed = (currentRxBytes - mLastRxBytes) * 1000 / timeDelta;

        StringBuilder sb = new StringBuilder();
        if (mMode == 1 || mMode == 3) {
            sb.append("↑ ").append(formatSpeed(txSpeed));
        }
        if (mMode == 3) {
            sb.append("  ");
        }
        if (mMode == 2 || mMode == 3) {
            sb.append("↓ ").append(formatSpeed(rxSpeed));
        }

        setText(sb.toString());

        mLastTxBytes = currentTxBytes;
        mLastRxBytes = currentRxBytes;
        mLastUpdateTime = currentTime;
    }

    private String formatSpeed(long bytesPerSecond) {
        if (bytesPerSecond < 1024) {
            return bytesPerSecond + " B/s";
        } else if (bytesPerSecond < 1024 * 1024) {
            return new DecimalFormat("0.##").format(bytesPerSecond / 1024f) + " KB/s";
        } else if (bytesPerSecond < 1024L * 1024 * 1024) {
            return new DecimalFormat("0.##").format(bytesPerSecond / (1024f * 1024f)) + " MB/s";
        } else {
            return new DecimalFormat("0.##").format(bytesPerSecond / (1024f * 1024f * 1024f)) + " GB/s";
        }
    }

}
