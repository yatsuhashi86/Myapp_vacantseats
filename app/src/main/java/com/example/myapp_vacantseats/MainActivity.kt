package com.example.myapp_vacantseats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private inner class TextListener : View.OnClickListener {
        override fun onClick(view: View) {
            val inputCurrentSta = findViewById<EditText>(R.id.enterStartSta)
            val currentSta = inputCurrentSta.text.toString()

        }
    }

}