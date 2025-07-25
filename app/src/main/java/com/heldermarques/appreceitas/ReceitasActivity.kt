package com.heldermarques.appreceitas

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.heldermarques.appreceitas.databinding.ActivityReceitasBinding

class ReceitasActivity : AppCompatActivity() {

    val binding by lazy{
        ActivityReceitasBinding.inflate(layoutInflater)
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

        with(binding){
            btnAgridoce.setOnClickListener{
                intent= Intent(binding.root.context,ListaReceitasActivity::class.java)
                startActivity(intent)
            }
        }
    }
}