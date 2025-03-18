package br.com.octashield.donanice
import androidx.lifecycle.LiveData

class CartRepository(private val cartDao: CartDao) {

    fun getCartItems(userId: String): LiveData<List<CartItem>> = cartDao.getCartItems(userId)

    fun getTotalCartValue(userId: String): LiveData<Double> = cartDao.getTotalCartValue(userId)

    suspend fun insertItem(item: CartItem) {
        cartDao.insertItem(item)
    }

    suspend fun clearCart(userId: String) {
        cartDao.clearCart(userId)
    }
}
