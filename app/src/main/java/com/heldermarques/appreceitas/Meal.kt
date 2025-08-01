package com.heldermarques.appreceitas

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Meal(
    @SerializedName("idMeal")
    val id: String,

    @SerializedName("strMeal")
    val nome: String,

    @SerializedName("strMealThumb")
    val linkImagem: String,

    // --- Campos da API de Detalhes (lookup.php) ---
    @SerializedName("strCategory")
    val categoria: String? = null,

    @SerializedName("strArea")
    val area: String? = null,

    @SerializedName("strInstructions")
    val modoPreparo: String? = null,

    @SerializedName("strYoutube")
    val linkYoutube: String? = null,

    @SerializedName("strIngredient1")
    val strIngredient1: String? = null,
    @SerializedName("strMeasure1")
    val strMeasure1: String? = null,
    @SerializedName("strIngredient2")
    val strIngredient2: String? = null,
    @SerializedName("strMeasure2")
    val strMeasure2: String? = null,
    @SerializedName("strIngredient3")
    val strIngredient3: String? = null,
    @SerializedName("strMeasure3")
    val strMeasure3: String? = null,
    @SerializedName("strIngredient4")
    val strIngredient4: String? = null,
    @SerializedName("strMeasure4")
    val strMeasure4: String? = null,
    @SerializedName("strIngredient5")
    val strIngredient5: String? = null,
    @SerializedName("strMeasure5")
    val strMeasure5: String? = null,
    @SerializedName("strIngredient6")
    val strIngredient6: String? = null,
    @SerializedName("strMeasure6")
    val strMeasure6: String? = null,
    @SerializedName("strIngredient7")
    val strIngredient7: String? = null,
    @SerializedName("strMeasure7")
    val strMeasure7: String? = null,
    @SerializedName("strIngredient8")
    val strIngredient8: String? = null,
    @SerializedName("strMeasure8")
    val strMeasure8: String? = null,
    @SerializedName("strIngredient9")
    val strIngredient9: String? = null,
    @SerializedName("strMeasure9")
    val strMeasure9: String? = null,
    @SerializedName("strIngredient10")
    val strIngredient10: String? = null,
    @SerializedName("strMeasure10")
    val strMeasure10: String? = null,
    @SerializedName("strIngredient11")
    val strIngredient11: String? = null,
    @SerializedName("strMeasure11")
    val strMeasure11: String? = null,
    @SerializedName("strIngredient12")
    val strIngredient12: String? = null,
    @SerializedName("strMeasure12")
    val strMeasure12: String? = null,
    @SerializedName("strIngredient13")
    val strIngredient13: String? = null,
    @SerializedName("strMeasure13")
    val strMeasure13: String? = null,
    @SerializedName("strIngredient14")
    val strIngredient14: String? = null,
    @SerializedName("strMeasure14")
    val strMeasure14: String? = null,
    @SerializedName("strIngredient15")
    val strIngredient15: String? = null,
    @SerializedName("strMeasure15")
    val strMeasure15: String? = null,
    @SerializedName("strIngredient16")
    val strIngredient16: String? = null,
    @SerializedName("strMeasure16")
    val strMeasure16: String? = null,
    @SerializedName("strIngredient17")
    val strIngredient17: String? = null,
    @SerializedName("strMeasure17")
    val strMeasure17: String? = null,
    @SerializedName("strIngredient18")
    val strIngredient18: String? = null,
    @SerializedName("strMeasure18")
    val strMeasure18: String? = null,
    @SerializedName("strIngredient19")
    val strIngredient19: String? = null,
    @SerializedName("strMeasure19")
    val strMeasure19: String? = null,
    @SerializedName("strIngredient20")
    val strIngredient20: String? = null,
    @SerializedName("strMeasure20")
    val strMeasure20: String? = null
)

data class MealListResponse(
    @SerializedName("meals") val meals: List<Meal>?
)