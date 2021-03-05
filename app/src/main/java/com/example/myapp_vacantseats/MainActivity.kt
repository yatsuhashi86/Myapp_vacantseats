package com.example.myapp_vacantseats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

data class StationInfo(
    val name: String,
    val departureTimes: List<Int>
)



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var syuppatuSta = emptyArray<String>()
        var toutyakuSta = emptyArray<String>()

        val kobeSubwayKaiganToEast: MutableList<StationInfo> = mutableListOf()



    }



    fun readCsv(filename: String, list: MutableList<StationInfo>, syuppatuSta: MutableList<String>, toutyakuSta: MutableList<String>){
        try{
            val file = resources.assets.open(filename)
            val fileReader = BufferedReader(InputStreamReader(file))
            var i = 0
            fileReader.forEachLine {
                if (it.isNotBlank()){
                    if (i == 0){
                        val x = it.split(",").toTypedArray()
                        x.forEach {
                            syuppatuSta.add(it)
                        }
                    } else if (i == 1){
                        val x = it.split(",").toTypedArray()
                        x.forEach {
                            toutyakuSta.add(it)
                        }
                    } else {
                        val line = it.split(",")
                        val x = line[0]
                        val y = line.drop(1).map { it.toInt() }
                        val addInfo = StationInfo(name = x, departureTimes = y)
                        list.add(addInfo)
                    }
                }
                i++
            }
        } catch (e: IOException){
            println(e)
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