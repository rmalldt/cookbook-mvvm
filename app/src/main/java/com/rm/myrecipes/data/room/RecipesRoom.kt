package com.rm.myrecipes.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rm.myrecipes.domain.data.Recipes

@Entity(tableName = "recipes")
data class RecipesEntity(
    var recipes: Recipes
) {
    @PrimaryKey(autoGenerate = false) var id: Int = 0
}

class RecipesTypeConverter {

    private var gson = Gson()

    @TypeConverter
    fun recipesToJson(recipes: Recipes): String = gson.toJson(recipes)

    @TypeConverter
    fun jsonToRecipes(jsonString: String): Recipes {
        val recipesType = object : TypeToken<Recipes>() {}.type
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







