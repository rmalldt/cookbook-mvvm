package com.rm.myrecipes.data.room

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.rm.myrecipes.data.network.dto.ExtendedIngredientDefinition
import com.rm.myrecipes.data.network.dto.RecipesResponse
import com.rm.myrecipes.domain.data.Recipe
import com.rm.myrecipes.domain.data.Recipes

@Entity(tableName = "recipes")
data class RecipesEntity(
    @PrimaryKey(autoGenerate = false) var id: Int = 0,
    val recipes: List<Recipe>
) {
    companion object {
        fun Recipes.toRecipesEntity(): RecipesEntity = RecipesEntity(recipes = recipes)

        fun RecipesEntity.toRecipes(): Recipes = Recipes(recipes)
    }
}

class RecipesTypeConverter {

    private var gson = Gson()

    @TypeConverter
    fun recipesToJson(recipes: List<Recipe>): String = gson.toJson(recipes)

    @TypeConverter
    fun jsonToRecipes(jsonString: String): List<Recipe> {
        val recipesType = object : TypeToken<List<Recipe>>() {}.type
        return gson.fromJson(jsonString, recipesType)
    }
}

@Database(entities = [RecipesEntity::class], version = 1, exportSchema = false)
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







