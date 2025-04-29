package br.com.octashield.donanice

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(3000)

            val usuario = FirebaseAuth.getInstance().currentUser
            val proximaTela = if (usuario != null) {
                CarrinhoActivity::class.java
            } else {
                LoginRegistrarActivity::class.java
            }

            startActivity(Intent(this@SplashActivity, proximaTela))
            finish()
        }
    }
}
