package com.raghu.smartpantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/** Custom RecyclerView adapter that shows the pantry items stored in SQLite. */
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.VH> {

    public interface Listener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    private final List<PantryItem> items = new ArrayList<>();
    private final Listener listener;
    private boolean showExpiryAlerts = true;

    public PantryAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<PantryItem> newItems, boolean showExpiryAlerts) {
        items.clear();
        items.addAll(newItems);
        this.showExpiryAlerts = showExpiryAlerts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        PantryItem item = items.get(position);
        holder.tvName.setText(item.name);
        String detail = RecipeMatcher.fmt(item.quantity) + " " + item.unit;
        if (!item.expiry.isEmpty()) detail += "   |   Expires: " + item.expiry;
        holder.tvDetail.setText(detail);

        String warning = showExpiryAlerts ? expiryWarning(item.expiry) : null;
        holder.tvWarning.setVisibility(warning == null ? View.GONE : View.VISIBLE);
        holder.tvWarning.setText(warning);

        holder.itemView.setOnClickListener(v -> listener.onEdit(item));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /** Returns a warning text if the date is today, in the past or within 3 days; otherwise null. */
    private String expiryWarning(String expiry) {
        if (expiry.isEmpty()) return null;
        try {
            long expMillis = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(expiry).getTime();
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            long days = Math.round((expMillis - today.getTimeInMillis()) / 86400000.0);
            if (days < 0) return "Expired!";
            if (days == 0) return "Expires today!";
            if (days <= 3) return "Expires in " + days + (days == 1 ? " day" : " days");
        } catch (ParseException e) {
            return null;
        }
        return null;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvDetail, tvWarning;
        Button btnDelete;

        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDetail = itemView.findViewById(R.id.tvDetail);
            tvWarning = itemView.findViewById(R.id.tvWarning);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
