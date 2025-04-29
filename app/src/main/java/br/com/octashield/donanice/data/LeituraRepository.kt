package br.com.octashield.donanice.data

import android.content.Context
import com.google.firebase.database.FirebaseDatabase
//import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
//import kotlinx.coroutines.withContext

class LeituraRepository(private val context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val leituraDao = db.leituraDao()

    suspend fun inserir(leitura: Leitura) {
        leituraDao.inserir(leitura)
    }

    suspend fun listarTodas(): List<Leitura> {
        return leituraDao.listarTodas()
    }

    suspend fun listarPorCodigo(codigo: String): List<Leitura> {
        return leituraDao.listarPorCodigo(codigo)
    }

    suspend fun listarNaoSincronizadas(): List<Leitura> {
        return leituraDao.listarNaoSincronizadas()
    }

    suspend fun marcarComoSincronizada(id: Int) {
        leituraDao.marcarComoSincronizada(id)
    }

    suspend fun apagarSincronizadas() {
        leituraDao.apagarSincronizadas()
    }

    suspend fun sincronizarLeituras() {
        val firebaseRef = FirebaseDatabase.getInstance().reference.child("leituras")
        val leiturasNaoSincronizadas = leituraDao.listarNaoSincronizadas()

        leiturasNaoSincronizadas.forEach { leitura ->
            val pushRef = firebaseRef.push()
            pushRef.setValue(leitura).await()
            leituraDao.marcarComoSincronizada(leitura.id)
        }

        leituraDao.apagarSincronizadas()
    }

    suspend fun buscarMenorPrecoFirebase(codigo: String, latitude: Double, longitude: Double, raio: Int): Double? {
        val firebaseRef = FirebaseDatabase.getInstance().reference.child("leituras")
        val snapshot = firebaseRef.get().await()

        val lista = snapshot.children.mapNotNull { it.getValue(Leitura::class.java) }
            .filter { it.codigo == codigo }
            .filter {
                val results = FloatArray(1)
                LocationUtils.distanceBetween(it.latitude, it.longitude, latitude, longitude, results)
                results[0] <= raio
            }

        return lista.minByOrNull { it.valorUnit }?.valorUnit
    }
}

