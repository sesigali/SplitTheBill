package br.edu.scl.ifsp.ads.splitthebill.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.scl.ifsp.ads.splitthebill.R
import br.edu.scl.ifsp.ads.splitthebill.model.Person

class PersonAdapter(private val context: Context, private val personList: MutableList<Person>) :
    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    inner class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTv: TextView = itemView.findViewById(R.id.nameTv) as TextView
        val spentTv: TextView = itemView.findViewById(R.id.spentTv) as TextView
        val debtTv: TextView = itemView.findViewById(R.id.debtTv) as TextView
    }

    // Cria e retorna uma nova instância do ViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.tile_person, parent, false)
        return PersonViewHolder(itemView)
    }

    // Preenche os dados do ViewHolder com os dados da pessoa na posição especificada.
    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = personList[position]

        with(holder) {
            nameTv.text = person.name
            spentTv.text = "Dinheiro Gasto: ${person.spent}"
            if (person.debt.toDouble() < 0) {
                person.debt = (person.debt.toDouble() * -1).toString()
                debtTv.text = "Deve Receber: R$ ${person.debt}"
            } else {
                debtTv.text = "Deve Pagar: R$ ${person.debt}"
            }
        }
    }

    // Retorna o número total de itens na lista.
    override fun getItemCount(): Int {
        return personList.size
    }
}
