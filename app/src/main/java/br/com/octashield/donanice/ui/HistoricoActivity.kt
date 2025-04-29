package br.com.octashield.donanice.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.octashield.donanice.data.AppDatabase
import br.com.octashield.donanice.databinding.ActivityHistoricoBinding
import br.com.octashield.donanice.ui.adapter.LeituraAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoricoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoricoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoricoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerLeituras.layoutManager = LinearLayoutManager(this)

        carregarHistorico()
    }

    private fun carregarHistorico() {
        lifecycleScope.launch(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(this@HistoricoActivity).leituraDao()
            val leituras = dao.listarTodas()

            withContext(Dispatchers.Main) {
                if (leituras.isEmpty()) {
                    binding.recyclerLeituras.visibility = android.view.View.GONE
                    binding.txtSemLeituras.visibility = android.view.View.VISIBLE
                } else {
                    binding.recyclerLeituras.adapter = LeituraAdapter(leituras)
                    binding.recyclerLeituras.visibility = android.view.View.VISIBLE
                    binding.txtSemLeituras.visibility = android.view.View.GONE
                }
            }
        }
    }
}
