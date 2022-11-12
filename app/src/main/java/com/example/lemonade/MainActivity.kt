package com.example.lemonade

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {


    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"

    // SELECT representa o estado "pick lemon"
    private val SELECT = "select"

    // SQUEEZE representa o estado "espremer limão"
    private val SQUEEZE = "squeeze"

    // DRINK representa o estado de "beber limonada"
    private val DRINK = "drink"

    // RESTART representa o estado em que a limonada foi bebida e o copo está vazio
    private val RESTART = "restart"

    // Padrão do estado para selecionar
    private var lemonadeState = "select"

    // Tamanho padrão do limão para -1
    private var lemonSize = -1

    // Padrão para o contador de espremidas -1
    private var squeezeCount = -1

    private var lemonTree = LemonTree()
    private var lemonImage: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // === DO NOT ALTER THE CODE IN THE FOLLOWING IF STATEMENT ===// NÃO FUI EU QUE FIZ ESSE
        if (savedInstanceState != null) {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }
        // === END IF STATEMENT ===

        lemonImage = findViewById(R.id.image_lemon_state) //associa á imagem a uma variavel


        lemonImage!!.setOnClickListener { //transforma a variavel associada a imagem em um botão na pratica
            clickLemonImage();setViewElements()

            // chama o método que muda o estado quando clicado e chama o metodo que muda a imagem e o texto
        }
        lemonImage!!.setOnLongClickListener {
            showSnackbar() //chama o metodo que faz mostrar o contador de espremidas durante SQUEEZE

        }

    }

    /**
     * === DO NOT ALTER THIS METHOD ===
     *
     * Este método salva o estado do aplicativo se for colocado em segundo plano.// Não fui eu que fiz esse
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }


    private fun clickLemonImage() {//metodo que controla a mudança de estado de lemonadeState

        fun espremer() { //função que torna possivel apertar mais de uma vez o limão de SQUEEZE

            if (lemonSize == -1 && squeezeCount == -1) {

                lemonSize = lemonTree.pick()
                squeezeCount = 0
            }

            if (lemonSize > 0) {

                lemonSize--
                squeezeCount++

            } else if (lemonSize == 0) { //depois de ser espremido o tanto certo o limão pode trocar de estado

                lemonadeState = DRINK
                lemonSize = -1
                squeezeCount = -1
            }
        }

        when (lemonadeState) { //estados do limão e o que acontece quando a imagem é clicada em cada um
            SELECT -> lemonadeState = SQUEEZE
            SQUEEZE -> espremer()
            DRINK -> lemonadeState = RESTART
            RESTART -> lemonadeState = SELECT
        }

    }


    private fun setViewElements() { //metodo que identifica o estado atual de lemonadeState e coloca o texto e a imagem correspondentes
        val textAction: TextView = findViewById(R.id.text_action)

        if (lemonadeState == SELECT) {
            lemonImage?.setImageResource(R.drawable.lemon_tree)
            textAction.text = getString(R.string.lemon_select)

        } else if (lemonadeState == SQUEEZE) {
            lemonImage?.setImageResource(R.drawable.lemon_squeeze)
            textAction.text = getString(R.string.lemon_squeeze)

        } else if (lemonadeState == DRINK) {
            lemonImage?.setImageResource(R.drawable.lemon_drink)
            textAction.text = getString(R.string.lemon_drink)

        } else if (lemonadeState == RESTART) {
            lemonImage?.setImageResource(R.drawable.lemon_restart)
            textAction.text = getString(R.string.lemon_empty_glass)

        } else {
            lemonImage?.setImageResource(R.drawable.lemon_tree)
            textAction.text = getString(R.string.lemon_select)
        }

    }


    private fun showSnackbar(): Boolean {//metodo que fazo clique prolongado exibir o numero de cliques em SQUEEZE
        if (lemonadeState != SQUEEZE) {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(findViewById(R.id.constraint_Layout), squeezeText, Snackbar.LENGTH_SHORT)
            .show()
        return true
    }


    class LemonTree { // metodo que aleatoriamente define quantas vezes o limão precisa ser espremido para mudar de estado
        fun pick(): Int {
            return (2..4).random()
        }
    }
}

