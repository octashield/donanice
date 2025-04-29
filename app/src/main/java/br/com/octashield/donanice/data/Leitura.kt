package br.com.octashield.donanice.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leitura")
data class Leitura(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var userId: String = "",
    var codigo: String = "",
    var nome: String? = null,
    var valorUnit: Double = 0.0,
    var qtde: Int = 0,
    var data: String = "",
    var hora: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var sincronizado: Boolean = false
) {
    // 🔥 Construtor vazio necessário para o Firebase
    constructor() : this(0, "", "", null, 0.0, 0, "", "", 0.0, 0.0, false)
}


