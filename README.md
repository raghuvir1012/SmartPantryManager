# Smart Pantry Manager

A Java Android application (Mobile App Development 700 - Richfield) that reduces food waste by tracking the
ingredients you already have and suggesting recipes you can cook **strictly** from them - no shopping trip needed.

## Features
- Pantry management: add, edit and delete ingredients (name, quantity, unit, optional expiry date) with input validation
- Pantry list screen (RecyclerView + custom adapter bound to SQLite), expiring-soon highlighting
- 22 pre-loaded recipes (seeded on first run)
- **Suggested Recipes** screen using the strict-matching rule: a recipe is shown only if EVERY ingredient is in the pantry in at least the required quantity
- Robust matching: singular/plural names ("tomato" = "tomatoes") and unit conversion (kg/g, l/ml, tsp/tbsp/cup)
- Optional "Almost There" list (recipes missing exactly one ingredient), clearly separated and switched on in Settings
- Recipe detail screen, Settings screen, friendly message when no recipes match
- No Google Maps, mapping SDK or location/GPS features

## Database choice: SQLite (SQLiteOpenHelper)
SQLite runs locally on the device, needs no account, internet or server, and data persists after the app is closed.
This suits a personal pantry app: it is fast, private and works offline. Tables: `pantry`, `recipes`, `recipe_ingredients`.

## Setup / run instructions
1. Install Android Studio (Hedgehog or newer) and open the `SmartPantryManager` folder (File > Open).
2. Wait for "Gradle sync" to finish (accept any prompt to install the Android SDK 34).
3. Create an emulator in Device Manager (e.g. Pixel 6, API 34) or connect a phone with USB debugging.
4. Press the green **Run** button. The app opens on the My Pantry screen.

## Structure
`app/src/main/java/com/raghu/smartpantry/` - Activities, adapters, `DatabaseHelper`, `RecipeMatcher` (core logic), `SeedData`.
`app/src/main/res/` - layouts, menu, strings, theme.
