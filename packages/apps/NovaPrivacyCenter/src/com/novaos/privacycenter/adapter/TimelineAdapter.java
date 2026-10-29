package com.novaos.privacycenter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.novaos.privacycenter.R;
import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private List<TimelineItem> mItems;

    public static class TimelineItem {
        public String appName;
        public String action;
        public String time;
        public TimelineItem(String appName, String action, String time) {
            this.appName = appName;
            this.action = action;
            this.time = time;
        }
    }

    public TimelineAdapter(List<TimelineItem> items) {
        mItems = items;
    }

    public void updateItems(List<TimelineItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimelineItem item = mItems.get(position);
        holder.appName.setText(item.appName);
        holder.action.setText(item.action);
        holder.time.setText(item.time);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView appName;
        TextView action;
        TextView time;
        ViewHolder(View v) {
            super(v);
            appName = v.findViewById(R.id.timeline_app_name);
            action = v.findViewById(R.id.timeline_action);
            time = v.findViewById(R.id.timeline_time);
        }
    }
}
