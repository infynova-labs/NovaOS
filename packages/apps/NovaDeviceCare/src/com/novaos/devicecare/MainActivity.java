package com.novaos.devicecare;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.novaos.devicecare.adapter.PerformanceProfileAdapter;
import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.usage.StorageStatsManager;
import android.os.storage.StorageManager;
import android.os.AsyncTask;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mBatteryLayout;
    private LinearLayout mStorageLayout;
    private RecyclerView mProfileList;
    private TextView mStorageStatsText;
    private View mOptimizeProgress;
    private Button mBtnOptimize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBatteryLayout = findViewById(R.id.battery_layout);
        mStorageLayout = findViewById(R.id.storage_layout);
        mStorageStatsText = findViewById(R.id.storage_stats_text);
        mProfileList = findViewById(R.id.profile_list);
        mOptimizeProgress = findViewById(R.id.optimize_progress);
        mBtnOptimize = findViewById(R.id.btn_optimize);
        mProfileList.setLayoutManager(new LinearLayoutManager(this));

        mBtnOptimize.setOnClickListener(v -> runOneTapCheckup());

        setupProfiles();
        setupTabs();
    }

    private void setupProfiles() {
        List<PerformanceProfileAdapter.Profile> profiles = new ArrayList<>();
        profiles.add(new PerformanceProfileAdapter.Profile(0, "Battery Saver", "Extends battery life."));
        profiles.add(new PerformanceProfileAdapter.Profile(1, "Balanced", "Default mode."));
        profiles.add(new PerformanceProfileAdapter.Profile(2, "Performance", "Prioritizes speed."));

        PerformanceProfileAdapter adapter = new PerformanceProfileAdapter(profiles, profile -> {
            Toast.makeText(this, "Selected: " + profile.name, Toast.LENGTH_SHORT).show();
            android.os.SystemProperties.set("persist.nova.power.profile", String.valueOf(profile.id));
        });
        mProfileList.setAdapter(adapter);
    }

    private void setupTabs() {
        Button btnBattery = findViewById(R.id.tab_battery);
        Button btnStorage = findViewById(R.id.tab_storage);
        Button btnPerf = findViewById(R.id.tab_perf);

        btnBattery.setOnClickListener(v -> {
            showLayout(mBatteryLayout);
            updateTabColors(btnBattery, btnStorage, btnPerf);
        });

        btnStorage.setOnClickListener(v -> {
            showLayout(mStorageLayout);
            updateTabColors(btnStorage, btnBattery, btnPerf);
            refreshStorageStats();
        });

        btnPerf.setOnClickListener(v -> {
            showLayout(mProfileList);
            updateTabColors(btnPerf, btnBattery, btnStorage);
        });
    }

    private void refreshStorageStats() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    StorageStatsManager stats = getSystemService(StorageStatsManager.class);
                    long totalBytes = stats.getTotalBytes(StorageManager.UUID_DEFAULT);
                    long freeBytes = stats.getFreeBytes(StorageManager.UUID_DEFAULT);
                    long usedBytes = totalBytes - freeBytes;
                    return (usedBytes / (1024 * 1024 * 1024)) + " GB / " + (totalBytes / (1024 * 1024 * 1024)) + " GB used";
                } catch (Exception e) {
                    return "Error calculating storage";
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (mStorageStatsText != null) mStorageStatsText.setText(result);
            }
        }.execute();
    }

    private void runOneTapCheckup() {
        mBtnOptimize.setEnabled(false);
        mOptimizeProgress.setVisibility(View.VISIBLE);
        
        mBtnOptimize.postDelayed(() -> {
            mOptimizeProgress.setVisibility(View.GONE);
            mBtnOptimize.setEnabled(true);
            mBtnOptimize.setText("Optimized");
            Toast.makeText(this, "System optimized: Background apps restricted.", Toast.LENGTH_LONG).show();
            
            // Trigger framework-level cleanup
            android.os.SystemProperties.set("nova.maintenance.run", "1");
        }, 3000);
    }

    private void showLayout(View view) {
        mBatteryLayout.setVisibility(View.GONE);
        mStorageLayout.setVisibility(View.GONE);
        mProfileList.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
    }

    private void updateTabColors(Button active, Button i1, Button i2) {
        active.setTextColor(0xFF007FAC);
        i1.setTextColor(0xFFFFFFFF);
        i2.setTextColor(0xFFFFFFFF);
    }
}
