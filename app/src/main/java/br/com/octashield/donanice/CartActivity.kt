package br.com.octashield.donanice

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth


class CartActivity : AppCompatActivity() {
    private val cartViewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCart)
        val totalTextView = findViewById<TextView>(R.id.totalTextView)
        val btnZerarCarrinho = findViewById<Button>(R.id.btnZerarCarrinho)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = CartAdapter()
        recyclerView.adapter = adapter

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        userId?.let {
            cartViewModel.getCartItems(it).observe(this) { items ->
                adapter.updateCart(items)
            }

            cartViewModel.getTotalCartValue(it).observe(this) { total ->
                totalTextView.text = "Total: R$ ${total ?: 0.0}"
            }

            btnZerarCarrinho.setOnClickListener {
                cartViewModel.clearCart(userId)
                Toast.makeText(this, "Carrinho zerado!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
