package com.rm.myrecipes.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rm.myrecipes.data.room.entity.RecipeEntity
import com.rm.myrecipes.data.room.entity.RecipeResultEntity
import com.rm.myrecipes.domain.data.ExtendedIngredient
import com.rm.myrecipes.domain.data.Recipe

class RecipesTypeConverter {

    private var gson = Gson()

    @TypeConverter
    fun recipesToJson(recipes: List<Recipe>): String = gson.toJson(recipes)

    @TypeConverter
    fun jsonToRecipes(jsonString: String): List<Recipe> {
        val recipesList = object : TypeToken<List<Recipe>>() {}.type
        return gson.fromJson(jsonString, recipesList)
    }

    @TypeConverter
    fun extendedIngredientToJson(extendedIngredients: List<ExtendedIngredient>): String = gson.toJson(extendedIngredients)

    @TypeConverter
    fun jsonToExtendedIngredient(jsonString: String): List<ExtendedIngredient> {
        val ingredientList = object : TypeToken<List<ExtendedIngredient>>() {}.type
        return gson.fromJson(jsonString, ingredientList)
    }
}

@Database(entities = [RecipeResultEntity::class, RecipeEntity::class], version = 1, exportSchema = false)
@TypeConverters(RecipesTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipesDao() : RecipesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(AppDatabase::class) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
