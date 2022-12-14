package com.abhi41.foodrecipe

import com.abhi41.foodrecipe.data.network.FoodRecipesApi
import com.abhi41.foodrecipe.data.network.RemoteDataSource
import com.abhi41.foodrecipe.model.FoodRecipe
import com.abhi41.foodrecipe.utils.Constants
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RecipesAPITest {

    lateinit var mockWebServer: MockWebServer
    lateinit var foodRecipesApi: FoodRecipesApi
    @Before
    fun setup() {
        mockWebServer = MockWebServer()

        foodRecipesApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(FoodRecipesApi::class.java)
    }

    @Test
    fun testGetEmptyRecipes() = runTest{
        val mockResponse = MockResponse()
        mockResponse.setBody("{\"results\": []}")
        mockWebServer.enqueue(mockResponse)

        val response = foodRecipesApi.getRecipies(applyQueries())
        mockWebServer.takeRequest()
        assertEquals(true,response.body()?.results?.isEmpty())

    }

    @Test
    fun testGetRecipes() = runTest{
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/recipes.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)

        val response = foodRecipesApi.getRecipies(applyQueries())
        mockWebServer.takeRequest()
        assertEquals(false,response.body()?.results?.isEmpty())
        assertEquals(10,response.body()?.results?.size)
    }

    @Test
    fun testRecipes_returnError() = runTest{
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(404)
        mockResponse.setBody("Something went Wrong")
        mockWebServer.enqueue(mockResponse)

        val response = foodRecipesApi.getRecipies(applyQueries())
        mockWebServer.takeRequest()
        assertEquals(false,response.isSuccessful)
        assertEquals(404,response.code())
    }

    @After
    fun teadDown(){
        mockWebServer.shutdown()
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