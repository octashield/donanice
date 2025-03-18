package br.com.octashield.donanice
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val cartDao = CartDatabase.getDatabase(application).cartDao()
    private val repository = CartRepository(cartDao)

    fun getCartItems(userId: String) = repository.getCartItems(userId)

    fun getTotalCartValue(userId: String) = repository.getTotalCartValue(userId)

    fun addItemToCart(item: CartItem) = viewModelScope.launch {
        repository.insertItem(item)
    }

    fun clearCart(userId: String) = viewModelScope.launch {
        repository.clearCart(userId)
    }
}
