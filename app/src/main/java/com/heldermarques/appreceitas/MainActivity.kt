package com.heldermarques.appreceitas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.heldermarques.appreceitas.SupabaseCliente.supabase
import com.heldermarques.appreceitas.databinding.ActivityMainBinding
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding) {
            btnLogin.setOnClickListener {
                val email = inputEmail.text.toString().trim()
                val password = inputSenha.text.toString().trim()

                lifecycleScope.launch {
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(this@MainActivity, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    try {
                        // Tenta fazer login
                        supabase.auth.signInWith(Email) {
                            this.email = email
                            this.password = password
                        }

                        Toast.makeText(this@MainActivity, "Login realizado!", Toast.LENGTH_SHORT).show()

                        // Redireciona para outra tela
                        val intent = Intent(this@MainActivity, ReceitasActivity::class.java)
                        startActivity(intent)
                        finish()

                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, "Erro no login: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }

            btnCadastro.setOnClickListener {
                val intent = Intent(this@MainActivity, CadastroActivity::class.java)
                startActivity(intent)
            }
        }

    }

    }
