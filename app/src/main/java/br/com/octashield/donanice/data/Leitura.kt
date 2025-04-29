package br.com.octashield.donanice.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leitura")
data class Leitura(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val codigo: String,
    val nome: String? = null,
    val valorUnit: Double,
    val qtde: Int,
    val data: String,
    val hora: String,
    val latitude: Double,
    val longitude: Double,
    val sincronizado: Boolean = false
)

