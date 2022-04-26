package com.abhi41.foodrecipe.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.abhi41.foodrecipe.data.database.entities.FavoriteEntity
import com.abhi41.foodrecipe.data.database.entities.RecipesEntity

@Database(
    entities = [RecipesEntity::class, FavoriteEntity::class],
    version = 2, //need to upgrade version because we added favorite entity
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase(){
    abstract fun recipeDao(): RecipesDao
}