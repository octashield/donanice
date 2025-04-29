package br.com.octashield.donanice

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.octashield.donanice.databinding.ActivityConfiguracoesBinding

class ConfiguracoesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfiguracoesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracoesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("config", Context.MODE_PRIVATE)
        val raioSalvo = prefs.getInt("raio", 1000) // valor padrão: 1000 metros

        binding.edtRaio.setText(raioSalvo.toString())

        binding.btnSalvar.setOnClickListener {
            val novoRaio = binding.edtRaio.text.toString().toIntOrNull()

            if (novoRaio != null && novoRaio > 0) {
                prefs.edit().putInt("raio", novoRaio).apply()
                Toast.makeText(this, "Configurações salvas!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Informe um raio válido!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
