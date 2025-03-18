package br.com.octashield.donanice
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: CartItem)

    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    fun getCartItems(userId: String): LiveData<List<CartItem>>

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCart(userId: String)

    @Query("SELECT SUM(price * quantity) FROM cart_items WHERE userId = :userId")
    fun getTotalCartValue(userId: String): LiveData<Double>
}
