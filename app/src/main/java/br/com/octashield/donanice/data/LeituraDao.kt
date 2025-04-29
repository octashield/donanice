package br.com.octashield.donanice.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface LeituraDao {

    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    //suspend fun inserir(leitura: Leitura)
    @Insert
    suspend fun inserir(leitura: Leitura)

    //@Query("SELECT * FROM Leitura WHERE sincronizado = 0")
    //suspend fun listarNaoSincronizadas(): List<Leitura>
    @Query("SELECT * FROM leitura")
    suspend fun listarTodas(): List<Leitura>

    //@Query("SELECT * FROM Leitura")
    //suspend fun listarTodas(): List<Leitura>
    @Query("SELECT * FROM leitura WHERE codigo = :codigo")
    suspend fun listarPorCodigo(codigo: String): List<Leitura>

    //@Query("UPDATE Leitura SET sincronizado = 1 WHERE id = :id")
    //suspend fun marcarComoSincronizada(id: Int)
    @Query("SELECT * FROM leitura WHERE sincronizado = 0")
    suspend fun listarNaoSincronizadas(): List<Leitura>

    //@Query("DELETE FROM Leitura WHERE sincronizado = 1")
    //suspend fun apagarSincronizadas()
    @Query("UPDATE leitura SET sincronizado = 1 WHERE id = :id")
    suspend fun marcarComoSincronizada(id: Int)

    //@Query("DELETE FROM Leitura")
    //suspend fun deleteAll()
    @Query("DELETE FROM leitura WHERE sincronizado = 1")
    suspend fun apagarSincronizadas()

    @Query("DELETE FROM leitura")
    suspend fun deleteAll()

    //@Query("SELECT * FROM Leitura WHERE codigo = :codigo")
    //suspend fun listarPorCodigo(codigo: String): List<Leitura>  // ✅ AQUI QUE FALTAVA
}
