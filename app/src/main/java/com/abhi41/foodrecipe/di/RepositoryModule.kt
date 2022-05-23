package com.abhi41.foodrecipe.di

import com.abhi41.foodrecipe.data.UseCase.GetAllRecipesUseCase
import com.abhi41.foodrecipe.data.UseCase.UseCases
import com.abhi41.foodrecipe.data.database.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

   @Provides
   @Singleton
   fun provideUseCases(localDataSource: LocalDataSource): UseCases{
       return UseCases(
           getAllRecipesUse = GetAllRecipesUseCase(localDataSource)
       )
   }

}