package br.edu.scl.ifsp.ads.splitthebill.view

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.ads.splitthebill.databinding.ActivityPersonBinding
import br.edu.scl.ifsp.ads.splitthebill.model.Constant.EXTRA_PERSON
import br.edu.scl.ifsp.ads.splitthebill.model.Constant.VIEW_PERSON
import br.edu.scl.ifsp.ads.splitthebill.model.Person
import kotlin.random.Random

class PersonActivity : AppCompatActivity() {
    private val binding: ActivityPersonBinding by lazy {
        ActivityPersonBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Obtém a pessoa recebida como Parcelable da Intent
        val receivedPerson = intent.getParcelableExtra<Person>(EXTRA_PERSON)

        // Preenche os campos de entrada com os dados da pessoa, se existirem
        receivedPerson?.let { person ->
            with(binding) {
                nameEt.setText(person.name)
                spentEt.setText(person.spent)
                debtEt.setText(person.debt)
                descriptionEt.setText(person.description)
            }
        }

        // Verifica se a tela deve ser somente de visualização (viewOnly)
        val viewOnly = intent.getBooleanExtra(VIEW_PERSON, false)

        // Configura a atividade com base na visualização somente leitura
        with(binding) {
            nameEt.isEnabled = !viewOnly
            spentEt.isEnabled = !viewOnly
            debtEt.isEnabled = !viewOnly
            descriptionEt.isEnabled = !viewOnly

            // Define a visibilidade do botão "Salvar" com base na variável viewOnly
            saveBt.visibility = if (viewOnly) View.GONE else View.VISIBLE
        }

        // Configura o clique no botão "Salvar"
        binding.saveBt.setOnClickListener {
            // Cria um objeto Person com os dados inseridos nos campos de entrada
            val person = Person(
                id = receivedPerson?.id ?: Random(System.currentTimeMillis()).nextInt(),
                name = binding.nameEt.text.toString(),
                spent = binding.spentEt.text.toString(),
                debt = binding.debtEt.text.toString(),
                description = binding.descriptionEt.text.toString()
            )

            // Cria uma Intent de resultado e adiciona a Person como um extra
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_PERSON, person)

            // Define o resultado da atividade como RESULT_OK e envia a Intent de resultado
            setResult(RESULT_OK, resultIntent)

            // Fecha a atividade
            finish()
        }
    }
}
