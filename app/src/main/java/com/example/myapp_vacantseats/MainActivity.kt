package com.example.myapp_vacantseats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

data class StationInfo(
    val name: String,
    val departureTimes: List<Int>
)

data class usersInfo(
    val timeOfHour: Int,
    val timeOfMinutes: Int,
    val currentStaNo: Int,
    val arriveStaNo: Int,
    val currentLineNo: Int,
    val arriveLineNo: Int
)



class MainActivity : AppCompatActivity(), View.OnClickListener {

    var kariList: MutableList<StationInfo> = mutableListOf()
    var kariSyuppatuSta: MutableList<String> = mutableListOf()
    var kariToutyakuSta: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //海岸線の時刻表データ
        var kobeSubwayKaiganToEastWeekdays: MutableList<StationInfo> = readCsv("open_kaigan_w_east.csv")
        var kobeSubwayKaiganToEastWeekends: MutableList<StationInfo> = readCsv("open_kaigan_h_east.csv")
        var kobeSubwayKaiganToWestWeekdays: MutableList<StationInfo> = readCsv("open_kaigan_w_west.csv")
        var kobeSubwayKaiganToWestWeekends: MutableList<StationInfo> = readCsv("open_kaigan_h_west.csv")

        //西神線の時刻表データ
        var kobeSubwaySeishinToEastWeekdays: MutableList<StationInfo> = readCsv("open_seishin_w_east.csv")
        var kobeSubwaySeishinToEastWeekends: MutableList<StationInfo> = readCsv("open_seishin_h_east.csv")
        var kobeSubwaySeishinToWestWeekdays: MutableList<StationInfo> = readCsv("open_seishin_w_west.csv")
        var kobeSubwaySeishinToWestWeekends: MutableList<StationInfo> = readCsv("open_seishin_h_west.csv")


        var buttonSearch = findViewById<Button>(R.id.searchStart)
        buttonSearch.setOnClickListener(this)

    }

    //ここに「検索」ボタンを押されたときの処理を書く
    //画面遷移のコードもここになるはず
    override fun onClick(v: View){
        val a = getInfo()


    }


    //路線データのcsvをkariListにつっこんで行く関数
    fun readCsv(filename: String): MutableList<StationInfo> {
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

        return kariList

    }


    //入力されたデータを取得する関数
    fun getInfo(): List<Int> {
        val currentSta = enterStartSta.text.toString()
        val arriveSta = enterEndSta.text.toString()
        val hour = jikann.text.toString().toInt()
        val minute = hun.text.toString().toInt()

        //海岸線を0、西神線を1にする
        var currentLine = -1
        var arriveLine = -1
        //駅番号をインデックス番号にする
        var currentStaNo = -1
        var arriveStaNo = -1

        val staKaiganList = mutableListOf("新長田", "駒ヶ林", "苅藻", "御崎公園", "和田岬", "中央市場前", "ハーバーランド", "みなと元町", "旧居留地・大丸前", "三宮・花時計前")
        val staSeisinList = mutableListOf("西神中央", "西神南", "伊川谷", "学園都市", "総合運動公園", "名谷", "妙法寺", "板宿", "新長田", "長田", "上沢", "湊川公園", "大倉山", "県庁前", "三宮", "新神戸", "谷上")

        if (staKaiganList.contains(currentSta)){
            currentLine = 0
            currentStaNo = staKaiganList.indexOf(currentSta)
        } else if (staSeisinList.contains(currentSta)){
            currentLine = 1
            currentStaNo = staSeisinList.indexOf(currentSta)
        }

        if (staKaiganList.contains(arriveSta)){
            arriveLine = 0
            currentStaNo = staKaiganList.indexOf(arriveSta)
        } else if (staSeisinList.contains(arriveSta)){
            arriveLine = 1
            currentStaNo = staSeisinList.indexOf(arriveSta)
        }
        val usersInfo = listOf(currentLine, arriveLine, currentStaNo, arriveStaNo, hour, minute)

        return usersInfo
    }















}