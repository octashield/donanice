package br.com.octashield.donanice.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.octashield.donanice.data.Leitura
import br.com.octashield.donanice.data.LeituraRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LeituraViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LeituraRepository(application.applicationContext)

    suspend fun inserir(leitura: Leitura) {
        withContext(Dispatchers.IO) {
            repository.inserir(leitura)
        }
    }

    suspend fun listarTodas(): List<Leitura> {
        return withContext(Dispatchers.IO) {
            repository.listarTodas()
        }
    }

    suspend fun obterMenorPreco(codigo: String, latitude: Double, longitude: Double, raio: Int): Double? {
        return withContext(Dispatchers.IO) {
            repository.buscarMenorPrecoFirebase(codigo, latitude, longitude, raio)
        }
    }

    suspend fun listarNaoSincronizadas(): List<Leitura> {
        return withContext(Dispatchers.IO) {
            repository.listarNaoSincronizadas()
        }
    }

    suspend fun sincronizarLeituras() {
        withContext(Dispatchers.IO) {
            repository.sincronizarLeituras()
        }
    }
}
