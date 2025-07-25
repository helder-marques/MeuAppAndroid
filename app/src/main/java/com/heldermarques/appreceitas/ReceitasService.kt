package com.heldermarques.appreceitas

import retrofit2.http.GET
import retrofit2.http.Query

// Interface que define os endpoints da API TheMealDB
interface TheMealDbApi {

    // Endpoint para listar refeições por categoria
    // Ex: filter.php?c=Dessert
    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): MealListResponse

    // Endpoint para buscar detalhes de uma refeição por ID
    // Ex: lookup.php?i=53049
    @GET("lookup.php")
    suspend fun getMealDetails(@Query("i") mealId: String): MealListResponse
}
