package com.abhi41.foodrecipe.data.UseCase

import com.abhi41.foodrecipe.data.database.LocalDataSource
import com.abhi41.foodrecipe.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

class GetAllRecipesUseCase(
    private val recipeRepository: LocalDataSource
) {
    operator fun invoke(): Flow<List<RecipesEntity>>{
        return recipeRepository.readRecipes()
    }
}
