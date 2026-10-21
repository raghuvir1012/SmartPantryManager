package com.raghu.smartpantry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The core business logic of the app: the STRICT-MATCHING RULE.
 * A recipe is "suggested" only if EVERY ingredient is in the pantry in at least the required quantity.
 * Matching is robust to singular/plural names ("tomato" = "tomatoes") and unit differences (1 kg = 1000 g).
 */
public class RecipeMatcher {

    /** strict = recipes the user can cook now; almost = recipes missing exactly one ingredient (bonus list). */
    public static class Result {
        public final List<RecipeMatch> strict = new ArrayList<>();
        public final List<RecipeMatch> almost = new ArrayList<>();
    }

    /** Lower-cases and singularises a name so that "Tomatoes", "tomato " and "TOMATO" are identical. */
    public static String normaliseName(String raw) {
        String s = raw.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
        if (s.endsWith("ies") && s.length() > 4) return s.substring(0, s.length() - 3) + "y";
        if (s.endsWith("oes") || s.endsWith("ches") || s.endsWith("shes")
                || s.endsWith("sses") || s.endsWith("xes")) return s.substring(0, s.length() - 2);
        if (s.endsWith("ss")) return s;
        if (s.endsWith("s") && s.length() > 3) return s.substring(0, s.length() - 1);
        return s;
    }

    /** Units can only be compared with units of the same kind (weight, volume or count). */
    public static String dimension(String unit) {
        switch (unit) {
            case "g": case "kg": return "mass";
            case "ml": case "l": case "tsp": case "tbsp": case "cup": return "volume";
            default: return "count";
        }
    }

    /** Converts a quantity to its base unit: grams, millilitres or pieces. */
    public static double toBase(double quantity, String unit) {
        switch (unit) {
            case "kg": case "l": return quantity * 1000;
            case "tsp": return quantity * 5;
            case "tbsp": return quantity * 15;
            case "cup": return quantity * 240;
            default: return quantity;
        }
    }

    private static String key(String name, String unit) {
        return normaliseName(name) + "|" + dimension(unit);
    }

    /** Checks every recipe against the pantry. */
    public static Result match(List<Recipe> recipes, List<PantryItem> pantry) {
        // 1. Total up what the user owns (the same ingredient may be entered more than once).
        Map<String, Double> stock = new HashMap<>();
        for (PantryItem p : pantry) {
            String k = key(p.name, p.unit);
            Double current = stock.get(k);
            stock.put(k, (current == null ? 0 : current) + toBase(p.quantity, p.unit));
        }
        // 2. A recipe qualifies only when NOTHING is missing.
        Result result = new Result();
        for (Recipe recipe : recipes) {
            List<String> missing = new ArrayList<>();
            for (Ingredient ing : recipe.ingredients) {
                Double have = stock.get(key(ing.name, ing.unit));
                double owned = have == null ? 0 : have;
                if (owned + 1e-9 < toBase(ing.quantity, ing.unit)) {
                    missing.add(ing.name);
                }
            }
            if (missing.isEmpty()) {
                result.strict.add(new RecipeMatch(recipe, missing));
            } else if (missing.size() == 1) {
                result.almost.add(new RecipeMatch(recipe, missing));
            }
        }
        return result;
    }

    /** Formats 3.0 as "3" and 2.5 as "2.5". */
    public static String fmt(double q) {
        return q == Math.rint(q) ? String.valueOf((long) q) : String.valueOf(q);
    }
}
