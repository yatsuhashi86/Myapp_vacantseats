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

    var kariList: MutableList<StationInfo> = mutableListOf()
    var kariSyuppatuSta: MutableList<String> = mutableListOf()
    var kariToutyakuSta: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        //海岸線の時刻表データ
        var kobeSubwayKaiganToEastWeekdays: MutableList<StationInfo> = mutableListOf()
        var kobeSubwayKaiganToEastWeekends: MutableList<StationInfo> = mutableListOf()
        var kobeSubwayKaiganToWestWeekdays: MutableList<StationInfo> = mutableListOf()
        var kobeSubwayKaiganToWestWeekends: MutableList<StationInfo> = mutableListOf()

        //西神線の時刻表データ
        var kobeSubwaySeishinToEastWeekdays: MutableList<StationInfo> = mutableListOf()
        var kobeSubwaySeishinToEastWeekends: MutableList<StationInfo> = mutableListOf()
        var kobeSubwaySeishinToWestWeekdays: MutableList<StationInfo> = mutableListOf()
        var kobeSubwaySeishinToWestWeekends: MutableList<StationInfo> = mutableListOf()

        //海岸線の週末ダイヤの時刻表データを入れた。hが週末なので注意
        readCsv("open_kaigan_h_east.csv")
        kobeSubwayKaiganToEastWeekends = kariList


    }

    //路線データのcsvをkariListにつっこんで行く関数
    fun readCsv(filename: String){
        kariList = mutableListOf()
        kariSyuppatuSta = mutableListOf()
        kariToutyakuSta = mutableListOf()
        try{
            val file = resources.assets.open(filename)
            val fileReader = BufferedReader(InputStreamReader(file))
            var i = 0
            fileReader.forEachLine {
                if (it.isNotBlank()){
                    if (i == 0){
                        val x = it.split(",").toTypedArray()
                        x.forEach {
                            kariSyuppatuSta.add(it)
                        }
                    } else if (i == 1){
                        val x = it.split(",").toTypedArray()
                        x.forEach {
                            kariToutyakuSta.add(it)
                        }
                    } else {
                        val line = it.split(",")
                        val x = line[0]
                        val y = line.drop(1).map { it.toInt() }
                        val addInfo = StationInfo(name = x, departureTimes = y)
                        kariList.add(addInfo)
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