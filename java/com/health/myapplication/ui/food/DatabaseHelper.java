package com.health.myapplication.ui.food;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "food_database";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "foods";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CALORIE = "calorie";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the foods table
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_CALORIE + " INTEGER);";

        db.execSQL(createTableQuery);


        addFood(new Food("Apple(1 unit)", 95), db);
        addFood(new Food("Banana(1 piece)", 52), db);
        addFood(new Food("Chicken Breast(100gm)", 165), db);
        addFood(new Food("Orange(1 piece)", 43), db);
        addFood(new Food("Grapes(100gm)", 69), db);
        addFood(new Food("Strawberries(50gm)", 32), db);
        addFood(new Food("Watermelon(1 slice)", 30), db);
        addFood(new Food("Mango(1 piece)", 60), db);
        addFood(new Food("Pineapple(1 unit)", 50), db);
        addFood(new Food("Kiwi(1 piece)", 61), db);
        addFood(new Food("Papaya(1 unit)", 43), db);
        addFood(new Food("Avacado(1 unit)", 160), db);
        addFood(new Food("Broccoli(100gm)", 55), db);
        addFood(new Food("Carrot(1 piece)", 41), db);
        addFood(new Food("Spinach(1 bunch)", 23), db);
        addFood(new Food("Potato(100gm)", 77), db);
        addFood(new Food("Tomato(50gm)", 18), db);
        addFood(new Food("Cabbage(1 unit)", 25), db);
        addFood(new Food("Onion(50gm)", 40), db);
        addFood(new Food("Cucumber(1 piece)", 16), db);
        addFood(new Food("Mushroom(100gm)", 22), db);
        addFood(new Food("Eggplant(1 unit)", 25), db);
        addFood(new Food("Radish(1 unit)", 16), db);
        addFood(new Food("Cauliflower(1 unit)", 25), db);
        addFood(new Food("Egg(1 piece)", 77), db);
        addFood(new Food("Chicken Breast(100gm)", 165), db);
        addFood(new Food("Milk(1 glass)", 55), db);
        addFood(new Food("Yogurt(100gm)", 59), db);
        addFood(new Food("Rice(100gm)", 130), db);
        addFood(new Food("White Bread(1 pack)", 247), db);
        addFood(new Food("Almonds(50gm)", 576), db);
        addFood(new Food("Potato Chips(250gm)", 536), db);
        addFood(new Food("Popcorn(1 bowl)", 31), db);
        addFood(new Food("Honey(1 tb spoon)", 304), db);
        addFood(new Food("Dark Chocolate(100gm)", 598), db);
        addFood(new Food("Butter(1tb spoon)", 717), db);
        addFood(new Food("Oats(100gm)", 68), db);
        addFood(new Food("Pasta(100gm)", 131), db);
        addFood(new Food("Beef(100gm)", 250), db);
        addFood(new Food("Pork(100gm)", 143), db);
        addFood(new Food("Chicken Thigh(100gm)", 177), db);
        addFood(new Food("Chickpeas(100gm)", 164), db);
        addFood(new Food("Black beans(100gm)", 132), db);
        addFood(new Food("Kidney beans(100gm)", 22), db);
        addFood(new Food("Peanuts(50gm)", 567), db);
        addFood(new Food("Soya beans(50gm)", 173), db);
        addFood(new Food("Walnuts(10gm)", 654), db);
        addFood(new Food("Sunflower Seeds(10gm)", 584), db);
        addFood(new Food("Chia Seeds(1tb spoon)", 489), db);
        addFood(new Food("Pumpkin Seeds(10gm)", 559), db);
        addFood(new Food("Flaxseeds(10gm)", 534), db);
        addFood(new Food("Black Coffee(1 cup)", 2), db);
        addFood(new Food("Orange Juice(1 glass)", 45), db);
        addFood(new Food("Coca-Cola(1 glass)", 42), db);
        addFood(new Food("Cheeseburger(1 piece)", 250), db);
        addFood(new Food("French Fries - ", 43), db);
        addFood(new Food("Pizza (Cheese)", 266), db);
        addFood(new Food("Chocolate Cake", 371), db);
        addFood(new Food("Chocolate Chip Cookies", 488), db);
        addFood(new Food("Apple Pie", 237), db);
        addFood(new Food("Brownies", 466), db);
        addFood(new Food("Peaches", 39), db);
        addFood(new Food("Guava", 68), db);
        addFood(new Food("Cranberries", 46), db);
        addFood(new Food("Blueberries", 57), db);
        addFood(new Food("Raspberries", 52), db);
        addFood(new Food("Blackberries", 40), db);
        addFood(new Food("Cherry", 50), db);
        addFood(new Food("Grapfruit", 42), db);
        addFood(new Food("Dragon Fruit", 60), db);
        addFood(new Food("Star Fruit", 31), db);
        addFood(new Food("Mulberries", 43), db);
        addFood(new Food("Lychee", 66), db);
        addFood(new Food("Pomegranate", 83), db);
        addFood(new Food("Jackfruit", 95), db);
        addFood(new Food("Cashew", 553), db);
        addFood(new Food("Hazelnut", 628), db);
        addFood(new Food("Pistachio", 562), db);
        addFood(new Food("Sunflower Seeds", 584), db);
        addFood(new Food("Brown Bread", 275), db);
        addFood(new Food("Water", 0), db);
        addFood(new Food("Coconut Water", 19), db);
        addFood(new Food("Apple Juice", 46), db);
        addFood(new Food("Grapefruit Juice", 39), db);
        addFood(new Food("Red Wine (100ml)", 83), db);
        addFood(new Food("Paneer", 300), db);
        addFood(new Food("Green Tea(1 cup)", 2), db);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {

            db.execSQL("ALTER TABLE foods ADD COLUMN calorie INTEGER");
        }
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }



    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Add downgrade logic here
    }

    public long addFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();
        long newRowId = addFood(food, db);
        db.close();
        return newRowId;
    }


    private long addFood(Food food, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, food.getName());
        values.put(COLUMN_CALORIE, food.getCalorie());
        return db.insert(TABLE_NAME, null, values);
    }

    // Get all foods from the database
    public List<Food> getAllFoods() {
        List<Food> foods = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_CALORIE
        };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Food food = new Food();
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                int calorieIndex = cursor.getColumnIndex(COLUMN_CALORIE);

                food.setId(cursor.getInt(idIndex));
                food.setName(cursor.getString(nameIndex));
                food.setCalorie(cursor.getInt(calorieIndex));
                foods.add(food);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return foods;
    }

    // Search for foods based on the input query
    public List<Food> searchFoods(String query) {
        List<Food> searchResults = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_CALORIE
        };

        String selection = COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};

        try {
            Cursor cursor = db.query(
                    TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Food food = new Food();
                    int idIndex = cursor.getColumnIndex(COLUMN_ID);
                    int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                    int calorieIndex = cursor.getColumnIndex(COLUMN_CALORIE);

                    food.setId(cursor.getInt(idIndex));
                    food.setName(cursor.getString(nameIndex));
                    food.setCalorie(cursor.getInt(calorieIndex));
                    searchResults.add(food);
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            // Handle the exception, recreate the table if needed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        } finally {
            db.close();
        }

        return searchResults;
    }

    public List<String> getAllFoodNames() {
        List<String> foodNames = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_NAME
        };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                String foodName = cursor.getString(nameIndex);
                foodNames.add(foodName);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return foodNames;
    }

}

