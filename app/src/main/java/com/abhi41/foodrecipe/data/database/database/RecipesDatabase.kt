package com.abhi41.foodrecipe.data.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.abhi41.foodrecipe.data.database.dao.RecipesDao
import com.abhi41.foodrecipe.data.database.typeConverters.RecipesTypeConverter
import com.abhi41.foodrecipe.data.database.entities.FavoriteEntity
import com.abhi41.foodrecipe.data.database.entities.FoodJokeEntity
import com.abhi41.foodrecipe.data.database.entities.RecipesEntity

@Database(
    entities = [RecipesEntity::class, FavoriteEntity::class, FoodJokeEntity::class],
    version = 3, //need to upgrade version because we added favorite entity and food joke entity
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase(){
    abstract fun recipeDao(): RecipesDao
}