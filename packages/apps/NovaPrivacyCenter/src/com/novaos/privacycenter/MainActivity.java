package com.novaos.privacycenter;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.novaos.privacycenter.adapter.AppPermissionAdapter;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.os.INovaGuardian;
import android.os.RemoteException;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.novaos.privacycenter.adapter.TimelineAdapter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mList;
    private AppPermissionAdapter mPermissionAdapter;
    private TimelineAdapter mTimelineAdapter;
    private List<ApplicationInfo> mApps;
    private INovaGuardian mGuardian;

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(android.content.ComponentName name, IBinder service) {
            mGuardian = INovaGuardian.Stub.asInterface(service);
            if (mPermissionAdapter != null) {
                mPermissionAdapter.setGuardian(mGuardian);
            }
            refreshTimeline();
        }

        @Override
        public void onServiceDisconnected(android.content.ComponentName name) {
            mGuardian = null;
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();
        intent.setClassName("android", "com.android.server.NovaGuardianService"); // Simulated
        // In AOSP, we'd usually use ServiceManager.getService("nova_guardian") 
        // since it's a system service, but for this simulation we use bind if needed.
        // Better: use ServiceManager directly if available to the app.
        IBinder b = android.os.ServiceManager.getService("nova_guardian");
        if (b != null) {
            mGuardian = INovaGuardian.Stub.asInterface(b);
        }
        
        mList = findViewById(R.id.main_list);
        mList.setLayoutManager(new LinearLayoutManager(this));

        PackageManager pm = getPackageManager();
        mApps = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        
        mPermissionAdapter = new AppPermissionAdapter(this, mApps);
        if (mGuardian != null) mPermissionAdapter.setGuardian(mGuardian);
        
        mTimelineAdapter = new TimelineAdapter(new ArrayList<>());
        refreshTimeline();

        mList.setAdapter(mPermissionAdapter);

        setupTabs();
    }

    private void refreshTimeline() {
        if (mGuardian != null) {
            try {
                List<android.os.PrivacyEvent> events = mGuardian.getPrivacyTimeline();
                List<TimelineAdapter.TimelineItem> items = new ArrayList<>();
                for (android.os.PrivacyEvent e : events) {
                    items.add(new TimelineAdapter.TimelineItem(e.packageName, e.action, "Just now"));
                }
                mTimelineAdapter.updateItems(items);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupTabs() {
        Button btnPermissions = findViewById(R.id.tab_permissions);
        Button btnTimeline = findViewById(R.id.tab_timeline);
        Button btnNetwork = findViewById(R.id.tab_network);
        Button btnVault = findViewById(R.id.tab_vault);
        Button btnSecurity = findViewById(R.id.tab_security);

        btnPermissions.setOnClickListener(v -> {
            mPermissionAdapter.setDisplayMode(AppPermissionAdapter.DisplayMode.PERMISSIONS);
            mList.setAdapter(mPermissionAdapter);
            findViewById(R.id.empty_text).setVisibility(View.GONE);
            updateTabColors(btnPermissions, btnTimeline, btnNetwork, btnVault, btnSecurity);
        });

        btnTimeline.setOnClickListener(v -> {
            mList.setAdapter(mTimelineAdapter);
            findViewById(R.id.empty_text).setVisibility(View.GONE);
            updateTabColors(btnTimeline, btnPermissions, btnNetwork, btnVault, btnSecurity);
        });

        btnNetwork.setOnClickListener(v -> {
            mPermissionAdapter.setDisplayMode(AppPermissionAdapter.DisplayMode.NETWORK);
            mList.setAdapter(mPermissionAdapter);
            findViewById(R.id.empty_text).setVisibility(View.GONE);
            updateTabColors(btnNetwork, btnPermissions, btnTimeline, btnVault, btnSecurity);
        });

        btnVault.setOnClickListener(v -> {
            openVault();
            updateTabColors(btnVault, btnPermissions, btnTimeline, btnNetwork, btnSecurity);
        });

        btnSecurity.setOnClickListener(v -> {
            showSecurityStatus();
            updateTabColors(btnSecurity, btnPermissions, btnTimeline, btnNetwork, btnVault);
        });
    }

    private void showSecurityStatus() {
        String status = android.os.SystemProperties.get("sys.novaguard.status", "unknown");
        String message = "System Integrity: " + status.toUpperCase();
        
        if ("compromised".equalsIgnoreCase(status)) {
            message += "\nWarning: Device may be rooted or partitions tampered!";
        } else if ("secure".equalsIgnoreCase(status)) {
            message += "\nEverything looks good. NovaGuard is active.";
        }

        mList.setAdapter(null); // Clear list for status view
        TextView empty = findViewById(R.id.empty_text);
        empty.setText(message);
        empty.setVisibility(View.VISIBLE);
        
        if ("compromised".equalsIgnoreCase(status)) {
            empty.setTextColor(0xFFFF0000);
        } else {
            empty.setTextColor(0xFF00FF00);
        }
    }

    private void openVault() {
        // Vault Logic: In a full ROM, this triggers a biometric challenge
        // then mounts the encrypted partition. For demo, we show a toast.
        Toast.makeText(this, "Privacy Vault: Authenticate to view secure files.", Toast.LENGTH_LONG).show();
        // Intent to launch VaultActivity would go here
    }

    public void openAppDetails(String packageName) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Cannot open settings for " + packageName, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTabColors(Button active, Button... inactive) {
        active.setTextColor(0xFF007FAC);
        for (Button btn : inactive) {
            btn.setTextColor(0xFFFFFFFF);
        }
    }
}
