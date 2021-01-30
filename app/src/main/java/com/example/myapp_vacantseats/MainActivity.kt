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
            val inputArriveSta = findViewById<EditText>(R.id.enterEndSta)
            val arriveSta = inputArriveSta.text.toString()
            val inputHour = findViewById<EditText>(R.id.jikann)
            val hour = inputHour.text.toString().toInt()
            val inputMinute = findViewById<EditText>(R.id.nanhun)
            val minute = inputMinute.text.toString().toInt()

        }
    }

}