package com.heldermarques.appreceitas

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.heldermarques.appreceitas.databinding.ActivityListaReceitasBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await
// import kotlinx.serialization.json.Json // Não é mais necessário para serializar o objeto Meal completo aqui

class ListaReceitasActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityListaReceitasBinding.inflate(layoutInflater)
    }

    private val listaReceitas = mutableListOf<Meal>() // Usa a mesma data class Meal expandida
    private val adapter by lazy { ReceitaAdapter(listaReceitas) }

    private var englishPortugueseTranslator: com.google.mlkit.nl.translate.Translator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.rcReceitas.adapter = adapter
        binding.rcReceitas.layoutManager = LinearLayoutManager(this)

        // --- ALTERAÇÃO CRÍTICA AQUI ---
        adapter.setOnItemClickListener { meal ->
            val intent = Intent(this@ListaReceitasActivity, ReceitaActivity::class.java)
            // Passe APENAS o ID da receita.
            // A ReceitaActivity será responsável por buscar os detalhes completos.
            intent.putExtra("MEAL_ID", meal.id)
            startActivity(intent)
        }
        // --- FIM DA ALTERAÇÃO CRÍTICA ---

        carregarReceitas()
    }

    private fun carregarReceitas() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                if (!isNetworkAvailable()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ListaReceitasActivity, "Sem conexão com a internet.", Toast.LENGTH_LONG).show()
                    }
                    return@launch
                }

                withContext(Dispatchers.Main) {
                    binding.progressBar.isVisible = true
                    Toast.makeText(this@ListaReceitasActivity, "Verificando modelo de tradução...", Toast.LENGTH_SHORT).show()
                }

                val options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.PORTUGUESE)
                    .build()
                englishPortugueseTranslator = Translation.getClient(options)

                englishPortugueseTranslator?.downloadModelIfNeeded()?.await()

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ListaReceitasActivity, "Modelo de tradução pronto.", Toast.LENGTH_SHORT).show()
                }


                val response = RetrofitClient.api.getMealsByCategory("Dessert")
                val mealsToDisplay = mutableListOf<Meal>()

                if (!response.meals.isNullOrEmpty()) {
                    for (meal in response.meals) {

                        val originalText = meal.nome ?: "Sem nome"
                        val translatedName = translateTextWithMLKit(englishPortugueseTranslator!!, originalText)

                        val translatedMeal = meal.copy(nome = translatedName)
                        mealsToDisplay.add(translatedMeal)
                    }

                    withContext(Dispatchers.Main) {
                        listaReceitas.clear()
                        listaReceitas.addAll(mealsToDisplay)
                        adapter.notifyDataSetChanged()
                        Toast.makeText(this@ListaReceitasActivity, "Receitas carregadas e traduzidas!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ListaReceitasActivity, "Nenhuma receita encontrada para esta categoria.", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ListaReceitasActivity,
                        "Erro ao carregar ou traduzir receitas: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("ListaReceitasActivity", "Erro ao carregar ou traduzir receitas", e)
                }
            } finally {
                try {
                    englishPortugueseTranslator?.close()
                } catch (e: Exception) {
                    Log.w("TranslatorClose", "Erro ao fechar o tradutor", e)
                }
                withContext(Dispatchers.Main) {
                    binding.progressBar.isVisible = false
                }
            }
        }
    }

    private suspend fun translateTextWithMLKit(
        translator: com.google.mlkit.nl.translate.Translator,
        text: String
    ): String {
        return try {
            translator.translate(text).await()
        } catch (e: Exception) {
            Log.e("MLKitTranslation", "Erro ao traduzir texto: $text", e)
            text // Retorna o texto original em caso de erro de tradução
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true
    }
}
