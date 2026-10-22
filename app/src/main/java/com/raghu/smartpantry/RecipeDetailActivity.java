package com.raghu.smartpantry;

import android.os.Bundle;
import android.widget.TextView;

/** Screen 4: full ingredient list and method for the recipe selected on the previous screen. */
public class RecipeDetailActivity extends BaseActivity {
    public static final String EXTRA_RECIPE_ID = "recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        enableBack();

        long id = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);
        Recipe recipe = new DatabaseHelper(this).getRecipe(id);
        if (recipe == null) { // defensive: should never happen
            finish();
            return;
        }
        setTitle(recipe.name);
        ((TextView) findViewById(R.id.tvRecipeTitle)).setText(recipe.name);

        StringBuilder ingredients = new StringBuilder();
        for (Ingredient ing : recipe.ingredients) {
            ingredients.append("\u2022 ").append(RecipeMatcher.fmt(ing.quantity)).append(' ')
                    .append(ing.unit).append(' ').append(ing.name).append('\n');
        }
        ((TextView) findViewById(R.id.tvIngredients)).setText(ingredients.toString().trim());

        StringBuilder method = new StringBuilder();
        String[] steps = recipe.steps.split("#");
        for (int i = 0; i < steps.length; i++) {
            method.append(i + 1).append(". ").append(steps[i].trim()).append("\n\n");
        }
        ((TextView) findViewById(R.id.tvSteps)).setText(method.toString().trim());
    }
}
