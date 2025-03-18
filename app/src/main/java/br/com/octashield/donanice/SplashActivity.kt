package br.com.octashield.donanice

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            val user = FirebaseAuth.getInstance().currentUser

            if (user != null) {
                // Usuário está logado, vai direto para o carrinho
                startActivity(Intent(this, CartActivity::class.java))
            } else {
                // Usuário não está logado, vai para a tela de login
                startActivity(Intent(this, LoginActivity::class.java))
            }

            finish()
        }, 2000) // 2 segundos de Splash
    }
}
