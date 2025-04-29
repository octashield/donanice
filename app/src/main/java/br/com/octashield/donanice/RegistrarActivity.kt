package br.com.octashield.donanice

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.octashield.donanice.databinding.ActivityRegistrarBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegistrarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupGeneroSpinner()
        aplicarMascaraDataNascimento()

        binding.btnRegistrar.setOnClickListener {
            val nome = binding.edtNome.text.toString()
            val email = binding.edtEmail.text.toString()
            val senha = binding.edtSenha.text.toString()
            val genero = binding.spinnerGenero.selectedItem.toString()
            val nascimento = binding.edtNascimento.text.toString()

            if (nome.isBlank() || email.isBlank() || senha.isBlank() || nascimento.isBlank()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                        // Dados adicionais do usuário
                        val dadosUsuario = mapOf(
                            "nome" to nome,
                            "email" to email,
                            "genero" to genero,
                            "nascimento" to nascimento
                        )

                        // Salvar no Realtime Database
                        val dbRef = com.google.firebase.database.FirebaseDatabase.getInstance().reference
                        dbRef.child("usuarios").child(userId).setValue(dadosUsuario)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Erro ao salvar dados: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    } else {
                        Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun setupGeneroSpinner() {
        val opcoes = listOf("Masculino", "Feminino", "LGBTQIA+")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcoes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGenero.adapter = adapter
    }

    private fun aplicarMascaraDataNascimento() {
        binding.edtNascimento.addTextChangedListener(object : TextWatcher {
            var isUpdating = false
            val mascara = "##/##/####"

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                isUpdating = true

                val str = s.toString().filter { it.isDigit() }
                val builder = StringBuilder()

                var i = 0
                for (m in mascara.toCharArray()) {
                    if (m != '#' && str.length > i) {
                        builder.append(m)
                        continue
                    }
                    try {
                        builder.append(str[i])
                    } catch (e: Exception) {
                        break
                    }
                    i++
                }

                binding.edtNascimento.setText(builder.toString())
                binding.edtNascimento.setSelection(builder.length)
                isUpdating = false
            }
        })
    }
    private fun dataValida(data: String): Boolean {
        return try {
            val formato = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            formato.isLenient = false
            val dataNascimento = formato.parse(data)

            val hoje = Calendar.getInstance()
            val nascimentoCal = Calendar.getInstance().apply { time = dataNascimento }

            val idade = hoje.get(Calendar.YEAR) - nascimentoCal.get(Calendar.YEAR)
            idade >= 10 // mínimo de 10 anos
        } catch (e: Exception) {
            false
        }
    }

}
