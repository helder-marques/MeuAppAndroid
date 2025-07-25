package com.heldermarques.appreceitas

data class Receita(
    val id: Int,
    val titulo: String,
    val imagem: String,
    val ingredientes: List<String>,
    val preparo: List<String>,
    val tipo: String

)
