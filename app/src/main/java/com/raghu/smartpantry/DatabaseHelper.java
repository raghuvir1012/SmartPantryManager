package com.raghu.smartpantry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/** SQLite storage: full CRUD on the pantry plus read access to the seeded recipes. */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, "
                + "quantity REAL NOT NULL, unit TEXT NOT NULL, expiry TEXT)");
        db.execSQL("CREATE TABLE recipes (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, "
                + "steps TEXT NOT NULL)");
        db.execSQL("CREATE TABLE recipe_ingredients (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "recipe_id INTEGER NOT NULL, name TEXT NOT NULL, quantity REAL NOT NULL, "
                + "unit TEXT NOT NULL, FOREIGN KEY(recipe_id) REFERENCES recipes(id))");
        SeedData.seed(db); // pre-load recipes on first run
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS pantry");
        onCreate(db);
    }

    // ---------------------------------------------------------------- CREATE
    public long insertPantry(PantryItem item) {
        return getWritableDatabase().insert("pantry", null, toValues(item));
    }

    // ---------------------------------------------------------------- READ
    public List<PantryItem> getAllPantry() {
        List<PantryItem> list = new ArrayList<>();
        try (Cursor c = getReadableDatabase().query("pantry", null, null, null, null, null,
                "name COLLATE NOCASE ASC")) {
            while (c.moveToNext()) list.add(pantryFromCursor(c));
        }
        return list;
    }

    public PantryItem getPantryItem(long id) {
        try (Cursor c = getReadableDatabase().query("pantry", null, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null)) {
            return c.moveToFirst() ? pantryFromCursor(c) : null;
        }
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query("recipes", null, null, null, null, null, "name ASC")) {
            while (c.moveToNext()) list.add(recipeFromCursor(db, c));
        }
        return list;
    }

    public Recipe getRecipe(long id) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query("recipes", null, "id = ?", new String[]{String.valueOf(id)},
                null, null, null)) {
            return c.moveToFirst() ? recipeFromCursor(db, c) : null;
        }
    }

    // ---------------------------------------------------------------- UPDATE
    public int updatePantry(PantryItem item) {
        return getWritableDatabase().update("pantry", toValues(item), "id = ?",
                new String[]{String.valueOf(item.id)});
    }

    // ---------------------------------------------------------------- DELETE
    public int deletePantry(long id) {
        return getWritableDatabase().delete("pantry", "id = ?", new String[]{String.valueOf(id)});
    }

    // ---------------------------------------------------------------- helpers
    private ContentValues toValues(PantryItem item) {
        ContentValues v = new ContentValues();
        v.put("name", item.name);
        v.put("quantity", item.quantity);
        v.put("unit", item.unit);
        v.put("expiry", item.expiry);
        return v;
    }

    private PantryItem pantryFromCursor(Cursor c) {
        return new PantryItem(
                c.getLong(c.getColumnIndexOrThrow("id")),
                c.getString(c.getColumnIndexOrThrow("name")),
                c.getDouble(c.getColumnIndexOrThrow("quantity")),
                c.getString(c.getColumnIndexOrThrow("unit")),
                c.getString(c.getColumnIndexOrThrow("expiry")));
    }

    private Recipe recipeFromCursor(SQLiteDatabase db, Cursor c) {
        Recipe r = new Recipe(c.getLong(c.getColumnIndexOrThrow("id")),
                c.getString(c.getColumnIndexOrThrow("name")),
                c.getString(c.getColumnIndexOrThrow("steps")));
        try (Cursor ic = db.query("recipe_ingredients", null, "recipe_id = ?",
                new String[]{String.valueOf(r.id)}, null, null, "id ASC")) {
            while (ic.moveToNext()) {
                r.ingredients.add(new Ingredient(
                        ic.getString(ic.getColumnIndexOrThrow("name")),
                        ic.getDouble(ic.getColumnIndexOrThrow("quantity")),
                        ic.getString(ic.getColumnIndexOrThrow("unit"))));
            }
        }
        return r;
    }
}
