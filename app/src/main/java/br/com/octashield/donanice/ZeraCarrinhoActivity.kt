package br.com.octashield.donanice

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.octashield.donanice.data.AppDatabase
import br.com.octashield.donanice.data.Leitura
import br.com.octashield.donanice.databinding.ActivityZeracarrinhoBinding
import br.com.octashield.donanice.ui.adapter.LeituraAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ZeraCarrinhoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityZeracarrinhoBinding
    private lateinit var leituras: List<Leitura>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Inicializa corretamente o binding
        binding = ActivityZeracarrinhoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerCarrinho.layoutManager = LinearLayoutManager(this)

        carregarLeituras()

        binding.btnZerar.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Zerar carrinho?")
                .setMessage("Essa ação apagará todos os itens do carrinho. Deseja continuar?")
                .setPositiveButton("Sim") { _, _ ->
                    lifecycleScope.launch(Dispatchers.IO) {
                        AppDatabase.getDatabase(this@ZeraCarrinhoActivity).leituraDao().deleteAll()
                        withContext(Dispatchers.Main) {
                            salvarTotalNoSharedPreferences(0.0)
                            finish()
                        }
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }


        binding.btnContinuar.setOnClickListener {
            finish() // volta pro carrinho SEM ZERAR
        }
    }

    private fun carregarLeituras() {
        lifecycleScope.launch(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(this@ZeraCarrinhoActivity).leituraDao()
            leituras = dao.listarTodas()
            val total = leituras.sumOf { it.qtde * it.valorUnit }
            salvarTotalNoSharedPreferences(total)

            withContext(Dispatchers.Main) {
                binding.recyclerCarrinho.adapter = LeituraAdapter(leituras)
                binding.txtTotal.text = "Total: R$ %.2f".format(total)
            }
        }
    }
    private fun salvarTotalNoSharedPreferences(total: Double) {
        val prefs = getSharedPreferences("carrinho", MODE_PRIVATE)
        prefs.edit().putFloat("total", total.toFloat()).apply()
    }

}
