package com.novaos.privacycenter.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.novaos.privacycenter.MainActivity;
import com.novaos.privacycenter.R;

import java.util.List;
import android.os.INovaGuardian;
import android.os.RemoteException;

public class AppPermissionAdapter extends RecyclerView.Adapter<AppPermissionAdapter.ViewHolder> {

    public enum DisplayMode {
        PERMISSIONS,
        NETWORK
    }

    private final List<ApplicationInfo> mApps;
    private final PackageManager mPm;
    private final MainActivity mActivity;
    private INovaGuardian mGuardian;
    private DisplayMode mMode = DisplayMode.PERMISSIONS;

    public AppPermissionAdapter(Context context, List<ApplicationInfo> apps) {
        mApps = apps;
        mPm = context.getPackageManager();
        mActivity = (MainActivity) context;
    }

    public void setDisplayMode(DisplayMode mode) {
        mMode = mode;
        notifyDataSetChanged();
    }

    public void setGuardian(INovaGuardian guardian) {
        mGuardian = guardian;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_permission, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApplicationInfo app = mApps.get(position);
        holder.name.setText(app.loadLabel(mPm));
        holder.icon.setImageDrawable(app.loadIcon(mPm));
        holder.summary.setText(app.packageName);
        
        holder.itemView.setOnClickListener(v -> mActivity.openAppDetails(app.packageName));

        if (mMode == DisplayMode.NETWORK && mGuardian != null) {
            holder.networkToggle.setVisibility(View.VISIBLE);
            try {
                boolean allowed = mGuardian.isAppNetworkAccessAllowed(app.uid);
                holder.networkToggle.setOnCheckedChangeListener(null);
                holder.networkToggle.setChecked(allowed);
                holder.networkToggle.setOnCheckedChangeListener((v, isChecked) -> {
                    try {
                        mGuardian.setAppNetworkAccess(app.uid, isChecked);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            holder.networkToggle.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        TextView summary;
        Switch networkToggle;

        ViewHolder(View v) {
            super(v);
            icon = v.findViewById(R.id.app_icon);
            name = v.findViewById(R.id.app_name);
            summary = v.findViewById(R.id.permission_summary);
            networkToggle = v.findViewById(R.id.network_toggle);
        }
    }
}
