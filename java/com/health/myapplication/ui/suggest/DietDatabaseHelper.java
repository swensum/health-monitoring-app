package com.health.myapplication.ui.suggest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.health.myapplication.R;

public class DietDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "food_database";
    private static final int DATABASE_VERSION = 3;


    public static final String TABLE_FOODS = "foods";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE_RESOURCE_ID = "image_resource_id";
    public static final String COLUMN_WEIGHT_CATEGORY = "weight_category";

    private static final String CREATE_TABLE_FOODS = "CREATE TABLE " + TABLE_FOODS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_IMAGE_RESOURCE_ID + " INTEGER,"
            + COLUMN_WEIGHT_CATEGORY + " INTEGER);";


    public DietDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FOODS);

        insertSampleData(db);
        Log.d("DietDatabaseHelper", "Database created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);
        onCreate(db);
    }

    public enum WeightCategory {
        UNDERWEIGHT,
        NORMAL,
        OVERWEIGHT,
        OBESE
    }

    private void insertSampleData(SQLiteDatabase db) {
        insertFood(db, "Green Tea", "Green tea is a calorie-free beverage that can aid weight loss by boosting metabolism and promoting fat oxidation, making it a popular choice for those seeking to shed extra pounds.", R.drawable.greentea, WeightCategory.OVERWEIGHT);
        insertFood(db, "Chia Seeds", "Chia seeds are rich in fiber and protein, aiding in weight loss by promoting feelings of fullness and reducing calorie intake. It contains 137 calories per ounce (28 grams).", R.drawable.chiaseed, WeightCategory.OVERWEIGHT);
        insertFood(db, "Broccoli", "Broccoli is a nutrient-packed vegetable that aids in weight loss due to its low-calorie content and high fiber, which promotes feelings of fullness and satiety, making it a perfect addition to a calorie-controlled diet", R.drawable.broccoli, WeightCategory.OVERWEIGHT);
        insertFood(db, "Apple", "Apples are low in calories, high in fiber, and water content, making them an ideal snack for weight loss. It has 95 calories per medium-sized apple ", R.drawable.apple, WeightCategory.OVERWEIGHT);
        insertFood(db, "Egg", "Eggs are a nutrient-dense food rich in protein, healthy fats, and essential vitamins and minerals, making them an excellent choice for supporting weight gain. It contains 70 calories per large egg ", R.drawable.egg, WeightCategory.UNDERWEIGHT);
        insertFood(db, "Oats", "Oats are a nutrient-dense whole grain, providing a blend of carbohydrates, protein, and fiber, aiding in healthy weight gain. It contains 150 calories per half-cup serving", R.drawable.oats, WeightCategory.UNDERWEIGHT);
        insertFood(db, "Quinoa", "Rich in protein and fiber, good for weight management", R.drawable.quinoa, WeightCategory.OVERWEIGHT);
        insertFood(db, "Salmon", "High in omega-3 fatty acids, supports metabolism", R.drawable.salmon, WeightCategory.NORMAL);
        insertFood(db, "Potato", "Potatoes are rich in carbohydrates, providing a dense source of calories ideal for weight gain strategies. With approximately 163 calories per medium-sized potato.", R.drawable.potato, WeightCategory.UNDERWEIGHT);
        insertFood(db, "Dried Fruits", "Dried fruits are dense in calories, making them an efficient snack for weight gain.", R.drawable.driedfruits, WeightCategory.UNDERWEIGHT);
        insertFood(db, "Milk", "Milk is a rich source of protein, calcium, and essential vitamins, providing a nutrient-dense option for those seeking weight gain. It contains 150 calories per cup.", R.drawable.milk, WeightCategory.UNDERWEIGHT);
        insertFood(db, "Meat", "Meat, such as beef or chicken, is rich in protein and healthy fats, providing a calorie-dense option for weight gain. A single serving of meat can contain several hundred calories.", R.drawable.meat, WeightCategory.UNDERWEIGHT);
    }

    private void insertFood(SQLiteDatabase db, String name, String description, int imageResourceId, WeightCategory weightCategory) {
        db.execSQL("INSERT INTO " + TABLE_FOODS + " (" + COLUMN_NAME + ", " + COLUMN_DESCRIPTION + ", " + COLUMN_IMAGE_RESOURCE_ID + ", " + COLUMN_WEIGHT_CATEGORY + ") VALUES (?, ?, ?, ?)",
                new Object[]{name, description, imageResourceId, weightCategory.name()});
    }
}
