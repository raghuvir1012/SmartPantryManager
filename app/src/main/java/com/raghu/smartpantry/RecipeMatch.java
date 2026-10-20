package com.raghu.smartpantry;

import java.util.List;

/** The outcome of checking one recipe against the pantry. */
public class RecipeMatch {
    public Recipe recipe;
    public List<String> missing; // names of ingredients the user lacks (empty = strict match)

    public RecipeMatch(Recipe recipe, List<String> missing) {
        this.recipe = recipe;
        this.missing = missing;
    }
}
