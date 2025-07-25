package com.heldermarques.appreceitas

import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory // Importe GsonConverterFactory
import kotlinx.serialization.json.Json // Importe Json para kotlinx.serialization

object RetrofitClient {
    // URL base da API TheMealDB
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    // Configuração para kotlinx.serialization (se estiver usando)
    // ignoreUnknownKeys = true é importante para ignorar campos que não estão na sua data class
    private val json = Json { ignoreUnknownKeys = true }

    // Instância preguiçosa (lazy) da API TheMealDbApi
    val api: TheMealDbApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Escolha um dos conversores:
            // Se estiver usando Gson para serialização/deserialização:
            .addConverterFactory(GsonConverterFactory.create())
            // Se estiver usando kotlinx.serialization:
            // .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(TheMealDbApi::class.java)
    }
}