package com.abhi41.foodrecipe.data.network

import com.abhi41.foodrecipe.model.FoodRecipe
import com.abhi41.foodrecipe.model.Result
import com.abhi41.foodrecipe.utils.Constants
import com.abhi41.foodrecipe.utils.NetworkResult
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

class RemoteDataSourceTest {

    @Mock
    lateinit var foodRecipesApi: FoodRecipesApi

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }


    @Test
    fun testRecipes_emptyList() = runTest{
        Mockito.`when`(foodRecipesApi.getRecipies(applyQueries())).thenReturn(
            Response.success(FoodRecipe(emptyList()))
        )
        val sut = RemoteDataSource(foodRecipesApi)
        val result = sut.getRecepies(applyQueries())
        assertEquals(true, result is Response)
        assertEquals(0,result.body()?.results?.size)
    }

    @Test
    fun testRecipes_expectedRecipeList() = runTest{

        val recipeList = listOf<Result>(
            Result(3,true,true, emptyList(),false,1,"",20,"","","","",false,false,false),
            Result(5,false,true, emptyList(),false,2,"",30,"","","","",false,true,true)
        )

        Mockito.`when`(foodRecipesApi.getRecipies(applyQueries())).thenReturn(
            Response.success(FoodRecipe(recipeList))
        )
        val sut = RemoteDataSource(foodRecipesApi)
        val result = sut.getRecepies(applyQueries())
        assertEquals(true, result is Response)
        assertEquals(2,result.body()?.results?.size)
        assertEquals(3, result.body()!!.results[0].aggregateLikes)
    }

    @Test
    fun testRecipes_expectedError() = runTest{
        Mockito.`when`(foodRecipesApi.getRecipies(applyQueries())).thenReturn(
            //Response.error(401,"Unautherized".toResponseBody())
            Response.error(401,"Unautherized".toResponseBody())
        )
        val sut = RemoteDataSource(foodRecipesApi)
        val result = sut.getRecepies(applyQueries())
        assertEquals(401, result.code())
        assertEquals("Response.error()", result.message())
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


