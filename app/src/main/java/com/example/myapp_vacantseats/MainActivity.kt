package com.example.myapp_vacantseats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var syuppatuSta = emptyArray<String>()
        var toutyakuSta = emptyArray<String>()
        val koubeSubwayKaiganToEast = MutableList(100){ MutableList(10){ -1 } }
        fun readCsv(filename: String, list: MutableList<MutableList<Int>>){
            try{
                val file = resources.assets.open(filename)
                val fileReader = BufferedReader(InputStreamReader(file))
                var i = 0
                fileReader.forEachLine {
                    if (it.isNotBlank()){
                        if (i == 0){
                            syuppatuSta = it.split(",").toTypedArray()
                        } else if (i == 1){
                            toutyakuSta = it.split(",").toTypedArray()
                        } else {
                            val line = it.split(",").map { it.toInt() }.toMutableList()
                            list[i] = line
                        }
                    }
                    i++
                }
            } catch (e: IOException){
                println(e)
            }

        }

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