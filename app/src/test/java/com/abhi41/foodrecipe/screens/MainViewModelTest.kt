package com.abhi41.foodrecipe.screens

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.abhi41.foodrecipe.data.Repository
import com.abhi41.foodrecipe.data.UseCase.UseCases
import com.abhi41.foodrecipe.data.database.entities.RecipesEntity
import com.abhi41.foodrecipe.data.network.RemoteDataSource
import com.abhi41.foodrecipe.extention.getOrAwaitValue
import com.abhi41.foodrecipe.model.FoodRecipe
import com.abhi41.foodrecipe.utils.Constants
import com.abhi41.foodrecipe.utils.NetworkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response
import javax.inject.Inject

@HiltAndroidTest
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: Repository

    @Mock
    lateinit var remoteDataSource: RemoteDataSource

    @Mock
    lateinit var context: Application

    @Mock
    lateinit var useCases: UseCases


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun test_getRecipes () {
       return runTest {
            Mockito.`when`(repository.remote.getRecepies(applyQueries())).thenReturn(
                Response.success(FoodRecipe(emptyList()))
            )
           val sut = MainViewModel(repository,context,useCases)
           sut.getRecipes(applyQueries())
           testDispatcher.scheduler.advanceUntilIdle()
           val result = sut.readRecipes.getOrAwaitValue()
           assertEquals(0,result.size)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[Constants.QUERY_NUMBER] = Constants.DEFAULT_RECIPES_NUMBER
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_TYPE] = Constants.DEFAULT_MEAL_TYPE
        queries[Constants.QUERY_DIET] = Constants.DEFAULT_DIET_TYPE
        queries[Constants.QUERY_ADD_RECIPE_INFO] = "true"
        queries[Constants.QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }
}