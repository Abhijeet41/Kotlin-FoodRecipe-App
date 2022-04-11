package com.abhi41.foodrecipe.data.database

import com.abhi41.foodrecipe.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
){
   //inserting recipes
   suspend fun insertRecipes(recipesEntity: RecipesEntity){
       recipesDao.insertRecipes(recipesEntity)
   }
    //read recipes
    fun readDatabase():Flow<List<RecipesEntity>>{
       return recipesDao.readRecipes()
    }
}