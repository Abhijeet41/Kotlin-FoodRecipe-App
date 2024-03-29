package com.abhi41.foodrecipe.utils

class Constants {

    companion object {

        const val BASE_URL = "https://api.spoonacular.com"
        const val BASE_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
        const val API_KEY = "4b1d5ec4278045d2a16c8bf467004700"
        //const val API_KEY = "4de00bcc444e489fb342b867ab400205"

        const val RECIPE_RESULT_KEY = "recipeBundle"

        // channel id push notification
        const val CHANNEL_ID = "RECIPES_PUSH_NOTIFICATION"
        const val CHANNEL_NAME = "RECIPES_CHANNEL"

        const val FCM_NOTIFICATION_ID = 1

        //API Queries Keys
        const val QUERY_SEARCH = "query"
        const val QUERY_NUMBER = "50"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFO = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        //ROOM Database
        const val DATABASE_NAME = "recipes_database"
        const val RECIPES_TABLE = "recipes_table"
        const val FAVORITE_RECIPES_TABLE = "favorite_recipes_table"
        const val FOOD_JOKE_TABLE = "food_joke_table"

        //Bottom Sheet and Preferences
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"
        const val DEFAULT_RECIPES_NUMBER = "50"

        const val PREFERENCES_NAME = "foody_preferences"
        const val PREFERENCES_MEAL_TYPE = "mealType"
        const val PREFERENCE_MEAL_TYPE_ID = "mealTypeId"
        const val PREFERENCES_DIET_TYPE = "dietType"
        const val PREFERENCE_DIET_TYPE_ID = "dietTypeId"
        const val PREFERENCE_BACK_ONLINE = "backOnline"
    }

}