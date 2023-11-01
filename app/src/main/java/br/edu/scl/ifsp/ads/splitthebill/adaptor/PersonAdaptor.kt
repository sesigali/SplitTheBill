package br.edu.scl.ifsp.ads.splitthebill.adaptor

import android.content.ClipDescription
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
    private data class TilePersonHolder(
        val nameTv: TextView,
        val spent1Tv: TextView,
        val spent2Tv: TextView,
        val spent3Tv: TextView,
        val description1Et: TextView,
        val description2Et: TextView,
        val description3Et: TextView,
        val debtTv: TextView
    )
    //private data class TilePersonHolder(val nameTv: TextView, val spentTv: TextView, val debtTv: TextView)

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
                personTileView.findViewById(R.id.item1Tv),
                personTileView.findViewById(R.id.item2Tv),
                personTileView.findViewById(R.id.item3Tv),
                personTileView.findViewById(R.id.debtTv),
                personTileView.findViewById(R.id.description1Et),
                personTileView.findViewById(R.id.description2Et),
                personTileView.findViewById(R.id.description3Et),

                )

            // Associamos o objeto TilePersonHolder à visualização para reutilização
            personTileView.tag = tilePersonHolder
        }

        // Utilizamos o objeto TilePersonHolder para configurar as visualizações com os dados da pessoa
        with(personTileView?.tag as TilePersonHolder) {
            nameTv.text = person.name
            spent1Tv.text = "Dinheiro Gasto: " + person.spent1
            spent2Tv.text = "Dinheiro Gasto: " + person.spent2
            spent3Tv.text = "Dinheiro Gasto: " + person.spent3
            description1Et.text = "Descrição: " + person.description1
            description2Et.text = "Descrição: " + person.description2
            description3Et.text = "Descrição: " + person.description3
            //debtTv.text = "Total: " + person.debt

            // Verifica para ajustar a exibição
            if (person.debt.toDouble() != null) {
                person.debt = person.debt.toDouble().toString()
                debtTv.text = "Total: " + person.debt
            }
        }

        return personTileView
    }
}
