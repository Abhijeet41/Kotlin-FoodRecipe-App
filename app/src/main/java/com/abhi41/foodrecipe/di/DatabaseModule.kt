package com.abhi41.foodrecipe.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.abhi41.foodrecipe.data.database.RecipesDatabase
import com.abhi41.foodrecipe.utils.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context  // dagger provide us context to use this for creation of builder
    ) = Room.databaseBuilder(
        context,
        RecipesDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideDao(database: RecipesDatabase) = database.recipeDao()

}