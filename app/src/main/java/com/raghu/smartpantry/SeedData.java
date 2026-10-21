package com.raghu.smartpantry;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/** Pre-loads 22 recipes the first time the database is created. Format: name|steps(#-separated)|ingredient:qty:unit;... */
public class SeedData {
    private static final String[] RECIPES = {
        "Scrambled Eggs|Beat the eggs with the milk.#Melt the butter in a pan.#Pour in the eggs and stir gently until just set.|egg:3:pcs;butter:10:g;milk:30:ml",
        "Cheese Omelette|Beat the eggs.#Melt the butter in a pan and add the eggs.#Add the grated cheese, fold in half and serve.|egg:3:pcs;cheese:50:g;butter:10:g",
        "Tomato Pasta|Boil the pasta until tender.#Fry the chopped onion in the oil.#Add the chopped tomatoes and simmer for 10 minutes.#Mix with the pasta.|pasta:200:g;tomato:4:pcs;onion:1:pcs;olive oil:15:ml",
        "Egg Fried Rice|Cook the rice and let it cool.#Fry the chopped onion.#Scramble the eggs in the pan.#Add the rice and soy sauce and stir-fry for 5 minutes.|rice:200:g;egg:2:pcs;onion:1:pcs;soy sauce:15:ml",
        "Pancakes|Whisk the flour, egg, sugar and milk into a smooth batter.#Pour a ladle into a hot pan.#Flip when bubbles appear and cook the other side.|flour:150:g;milk:250:ml;egg:1:pcs;sugar:20:g",
        "Cheese Toast|Butter the bread.#Cover with the cheese.#Grill until golden and bubbling.|bread:4:pcs;cheese:80:g;butter:10:g",
        "French Toast|Whisk the eggs, milk and sugar.#Soak each slice of bread.#Fry in a pan until golden on both sides.|bread:4:pcs;egg:2:pcs;milk:100:ml;sugar:10:g",
        "Mashed Potatoes|Peel and boil the potatoes until soft.#Drain and mash.#Stir in the butter and warm milk.|potato:4:pcs;butter:30:g;milk:60:ml",
        "Cheesy Baked Potatoes|Bake the potatoes for 60 minutes at 200 C.#Cut open and fluff the inside.#Add butter and cheese and return to the oven for 5 minutes.|potato:2:pcs;cheese:60:g;butter:15:g",
        "Tuna Pasta|Boil the pasta.#Fry the chopped onion in the oil.#Stir in the tuna.#Toss with the pasta.|pasta:200:g;tuna:1:pcs;onion:1:pcs;olive oil:15:ml",
        "Chicken Rice Bowl|Cook the rice.#Fry the onion in the oil.#Add the sliced chicken and cook until done.#Serve over the rice.|chicken breast:300:g;rice:200:g;onion:1:pcs;olive oil:15:ml",
        "Chicken Stir-Fry|Slice the chicken, carrots and onion.#Stir-fry the chicken until cooked.#Add the vegetables and soy sauce and cook for 5 minutes.|chicken breast:300:g;carrot:2:pcs;onion:1:pcs;soy sauce:30:ml",
        "Vegetable Soup|Chop the carrots, potatoes and onion.#Boil in 1 litre of water with the stock cube for 25 minutes.#Blend or serve chunky.|carrot:2:pcs;potato:2:pcs;onion:1:pcs;stock cube:1:pcs",
        "Tomato Soup|Fry the chopped onion in the butter.#Add the tomatoes and stock cube with 500 ml water.#Simmer for 20 minutes and blend.|tomato:6:pcs;onion:1:pcs;butter:20:g;stock cube:1:pcs",
        "Banana Pancakes|Mash the bananas.#Mix with the eggs and flour.#Fry spoonfuls in a pan until golden.|banana:2:pcs;egg:2:pcs;flour:50:g",
        "Oats Porridge|Combine oats, milk and sugar in a pot.#Stir over medium heat for 5 minutes until creamy.|oats:80:g;milk:300:ml;sugar:10:g",
        "Banana Oat Smoothie|Add the banana, oats and milk to a blender.#Blend until smooth.|banana:1:pcs;oats:30:g;milk:250:ml",
        "Lentil Curry|Fry the chopped onion.#Add the curry powder and chopped tomatoes.#Add the lentils and 500 ml water and simmer for 25 minutes.|lentil:200:g;onion:1:pcs;tomato:2:pcs;curry powder:2:tsp",
        "Bean Burritos|Warm the beans with the chopped onion.#Spoon onto the tortillas and add cheese.#Roll up and toast in a dry pan.|tortilla:2:pcs;bean:200:g;cheese:50:g;onion:1:pcs",
        "Egg Sandwich|Boil the eggs for 9 minutes and chop them.#Butter the bread.#Fill the sandwich with the egg.|bread:2:pcs;egg:2:pcs;butter:10:g",
        "Garlic Bread|Mash the garlic into the butter.#Spread on the bread.#Bake for 10 minutes at 200 C.|bread:4:pcs;garlic:2:pcs;butter:40:g",
        "Spaghetti Aglio e Olio|Boil the pasta.#Gently fry sliced garlic in the oil.#Toss the pasta in the garlic oil.|pasta:200:g;garlic:4:pcs;olive oil:45:ml"
    };

    /** Inserts every recipe and its ingredients using the supplied (open) database. */
    public static void seed(SQLiteDatabase db) {
        for (String line : RECIPES) {
            String[] parts = line.split("\\|");
            ContentValues rv = new ContentValues();
            rv.put("name", parts[0]);
            rv.put("steps", parts[1]);
            long recipeId = db.insert("recipes", null, rv);
            for (String ingText : parts[2].split(";")) {
                String[] f = ingText.split(":");
                ContentValues iv = new ContentValues();
                iv.put("recipe_id", recipeId);
                iv.put("name", f[0]);
                iv.put("quantity", Double.parseDouble(f[1]));
                iv.put("unit", f[2]);
                db.insert("recipe_ingredients", null, iv);
            }
        }
    }
}
