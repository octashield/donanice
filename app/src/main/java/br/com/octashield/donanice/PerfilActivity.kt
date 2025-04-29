package br.com.octashield.donanice

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.octashield.donanice.databinding.ActivityPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class PerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGeneroSpinner()
        setupDataNascimento()

        carregarDados()

        binding.btnSalvar.setOnClickListener {
            salvarDados()
        }
    }

    private fun carregarDados() {
        val uid = auth.currentUser?.uid ?: return

        db.child("usuarios").child(uid).get().addOnSuccessListener { snapshot ->
            val nome = snapshot.child("nome").value?.toString() ?: ""
            val genero = snapshot.child("genero").value?.toString() ?: ""
            val nascimento = snapshot.child("nascimento").value?.toString() ?: ""

            binding.edtNome.setText(nome)
            binding.edtNascimento.setText(nascimento)

            val generos = resources.getStringArray(R.array.generos)
            val index = generos.indexOf(genero)
            if (index != -1) {
                binding.spinnerGenero.setSelection(index)
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Erro ao carregar perfil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun salvarDados() {
        val uid = auth.currentUser?.uid ?: return
        val nome = binding.edtNome.text.toString()
        val genero = binding.spinnerGenero.selectedItem.toString()
        val nascimento = binding.edtNascimento.text.toString()

        if (nome.isBlank() || nascimento.isBlank()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        val dados = mapOf(
            "nome" to nome,
            "genero" to genero,
            "nascimento" to nascimento
        )

        db.child("usuarios").child(uid).updateChildren(dados).addOnSuccessListener {
            Toast.makeText(this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Erro ao salvar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupGeneroSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.generos,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGenero.adapter = adapter
    }

    private fun setupDataNascimento() {
        binding.edtNascimento.setOnClickListener {
            val cal = Calendar.getInstance()
            val ano = cal.get(Calendar.YEAR)
            val mes = cal.get(Calendar.MONTH)
            val dia = cal.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val data = "%02d/%02d/%04d".format(dayOfMonth, month + 1, year)
                binding.edtNascimento.setText(data)
            }, ano, mes, dia)

            dpd.show()
        }
    }
}

