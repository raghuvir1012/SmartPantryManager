package com.raghu.smartpantry;

/** One ingredient the user currently owns (a row in the "pantry" table). */
public class PantryItem {
    public long id;
    public String name;
    public double quantity;
    public String unit;
    public String expiry; // format yyyy-MM-dd, or "" when no expiry date was given

    public PantryItem(long id, String name, double quantity, String unit, String expiry) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiry = expiry == null ? "" : expiry;
    }
}
