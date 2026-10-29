package com.novaos.devicecare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.novaos.devicecare.R;
import java.util.List;

public class PerformanceProfileAdapter extends RecyclerView.Adapter<PerformanceProfileAdapter.ViewHolder> {

    private final List<Profile> mProfiles;
    private final OnProfileSelectedListener mListener;

    public interface OnProfileSelectedListener {
        void onProfileSelected(Profile profile);
    }

    public PerformanceProfileAdapter(List<Profile> profiles, OnProfileSelectedListener listener) {
        mProfiles = profiles;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_performance_profile, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Profile profile = mProfiles.get(position);
        holder.name.setText(profile.name);
        holder.description.setText(profile.description);
        holder.itemView.setOnClickListener(v -> mListener.onProfileSelected(profile));
    }

    @Override
    public int getItemCount() {
        return mProfiles.size();
    }

    public static class Profile {
        public String name;
        public String description;
        public int id;

        public Profile(int id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView description;

        public ViewHolder(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.profile_name);
            description = v.findViewById(R.id.profile_description);
        }
    }
}