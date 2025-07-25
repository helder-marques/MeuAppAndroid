package com.heldermarques.appreceitas

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await // Necessário para usar .await() com as Tasks do ML Kit
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import android.util.Log // Importar para usar Log.e e Log.w

class ReceitaActivity : AppCompatActivity() {


    private var englishPortugueseTranslator: com.google.mlkit.nl.translate.Translator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receita)


        val mealId = intent.getStringExtra("MEAL_ID")

        // Verificamos se o ID é válido
        if (mealId.isNullOrEmpty()) {
            Toast.makeText(this, "Erro: ID da receita não encontrado.", Toast.LENGTH_LONG).show()
            finish()
            return
        }


        val imagemView = findViewById<ImageView>(R.id.img_receita_detalhe)
        val tituloText = findViewById<TextView>(R.id.text_titulo_receita_detalhe)
        val ingredientesText = findViewById<TextView>(R.id.text_ingredientes_receita_detalhe)
        val preparoText = findViewById<TextView>(R.id.text_modo_preparo_receita_detalhe)


        CoroutineScope(Dispatchers.Main).launch {
            try {

                Toast.makeText(this@ReceitaActivity, "Verificando modelo de tradução...", Toast.LENGTH_SHORT).show()
                val options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.PORTUGUESE)
                    .build()
                englishPortugueseTranslator = Translation.getClient(options)


                englishPortugueseTranslator?.downloadModelIfNeeded()?.await()

                Toast.makeText(this@ReceitaActivity, "Modelo de tradução pronto.", Toast.LENGTH_SHORT).show()


                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.api.getMealDetails(mealId)
                }


                val mealDetail = response.meals?.firstOrNull()

                if (mealDetail != null) {

                    val translatedTitle = translateTextWithMLKit(englishPortugueseTranslator!!, mealDetail.nome ?: "")
                    tituloText.text = translatedTitle


                    val originalModoPreparo = mealDetail.modoPreparo ?: ""
                    val translatedModoPreparo = translateTextWithMLKit(englishPortugueseTranslator!!, originalModoPreparo)
                    preparoText.text = translatedModoPreparo



                    val ingredientesBuilder = StringBuilder()
                    for (i in 1..20) {
                        val ingredient = mealDetail::class.java.getDeclaredField("strIngredient$i").apply { isAccessible = true }.get(mealDetail) as? String
                        val measure = mealDetail::class.java.getDeclaredField("strMeasure$i").apply { isAccessible = true }.get(mealDetail) as? String

                        // Traduzimos o ingrediente se ele existir e não estiver em branco
                        val translatedIngredient = if (!ingredient.isNullOrBlank()) {
                            translateTextWithMLKit(englishPortugueseTranslator!!, ingredient)
                        } else {
                            null
                        }


                        if (!translatedIngredient.isNullOrBlank() && !measure.isNullOrBlank()) {
                            ingredientesBuilder.append("- $translatedIngredient ($measure)\n")
                        } else if (!translatedIngredient.isNullOrBlank()) {

                            ingredientesBuilder.append("- $translatedIngredient\n")
                        }
                    }
                    ingredientesText.text = ingredientesBuilder.toString().trim()


                    Glide.with(this@ReceitaActivity)
                        .load(mealDetail.linkImagem)
                        .into(imagemView)
                } else {

                    Toast.makeText(this@ReceitaActivity, "Receita detalhada não encontrada.", Toast.LENGTH_LONG).show()
                    finish()
                }

            } catch (e: Exception) {

                e.printStackTrace()
                Toast.makeText(this@ReceitaActivity, "Erro ao carregar ou traduzir detalhes: ${e.message}", Toast.LENGTH_LONG).show()
                finish()
            } finally {

                try {
                    englishPortugueseTranslator?.close()
                } catch (e: Exception) {

                    Log.w("TranslatorClose", "Erro ao fechar o tradutor em ReceitaActivity", e)
                }
            }
        }
    }

    /**
     * Função auxiliar suspensa para traduzir texto usando o ML Kit.
     * @param translator A instância do tradutor do ML Kit.
     * @param text O texto original a ser traduzido.
     * @return O texto traduzido, ou o texto original em caso de erro.
     */
    private suspend fun translateTextWithMLKit(
        translator: com.google.mlkit.nl.translate.Translator,
        text: String
    ): String {
        return try {

            if (text.isBlank()) return text

            translator.translate(text).await()
        } catch (e: Exception) {

            Log.e("MLKitTranslation", "Erro ao traduzir texto: $text", e)
            text
        }
    }
}
