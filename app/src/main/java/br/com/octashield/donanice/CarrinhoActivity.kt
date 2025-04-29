package br.com.octashield.donanice

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import br.com.octashield.donanice.data.Leitura
import br.com.octashield.donanice.databinding.ActivityCarrinhoBinding
import br.com.octashield.donanice.viewmodel.LeituraViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CarrinhoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarrinhoBinding
    private lateinit var locationClient: FusedLocationProviderClient
    private val viewModel: LeituraViewModel by viewModels()

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var totalCarrinho: Double = 0.0
    private var raio: Int = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarrinhoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationClient = LocationServices.getFusedLocationProviderClient(this)

        raio = getSharedPreferences("config", Context.MODE_PRIVATE).getInt("raio", 1000)

        binding.btnLerCodigo.setOnClickListener {
            iniciarLeituraCodigo()
        }

        binding.btnSalvar.setOnClickListener {
            salvarLeitura()
        }

        binding.btnConfiguracoes.setOnClickListener {
            startActivity(Intent(this, ConfiguracoesActivity::class.java))
        }

        binding.btnZerar.setOnClickListener {
            startActivity(Intent(this, ZeraCarrinhoActivity::class.java))
        }

        binding.btnHistorico.setOnClickListener {
            startActivity(Intent(this, br.com.octashield.donanice.ui.HistoricoActivity::class.java))
        }

        atualizarTotal()
    }

    private fun iniciarLeituraCodigo() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("Aproxime o código de barras")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.setOrientationLocked(false)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null && result.contents != null) {
            binding.edtCodigo.setText(result.contents)
            obterLocalizacao()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun obterLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        locationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude
                Toast.makeText(this, "Localização obtida", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Erro ao obter localização", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun salvarLeitura() {
        val codigo = binding.edtCodigo.text.toString()
        val nome = binding.edtNomeProduto.text.toString().ifBlank { null }
        val valorUnit = binding.edtValorUnit.text.toString().toDoubleOrNull()
        val qtde = binding.edtQtde.text.toString().toIntOrNull()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "desconhecido"

        if (codigo.isBlank() || valorUnit == null || qtde == null) {
            Toast.makeText(this, "Preencha código, valor e quantidade!", Toast.LENGTH_SHORT).show()
            return
        }

        val total = valorUnit * qtde
        totalCarrinho += total
        salvarTotalNoSharedPreferences(totalCarrinho)
        atualizarTotal()

        val dataAtual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val horaAtual = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        val leitura = Leitura(
            userId = userId,
            codigo = codigo,
            nome = nome,
            valorUnit = valorUnit,
            qtde = qtde,
            data = dataAtual,
            hora = horaAtual,
            latitude = latitude,
            longitude = longitude
        )

        lifecycleScope.launch {
            viewModel.inserir(leitura)

            FirebaseDatabase.getInstance().reference
                .child("leituras")
                .push()
                .setValue(leitura)
                .addOnSuccessListener {
                    Toast.makeText(this@CarrinhoActivity, "Leitura salva online!", Toast.LENGTH_SHORT).show()

                    // ✅ SOMENTE AQUI DENTRO! Agora podemos chamar o menor preço!
                    lifecycleScope.launch {
                        val menorPreco = viewModel.obterMenorPreco(codigo, latitude, longitude, raio)
                        if (menorPreco != null) {
                            binding.txtMenorPreco.text = "💰 Menor preço próximo: R$ %.2f".format(menorPreco)
                            binding.txtMenorPreco.visibility = android.view.View.VISIBLE
                        } else {
                            binding.txtMenorPreco.text = ""
                            binding.txtMenorPreco.visibility = android.view.View.GONE
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this@CarrinhoActivity, "Erro ao salvar no Firebase: ${e.message}", Toast.LENGTH_LONG).show()
                }

            limparCampos()
        }
    }

    private fun atualizarTotal() {
        val prefs = getSharedPreferences("carrinho", MODE_PRIVATE)
        totalCarrinho = prefs.getFloat("total", 0.0f).toDouble()
        binding.txtTotal.text = "Total: R$ %.2f".format(totalCarrinho)
    }

    private fun salvarTotalNoSharedPreferences(total: Double) {
        val prefs = getSharedPreferences("carrinho", MODE_PRIVATE)
        prefs.edit().putFloat("total", total.toFloat()).apply()
    }

    private fun limparCampos() {
        binding.edtCodigo.setText("")
        binding.edtNomeProduto.setText("")
        binding.edtValorUnit.setText("")
        binding.edtQtde.setText("")
        binding.edtValorUnit.requestFocus()
    }
}
