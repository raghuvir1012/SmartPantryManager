package com.raghu.smartpantry;

import java.util.ArrayList;
import java.util.List;

/** A recipe with its required ingredients and preparation steps. */
public class Recipe {
    public long id;
    public String name;
    public String steps; // steps separated by '#'
    public List<Ingredient> ingredients = new ArrayList<>();

    public Recipe(long id, String name, String steps) {
        this.id = id;
        this.name = name;
        this.steps = steps;
    }
}
