package br.com.octashield.donanice
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val barcode: String,
    val price: Double,
    val quantity: Int,
    val location: String,
    val timestamp: Long = System.currentTimeMillis()
)
