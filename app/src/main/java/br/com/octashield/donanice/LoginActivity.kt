package br.com.octashield.donanice

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val edtEmail = findViewById<EditText>(R.id.edtEmailLogin)
        val edtSenha = findViewById<EditText>(R.id.edtSenhaLogin)
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarLogin)

        btnEntrar.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val senha = edtSenha.text.toString().trim()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE
            btnEntrar.isEnabled = false

            auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener { task ->
                    progressBar.visibility = View.GONE
                    btnEntrar.isEnabled = true

                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, CartActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Erro no login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
