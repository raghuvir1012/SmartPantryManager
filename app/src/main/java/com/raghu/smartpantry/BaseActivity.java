package com.raghu.smartpantry;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

/** Shared toolbar menu (Pantry / Suggested Recipes / Settings) used by every screen. */
public abstract class BaseActivity extends AppCompatActivity {

    /** Shows the "up" arrow on screens that are not top-level. */
    protected void enableBack() {
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        Class<?> target = null;
        if (id == R.id.action_pantry) target = PantryListActivity.class;
        else if (id == R.id.action_suggested) target = SuggestedRecipesActivity.class;
        else if (id == R.id.action_settings) target = SettingsActivity.class;
        if (target != null) {
            if (!target.isInstance(this)) { // explicit Intent to navigate between screens
                Intent intent = new Intent(this, target);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
