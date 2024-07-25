package com.example.vamosrachar_512130jasminemendes

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var people: EditText
    private lateinit var value: EditText
    private lateinit var result: TextView
    private lateinit var tts: TextToSpeech
//    private lateinit var resultTextView: TextView
//    private lateinit var shareButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        people = findViewById(R.id.people)
        value = findViewById(R.id.value)
        result = findViewById(R.id.result)
        tts = TextToSpeech(this, this) // Inicializa o TTS

        people.addTextChangedListener(textWatcher)
        value.addTextChangedListener(textWatcher)

//        shareButton.setOnClickListener {
//            shareResult()
//        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // val result = tts.setLanguage(Locale.getDefault())
            val result = tts.setLanguage(Locale.JAPANESE)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Lidar com o erro se o idioma não for suportado
                println("Idioma não suportado")
            }
        } else {
            // Lidar com a inicialização falhada do TTS
            println("Inicialização do TTS falhou")
        }
    }

    private fun calc() {
        val peopleStr = people.text.toString()
        val valueStr = value.text.toString()

        if (peopleStr.isNotEmpty() && valueStr.isNotEmpty()) {
            try {
                val peopleVl = peopleStr.toDouble()
                val valueVl = valueStr.toDouble()
                val resultVl = valueVl / peopleVl
                val resultMsg = resultVl.toString()
                result.text = resultMsg
                speakResult(resultMsg) // Chama a função para falar o resultado
                println(resultMsg)
            } catch (e: NumberFormatException) {
                result.text = "Entrada inválida"
            }
        }
    }

    private fun speakResult(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

//    private fun shareResult() {
//        val resultText = resultTextView.text.toString()
//
//        val sendIntent: Intent = Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_TEXT, resultText)
//            type = "text/plain"
//        }
//
//        val shareIntent = Intent.createChooser(sendIntent, null)
//        startActivity(shareIntent)
//    }


    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Não utilizado
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            calc() // Chama a função calc() quando o texto é alterado
        }


        override fun afterTextChanged(s: Editable?) {
            // Não utilizado
        }
    }


    override fun onDestroy() {
        // Encerrar o TTS quando a atividade for destruída
        if (this::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
