package com.example.myapp_vacantseats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.time.DayOfWeek

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



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //海岸線の時刻表データ
        val kobeSubwayKaiganToEastWeekdays: MutableList<StationInfo> = readCsv("open_kaigan_w_east.csv")
        val kobeSubwayKaiganToEastWeekends: MutableList<StationInfo> = readCsv("open_kaigan_h_east.csv")
        val kobeSubwayKaiganToWestWeekdays: MutableList<StationInfo> = readCsv("open_kaigan_w_west.csv")
        val kobeSubwayKaiganToWestWeekends: MutableList<StationInfo> = readCsv("open_kaigan_h_west.csv")

        val kaiganData = mutableListOf(kobeSubwayKaiganToEastWeekdays, kobeSubwayKaiganToEastWeekends, kobeSubwayKaiganToWestWeekdays, kobeSubwayKaiganToWestWeekends)

        //西神線の時刻表データ
        val kobeSubwaySeishinToEastWeekdays: MutableList<StationInfo> = readCsv("open_seishin_w_east.csv")
        val kobeSubwaySeishinToEastWeekends: MutableList<StationInfo> = readCsv("open_seishin_h_east.csv")
        val kobeSubwaySeishinToWestWeekdays: MutableList<StationInfo> = readCsv("open_seishin_w_west.csv")
        val kobeSubwaySeishinToWestWeekends: MutableList<StationInfo> = readCsv("open_seishin_h_west.csv")

        val seishinData = mutableListOf(kobeSubwaySeishinToEastWeekdays, kobeSubwaySeishinToEastWeekends, kobeSubwaySeishinToWestWeekdays, kobeSubwaySeishinToWestWeekends)

        val dataOfTimeTable = mutableListOf<MutableList<MutableList<StationInfo>>>()
        dataOfTimeTable.add(kaiganData)
        dataOfTimeTable.add(seishinData) //時刻表データを一つのlistに格納した。たぶんダメ。三次元配列になった。

        var isItArrive = -1
        var whatDay = -1

        fun onRadioButtonClicked(view: View) {
            if (view is RadioButton) {
                val checked = view.isChecked
                when (view.getId()) {
                    R.id.startTimeOfButton->
                        if (checked) {
                            isItArrive = 0
                        }
                    R.id.endTimeOfButton->
                        if (checked) {
                            isItArrive = 1
                        }
                    R.id.buttonOfWeekDay ->
                        if (checked){
                            whatDay = 0 //平日を0に
                        }
                    R.id.buttonOfWeekEnd ->
                        if (checked){
                            whatDay = 1 //土休日を1に
                        }
                }
            }
        }

        val RadioButtonOfArriveOrCurrent = findViewById<RadioGroup>(R.id.arriveOrCurrent)
        onRadioButtonClicked(RadioButtonOfArriveOrCurrent)
        val RadioButtonOfWeelDayOrWeekEnd = findViewById<RadioGroup>(R.id.weekDayOrWeekEnd)
        onRadioButtonClicked(RadioButtonOfWeelDayOrWeekEnd)

        val buttonSearch = findViewById<Button>(R.id.searchStart)
        buttonSearch.setOnClickListener{
            //todo
            //ここに画面遷移の実装をする
            //たぶんアルゴリズムもここ
            val info = getInfo()
            if (info.currentLineNo == info.arriveLineNo){ //出発駅と到着駅が同じ路線

            }


        }
    }





    //路線データのcsvをkariListにつっこんで行く関数
    fun readCsv(filename: String): MutableList<StationInfo> {
        val kariList = mutableListOf<StationInfo>()
        val kariSyuppatuSta = mutableListOf<String>()
        val kariToutyakuSta = mutableListOf<String>()

        try {
            val fileReader = BufferedReader(InputStreamReader(resources.assets.open(filename)))
            fileReader.readLine()!!.split(",").forEach { kariSyuppatuSta.add(it) }
            fileReader.readLine()!!.split(",").forEach { kariToutyakuSta.add(it) }
            fileReader.forEachLine { line ->
                val split = line.split(",")
                kariList.add(StationInfo(
                    name = split[0],
                    departureTimes = split.drop(1).map { it.toInt() }
                ))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return kariList
    }

    //入力されたデータを取得する関数
    fun getInfo(): usersInfo {
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

        if (staKaiganList.contains(currentSta)) {
            currentLine = 0
            currentStaNo = staKaiganList.indexOf(currentSta)
        } else if (staSeisinList.contains(currentSta)) {
            currentLine = 1
            currentStaNo = staSeisinList.indexOf(currentSta)
        }

        if (staKaiganList.contains(arriveSta)) {
            arriveLine = 0
            arriveStaNo = staKaiganList.indexOf(arriveSta)
        } else if (staSeisinList.contains(arriveSta)) {
            arriveLine = 1
            arriveStaNo = staSeisinList.indexOf(arriveSta)
        }







        val returnInfo = usersInfo(
            timeOfHour = hour,
            timeOfMinutes = minute,
            currentStaNo = currentStaNo,
            arriveStaNo = arriveStaNo,
            currentLineNo = currentLine,
            arriveLineNo = arriveLine
        )

        return returnInfo
    }
}
