package br.com.octashield.donanice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.octashield.donanice.databinding.ItemCartBinding

class CartAdapter : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val cartItems = mutableListOf<CartItem>() // 🔹 Agora armazenando objetos CartItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size

    fun updateCart(items: List<CartItem>) { // 🔹 Alterado para aceitar List<CartItem>
        cartItems.clear()
        cartItems.addAll(items)
        notifyDataSetChanged()
    }

    class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) { // 🔹 Agora recebe um CartItem
            binding.textViewItemName.text = item.barcode // 🔹 Ajuste no XML necessário
        }
    }
}
