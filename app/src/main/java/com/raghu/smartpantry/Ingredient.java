package com.raghu.smartpantry;

/** One ingredient line that a recipe requires (a row in "recipe_ingredients"). */
public class Ingredient {
    public String name;
    public double quantity;
    public String unit;

    public Ingredient(String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }
}
