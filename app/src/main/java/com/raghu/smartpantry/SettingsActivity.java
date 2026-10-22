package com.raghu.smartpantry;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;

/** Screen 5: two preferences stored in SharedPreferences. */
public class SettingsActivity extends BaseActivity {
    public static final String PREFS = "smart_pantry_prefs";
    public static final String KEY_EXPIRY = "expiry_alerts";
    public static final String KEY_ALMOST = "show_almost";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.title_settings);
        enableBack();

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        SwitchCompat swExpiry = findViewById(R.id.swExpiry);
        SwitchCompat swAlmost = findViewById(R.id.swAlmost);
        swExpiry.setChecked(prefs.getBoolean(KEY_EXPIRY, true));
        swAlmost.setChecked(prefs.getBoolean(KEY_ALMOST, false));

        swExpiry.setOnCheckedChangeListener((button, checked) ->
                prefs.edit().putBoolean(KEY_EXPIRY, checked).apply());
        swAlmost.setOnCheckedChangeListener((button, checked) ->
                prefs.edit().putBoolean(KEY_ALMOST, checked).apply());
    }
}
