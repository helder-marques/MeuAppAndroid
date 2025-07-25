package com.heldermarques.appreceitas

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.heldermarques.appreceitas.SupabaseCliente.supabase
import com.heldermarques.appreceitas.databinding.ActivityCadastroBinding // Importar a classe de binding gerada
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CadastroActivity : AppCompatActivity() {

    // Reintroduzindo a inicialização lazy do binding
    private val binding by lazy {
        ActivityCadastroBinding.inflate(layoutInflater)
    }

    // Removidas as declarações lateinit var para as views individuais,
    // pois elas serão acessadas via objeto 'binding'.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root) // Usando binding.root para definir o layout

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets -> // Usando binding.root
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Usando o bloco 'with(binding)' para facilitar o acesso às views
        with(binding) {
            btnCadastrar.setOnClickListener {
                val nome = inputNome.text.toString().trim()
                val email = inputEmail.text.toString().trim().lowercase()
                val senha = inputSenha.text.toString() // Sem .trim() para senhas
                val confirmaSenha = inputConfirmarSenha.text.toString() // Sem .trim() para senhas

                // Validação de campos vazios
                if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmaSenha.isEmpty()) {
                    Toast.makeText(this@CadastroActivity, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Confirmação de que as senhas são iguais
                if (senha != confirmaSenha) {
                    Toast.makeText(this@CadastroActivity, "As senhas não coincidem!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Chama o método 'register' para lidar com a lógica de cadastro
                register(nome, email, senha)
            }
        }
    }

    // Método 'register'
    private fun register(nome: String, email: String, senha: String) {
        CoroutineScope(Dispatchers.IO).launch { // Usa CoroutineScope(Dispatchers.IO)
            try {
                // 1. Cria o usuário na autenticação do Supabase
                val result = supabase.auth.signUpWith(Email) {
                    this.email = email
                    this.password = senha
                }

                // Obtém o ID do usuário logado/cadastrado.
                // Importante: Se a confirmação de e-mail estiver ativada no Supabase,
                // `currentUserOrNull()` pode ser nulo aqui até o e-mail ser confirmado.
                // A mensagem de sucesso deve refletir isso.
                val userId = supabase.auth.currentUserOrNull()?.id
                    ?: throw Exception("Erro ao obter ID do usuário. Verifique seu email para confirmar a conta.")

                // 2. Insere os dados do usuário na tabela 'usuarios' usando um Map<String, Any>
                supabase.from("usuarios").insert(
                    mapOf(
                        "id" to userId,
                        "nome" to nome,
                        "email" to email
                    )
                )

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@CadastroActivity,
                        "Cadastro realizado com sucesso! Por favor, verifique seu e-mail para confirmar sua conta.",
                        Toast.LENGTH_LONG
                    ).show()
                    // Navega para a MainActivity após o cadastro
                    startActivity(Intent(this@CadastroActivity, MainActivity::class.java))
                    finish() // Fecha a tela de cadastro
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@CadastroActivity,
                        "Erro no cadastro: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("SUPABASE", "Erro ao cadastrar", e)
                }
            }
        }
    }
}