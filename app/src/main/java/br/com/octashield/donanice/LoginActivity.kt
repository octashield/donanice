package br.com.octashield.donanice

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import br.com.octashield.donanice.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupBiometria()

        binding.btnEntrar.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val senha = binding.edtSenha.text.toString()

            if (email.isNotEmpty() && senha.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            salvarPreferenciaLogin()
                            abrirTelaPrincipal()
                        } else {
                            Toast.makeText(this, "Erro no login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBiometria.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

        if (foiLogadoAntes()) {
            binding.btnBiometria.visibility = android.view.View.VISIBLE
        }
    }

    private fun setupBiometria() {
        val executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    abrirTelaPrincipal()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Biometria inválida", Toast.LENGTH_SHORT).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login com biometria")
            .setSubtitle("Use sua digital ou face")
            .setNegativeButtonText("Cancelar")
            .build()
    }

    private fun abrirTelaPrincipal() {
        startActivity(Intent(this, ZeraCarrinhoActivity::class.java))
        finish()
    }

    private fun salvarPreferenciaLogin() {
        getSharedPreferences("prefs", MODE_PRIVATE)
            .edit()
            .putBoolean("logadoAntes", true)
            .apply()
    }

    private fun foiLogadoAntes(): Boolean {
        return getSharedPreferences("prefs", MODE_PRIVATE)
            .getBoolean("logadoAntes", false)
    }
}
