package com.abhi41.foodrecipe.di

import android.content.Context
import androidx.room.Room
import com.abhi41.foodrecipe.data.UseCase.GetAllRecipesUseCase
import com.abhi41.foodrecipe.data.UseCase.UseCases
import com.abhi41.foodrecipe.data.database.LocalDataSource
import com.abhi41.foodrecipe.data.database.database.RecipesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(components = [SingletonComponent::class], replaces = [DatabaseModule::class])
@Module
class TestDatabaseModule {

    @Provides
    @Singleton
    fun provideTestDB(@ApplicationContext context: Context): RecipesDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            RecipesDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideUseCases(localDataSource: LocalDataSource): UseCases {
        return UseCases(
            getAllRecipesUse = GetAllRecipesUseCase(localDataSource)
        )
    }

}