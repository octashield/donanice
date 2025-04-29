package br.com.octashield.donanice.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.octashield.donanice.data.Leitura
import br.com.octashield.donanice.databinding.ItemLeituraBinding

class LeituraAdapter(private val leituras: List<Leitura>) : RecyclerView.Adapter<LeituraAdapter.LeituraViewHolder>() {

    inner class LeituraViewHolder(private val binding: ItemLeituraBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(leitura: Leitura) {
            binding.txtNome.text = leitura.nome ?: "Produto sem nome"
            binding.txtCodigo.text = "Código: ${leitura.codigo}"
            binding.txtValor.text = "R$ %.2f".format(leitura.valorUnit)
            binding.txtQuantidade.text = "Qtd: ${leitura.qtde}"
            binding.txtDataHora.text = "${leitura.data} ${leitura.hora}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeituraViewHolder {
        val binding = ItemLeituraBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeituraViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeituraViewHolder, position: Int) {
        holder.bind(leituras[position])
    }

    override fun getItemCount(): Int = leituras.size
}
