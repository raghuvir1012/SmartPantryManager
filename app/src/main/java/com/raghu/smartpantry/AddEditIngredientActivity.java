package com.raghu.smartpantry;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

/** Screen 2: add a new pantry item, or edit an existing one (when an item id is passed in the Intent). */
public class AddEditIngredientActivity extends BaseActivity {
    public static final String EXTRA_ITEM_ID = "item_id";
    private static final String[] UNITS = {"g", "kg", "ml", "l", "pcs", "tsp", "tbsp", "cup"};

    private EditText etName, etQuantity, etExpiry;
    private Spinner spUnit;
    private DatabaseHelper db;
    private long itemId = -1; // -1 means "adding a new item"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        enableBack();

        db = new DatabaseHelper(this);
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiry = findViewById(R.id.etExpiry);
        spUnit = findViewById(R.id.spUnit);
        spUnit.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, UNITS));

        itemId = getIntent().getLongExtra(EXTRA_ITEM_ID, -1);
        if (itemId != -1) {
            setTitle(R.string.title_edit);
            PantryItem p = db.getPantryItem(itemId);
            if (p != null) {
                etName.setText(p.name);
                etQuantity.setText(RecipeMatcher.fmt(p.quantity));
                etExpiry.setText(p.expiry);
                spUnit.setSelection(Math.max(0, Arrays.asList(UNITS).indexOf(p.unit)));
            }
        } else {
            setTitle(R.string.title_add);
        }

        etExpiry.setOnClickListener(v -> showDatePicker());
        findViewById(R.id.btnClearDate).setOnClickListener(v -> etExpiry.setText(""));
        findViewById(R.id.btnSave).setOnClickListener(v -> save());
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, day) ->
                        etExpiry.setText(String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day)),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    /** Validates the form; only saves to the database when every field is valid. */
    private void save() {
        String name = etName.getText().toString().trim();
        String qtyText = etQuantity.getText().toString().trim();
        boolean valid = true;

        if (name.isEmpty()) {
            etName.setError("Ingredient name is required");
            valid = false;
        } else if (!name.matches("[\\p{L}][\\p{L} '\\-]{0,39}")) {
            etName.setError("Use letters only (max 40 characters)");
            valid = false;
        }

        double qty = -1;
        if (qtyText.isEmpty()) {
            etQuantity.setError("Quantity is required");
            valid = false;
        } else {
            try {
                qty = Double.parseDouble(qtyText);
            } catch (NumberFormatException e) {
                qty = -1;
            }
            if (qty <= 0 || qty > 100000) {
                etQuantity.setError("Enter a number greater than 0");
                valid = false;
            }
        }
        if (!valid) return;

        String unit = (String) spUnit.getSelectedItem();
        String expiry = etExpiry.getText().toString().trim(); // optional
        PantryItem item = new PantryItem(itemId, name, qty, unit, expiry);

        if (itemId == -1) {
            db.insertPantry(item);   // CREATE
            Toast.makeText(this, "Ingredient added", Toast.LENGTH_SHORT).show();
        } else {
            db.updatePantry(item);   // UPDATE
            Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
