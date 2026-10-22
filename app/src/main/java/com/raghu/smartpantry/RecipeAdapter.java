package com.raghu.smartpantry;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/** Custom adapter for the recipe lists (strict suggestions and the optional "Almost There" list). */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.VH> {

    public interface Listener {
        void onRecipeClick(RecipeMatch match);
    }

    private final List<RecipeMatch> items = new ArrayList<>();
    private final boolean showMissing;
    private final Listener listener;

    public RecipeAdapter(boolean showMissing, Listener listener) {
        this.showMissing = showMissing;
        this.listener = listener;
    }

    public void setItems(List<RecipeMatch> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        RecipeMatch match = items.get(position);
        holder.tvName.setText(match.recipe.name);
        if (showMissing) {
            holder.tvSub.setText("Missing: " + TextUtils.join(", ", match.missing));
        } else {
            holder.tvSub.setText(match.recipe.ingredients.size() + " ingredients - you have everything!");
        }
        holder.itemView.setOnClickListener(v -> listener.onRecipeClick(match));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvSub;

        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvRecipeName);
            tvSub = itemView.findViewById(R.id.tvRecipeSub);
        }
    }
}
