package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;

public class BatteryBarView extends View {

    private static final String STATUS_BAR_BATTERY_BAR = "status_bar_battery_bar";
    private static final String STATUS_BAR_BATTERY_BAR_COLOR = "status_bar_battery_bar_color";
    private static final String STATUS_BAR_BATTERY_BAR_THICKNESS = "status_bar_battery_bar_thickness";

    private boolean mEnabled;
    private int mBatteryLevel;
    private boolean mBatteryCharging;
    private int mColor = 0xFFFFFFFF;
    private int mThickness = 4;
    private final Paint mPaint = new Paint();

    private final ContentObserver mObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            updateSettings();
        }
    };

    private final BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            mBatteryCharging = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0) == BatteryManager.BATTERY_STATUS_CHARGING;
            invalidate();
        }
    };

    public BatteryBarView(Context context) {
        this(context, null);
    }

    public BatteryBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setAntiAlias(true);
        updateSettings();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        getContext().registerReceiver(mBatteryReceiver, filter);

        ContentResolver res = getContext().getContentResolver();
        res.registerContentObserver(Settings.System.getUriFor(STATUS_BAR_BATTERY_BAR), false, mObserver, UserHandle.USER_ALL);
        res.registerContentObserver(Settings.System.getUriFor(STATUS_BAR_BATTERY_BAR_COLOR), false, mObserver, UserHandle.USER_ALL);
        res.registerContentObserver(Settings.System.getUriFor(STATUS_BAR_BATTERY_BAR_THICKNESS), false, mObserver, UserHandle.USER_ALL);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getContext().unregisterReceiver(mBatteryReceiver);
        getContext().getContentResolver().unregisterContentObserver(mObserver);
    }

    private void updateSettings() {
        ContentResolver res = getContext().getContentResolver();
        mEnabled = Settings.System.getIntForUser(res, STATUS_BAR_BATTERY_BAR, 0, UserHandle.USER_CURRENT) == 1;
        String colorStr = Settings.System.getStringForUser(res, STATUS_BAR_BATTERY_BAR_COLOR);
        mColor = (colorStr != null) ? Color.parseColor(colorStr) : 0xFFFFFFFF;
        mThickness = Settings.System.getIntForUser(res, STATUS_BAR_BATTERY_BAR_THICKNESS, 4, UserHandle.USER_CURRENT);

        mPaint.setColor(mColor);
        setVisibility(mEnabled ? View.VISIBLE : View.GONE);
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, mThickness);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mEnabled) return;

        float width = getWidth();
        float progress = width * mBatteryLevel / 100f;
        canvas.drawRect(0, 0, progress, getHeight(), mPaint);
    }

}
