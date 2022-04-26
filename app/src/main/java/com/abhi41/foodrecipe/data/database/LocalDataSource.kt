package com.abhi41.foodrecipe.data.database

import com.abhi41.foodrecipe.data.database.entities.FavoriteEntity
import com.abhi41.foodrecipe.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {
    //inserting recipes
    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }

    //read recipes
    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }

    //read favorite recipes
    fun readFavoriteRecipes(): Flow<List<FavoriteEntity>> {
        return recipesDao.readFavoriteRecipes()
    }

    //insert favorite recipes
    suspend fun insertFavoriteRecipes(favoriteEntity: FavoriteEntity) {
        recipesDao.insertFavoriteRecipe(favoriteEntity)
    }

    //delete favorite recipes
    suspend fun deleteFavoriteRecipe(favoriteEntity: FavoriteEntity) {
        recipesDao.deleteFavoriteRecipe(favoriteEntity)
    }

    //delete all favorite recipes
    suspend fun deleteAllFavoriteRecipes() {
        recipesDao.deleteAllFavorites()
    }
}