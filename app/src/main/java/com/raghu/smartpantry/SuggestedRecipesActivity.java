package com.raghu.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/** Screen 3: runs the strict-matching logic and lists only the recipes the user can cook right now. */
public class SuggestedRecipesActivity extends BaseActivity {
    private DatabaseHelper db;
    private RecipeAdapter strictAdapter, almostAdapter;
    private TextView tvEmpty, tvAlmostHeader;
    private RecyclerView rvAlmost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested);
        setTitle(R.string.title_suggested);
        enableBack();

        db = new DatabaseHelper(this);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvAlmostHeader = findViewById(R.id.tvAlmostHeader);
        rvAlmost = findViewById(R.id.rvAlmost);

        RecyclerView rvStrict = findViewById(R.id.rvStrict);
        rvStrict.setLayoutManager(new LinearLayoutManager(this));
        rvAlmost.setLayoutManager(new LinearLayoutManager(this));

        strictAdapter = new RecipeAdapter(false, match -> openDetail(match.recipe.id));
        almostAdapter = new RecipeAdapter(true, match -> openDetail(match.recipe.id));
        rvStrict.setAdapter(strictAdapter);
        rvAlmost.setAdapter(almostAdapter);
    }

    /** Re-run the matching every time the screen is shown, so it always reflects the current pantry. */
    @Override
    protected void onResume() {
        super.onResume();
        RecipeMatcher.Result result = RecipeMatcher.match(db.getAllRecipes(), db.getAllPantry());

        strictAdapter.setItems(result.strict);
        tvEmpty.setVisibility(result.strict.isEmpty() ? View.VISIBLE : View.GONE);

        boolean showAlmost = getSharedPreferences(SettingsActivity.PREFS, MODE_PRIVATE)
                .getBoolean(SettingsActivity.KEY_ALMOST, false) && !result.almost.isEmpty();
        almostAdapter.setItems(result.almost);
        tvAlmostHeader.setVisibility(showAlmost ? View.VISIBLE : View.GONE);
        rvAlmost.setVisibility(showAlmost ? View.VISIBLE : View.GONE);
    }

    private void openDetail(long recipeId) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipeId);
        startActivity(intent);
    }
}
