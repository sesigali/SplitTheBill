package br.edu.scl.ifsp.ads.splitthebill.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.scl.ifsp.ads.splitthebill.R
import br.edu.scl.ifsp.ads.splitthebill.adaptor.PersonAdapter
import br.edu.scl.ifsp.ads.splitthebill.databinding.ActivityMainBinding
import br.edu.scl.ifsp.ads.splitthebill.model.Constant.EXTRA_PERSON
import br.edu.scl.ifsp.ads.splitthebill.model.Constant.VIEW_PERSON
import br.edu.scl.ifsp.ads.splitthebill.model.Person

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val personList: MutableList<Person> = mutableListOf()
    private lateinit var personAdapter: PersonAdapter
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    // Função para calcular as dívidas com base nos gastos das pessoas
    private fun modifyPersonList() {
        var totalMoneyAmount = 0.0
        for (person in personList) {
            totalMoneyAmount += person.spent.toDouble()
        }
        val dividedMoneyAmount = totalMoneyAmount / personList.size
        for (person in personList) {
            person.debt = (dividedMoneyAmount - person.spent.toDouble()).toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Inicializa o adaptador de pessoa e atribui ao ListView
        personAdapter = PersonAdapter(this, personList)
        binding.personLv.adapter = personAdapter

        // Notifica o adaptador de que os dados mudaram
        personAdapter.notifyDataSetChanged()

        // Inicializa o ActivityResultLauncher para obter resultados de outras atividades
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Obtém a pessoa retornada da atividade PersonActivity
                val person = result.data?.getParcelableExtra<Person>(EXTRA_PERSON)

                person?.let { _person ->
                    val position = personList.indexOfFirst { it.id == _person.id }
                    if (position != -1) {
                        // Atualiza a pessoa na lista se já existir
                        personList[position] = _person
                    } else {
                        // Adiciona a pessoa à lista se não existir
                        personList.add(_person)
                    }
                    // Notifica o adaptador de que os dados mudaram
                    personAdapter.notifyDataSetChanged()
                }
            }
        }

        // Registra o ListView para o menu de contexto
        registerForContextMenu(binding.personLv)

        // Define um ouvinte de clique para o ListView
        binding.personLv.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val person = personList[position]
                val personIntent = Intent(this@MainActivity, PersonActivity::class.java)
                personIntent.putExtra(EXTRA_PERSON, person)
                personIntent.putExtra(VIEW_PERSON, true)
                startActivity(personIntent)
            }
    }

    // Função chamada quando a atividade é retomada
    override fun onResume() {
        super.onResume()
        if (personList.size > 0) {
            // Calcula as dívidas quando há pessoas na lista
            modifyPersonList()
        }
    }

    // Cria o menu de opções na barra de ação
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Lida com a seleção de itens do menu de opções
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addPersonMi -> {
                // Inicia a atividade PersonActivity para adicionar uma nova pessoa
                resultLauncher.launch(Intent(this, PersonActivity::class.java))
                true
            }
            else -> false
        }
    }

    // Cria o menu de contexto (pressionando e segurando um item na lista)
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    // Lida com a seleção de itens no menu de contexto
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
        return when (item.itemId) {
            R.id.removePersonMi -> {
                // Remove uma pessoa da lista e atualiza as dívidas
                personList.removeAt(position)
                modifyPersonList()
                // Notifica o adaptador de que os dados mudaram
                personAdapter.notifyDataSetChanged()
                true
            }
            R.id.editPersonMi -> {
                // Edita uma pessoa existente
                val person = personList[position]
                val personIntent = Intent(this, PersonActivity::class.java)
                personIntent.putExtra(EXTRA_PERSON, person)
                personIntent.putExtra(VIEW_PERSON, false)
                // Inicia a atividade PersonActivity para edição
                resultLauncher.launch(personIntent)
                true
            }
            else -> false
        }
    }
}
