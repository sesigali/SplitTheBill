package br.edu.scl.ifsp.ads.splitthebill.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.scl.ifsp.ads.splitthebill.R
import br.edu.scl.ifsp.ads.splitthebill.model.Person

class PersonAdapter(
    context: Context,
    private val personList: MutableList<Person>
) : ArrayAdapter<Person>(context, R.layout.tile_person, personList) {
    // Classe interna para armazenar as visualizações de cada item da lista
    private data class TilePersonHolder(val nameTv: TextView, val spentTv: TextView, val debtTv: TextView)

    // Método para criar ou reutilizar a visualização de um item da lista
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val person = personList[position]
        var personTileView = convertView

        if (personTileView == null) {
            // Se a visualização ainda não existe, inflamos uma nova
            personTileView =
                (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                    R.layout.tile_person,
                    parent,
                    false
                )

            // Criamos um objeto TilePersonHolder para armazenar as visualizações de cada elemento
            val tilePersonHolder = TilePersonHolder(
                personTileView.findViewById(R.id.nameTv),
                personTileView.findViewById(R.id.spentTv),
                personTileView.findViewById(R.id.debtTv)
            )

            // Associamos o objeto TilePersonHolder à visualização para reutilização
            personTileView.tag = tilePersonHolder
        }

        // Utilizamos o objeto TilePersonHolder para configurar as visualizações com os dados da pessoa
        with(personTileView?.tag as TilePersonHolder) {
            nameTv.text = person.name
            spentTv.text = "Dinheiro Gasto: " + person.spent

            // Verificamos se a dívida é negativa para ajustar a exibição
            if (person.debt.toDouble() < 0) {
                person.debt = (person.debt.toDouble() * -1).toString()
                debtTv.text = "Deve Receber: R$" + person.debt
            } else {
                debtTv.text = "Deve Pagar: R$" + person.debt
            }
        }

        return personTileView
    }
}
