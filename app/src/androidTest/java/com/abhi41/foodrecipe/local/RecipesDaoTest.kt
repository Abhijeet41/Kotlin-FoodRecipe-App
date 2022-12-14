package com.abhi41.foodrecipe.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.abhi41.foodrecipe.data.database.dao.RecipesDao
import com.abhi41.foodrecipe.data.database.database.RecipesDatabase
import com.abhi41.foodrecipe.data.database.entities.RecipesEntity
import com.abhi41.foodrecipe.model.FoodRecipe
import com.abhi41.foodrecipe.model.Result
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.*
import javax.inject.Inject

@HiltAndroidTest
class RecipesDaoTest {
    @get:Rule  //we need this for getOrAwaitValue method to execute tasks synchronously.
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var recipesDatabase: RecipesDatabase
    lateinit var recipesDao: RecipesDao


    @Before
    fun setUp() {
        hiltAndroidRule.inject()
      /*  recipesDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RecipesDatabase::class.java
        ).allowMainThreadQueries().build()*/

        recipesDao = recipesDatabase.recipeDao()
    }

    @Test
    fun insertRecipesSingleQuote() = runBlocking{
        val recipe = RecipesEntity(
            FoodRecipe(
                listOf(
                    Result(
                        26,
                        true,
                        true,
                        emptyList(),
                        false,
                        1,
                        "",
                        20,
                        "",
                        "",
                        "testRecipe1",
                        "test1",
                        false,
                        false,
                        false
                    ),
                )
            )
        )

        recipesDao.insertRecipes(recipe)
        recipesDao.deleteAllFavorites()

        recipesDao.readRecipes().collect { list->

            Assert.assertEquals(1,list.size)
            Assert.assertEquals(26, list[0].foodRecipe.results[0].aggregateLikes)
        }
    }

    @After
    fun tearDown() {
        recipesDatabase.close()
    }
}