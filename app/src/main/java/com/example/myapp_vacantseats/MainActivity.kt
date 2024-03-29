package com.example.myapp_vacantseats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

data class StationInfo(
    val name: String,
    val departureTimes: List<Int>
)

data class usersInfo(
    val time: Int,
    val currentStaNo: Int,
    val arriveStaNo: Int,
    val currentLineNo: Int,
    val arriveLineNo: Int
)

//二画面目にもデータはいるくない？？三画面目まであるんやで
data class useData(
    val useLines: MutableList<Int>,
    val useStations: MutableList<Int>
)

//二画面目に持っていくデータを格納するdata classを作る
data class secondScreenInfo (
    val transferSta: MutableList<String>,
    val transferTime: MutableList<Int>,
    val useLine: MutableList<String>,
    val useData: useData
)


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //海岸線の時刻表データ
        //dataOfTimeTableの最初のlistのindexは0
        val kobeSubwayKaiganToEastWeekdays: MutableList<StationInfo> = readCsv("open_kaigan_w_east.csv")
        val kobeSubwayKaiganToEastWeekends: MutableList<StationInfo> = readCsv("open_kaigan_h_east.csv")
        val kobeSubwayKaiganToWestWeekdays: MutableList<StationInfo> = readCsv("open_kaigan_w_west.csv")
        val kobeSubwayKaiganToWestWeekends: MutableList<StationInfo> = readCsv("open_kaigan_h_west.csv")

        val kaiganData = mutableListOf(kobeSubwayKaiganToEastWeekdays, kobeSubwayKaiganToEastWeekends, kobeSubwayKaiganToWestWeekdays, kobeSubwayKaiganToWestWeekends)

        //西神線の時刻表データ
        //dataOfTimeTableの最初のlistのindexは1
        val kobeSubwaySeishinToEastWeekdays: MutableList<StationInfo> = readCsv("open_seishin_w_east.csv")
        val kobeSubwaySeishinToEastWeekends: MutableList<StationInfo> = readCsv("open_seishin_h_east.csv")
        val kobeSubwaySeishinToWestWeekdays: MutableList<StationInfo> = readCsv("open_seishin_w_west.csv")
        val kobeSubwaySeishinToWestWeekends: MutableList<StationInfo> = readCsv("open_seishin_h_west.csv")

        val seishinData = mutableListOf(kobeSubwaySeishinToEastWeekdays, kobeSubwaySeishinToEastWeekends, kobeSubwaySeishinToWestWeekdays, kobeSubwaySeishinToWestWeekends)

        val dataOfTimeTable = mutableListOf<MutableList<MutableList<StationInfo>>>()
        dataOfTimeTable.add(kaiganData)
        dataOfTimeTable.add(seishinData) //時刻表データを一つのlistに格納した。たぶんダメ。三次元配列になった。

        var isItArrive = -1 //検索が出発時刻でのものか到着時刻のものなのか判定
        var whatDay = -1 //平日か土休日か

        fun onRadioButtonClicked(view: View) {
            if (view is RadioButton) {
                val checked = view.isChecked
                when (view.getId()) {
                    R.id.startTimeOfButton->
                        if (checked) {
                            isItArrive = 0//出発時刻なら0
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
        val RadioButtonOfWeekDayOrWeekEnd = findViewById<RadioGroup>(R.id.weekDayOrWeekEnd)
        onRadioButtonClicked(RadioButtonOfWeekDayOrWeekEnd)

        val buttonSearch = findViewById<Button>(R.id.searchStart)
        //たぶんアルゴリズムもここ
        buttonSearch.setOnClickListener{
            //todo
            //ここに画面遷移の実装をする
            val info = getInfo()
            val stationsList = mutableListOf<Int>()
            stationsList.add(info.currentStaNo)
            val directAndDay = mutableListOf<Int>()//二つ目のindex
            val linesList = mutableListOf<Int>()
            linesList.add(info.currentLineNo)//一つ目のindex
            //どの時刻表データを使うかの選定
            if (info.currentLineNo == info.arriveLineNo){ //出発駅と到着駅が同じ路線
                directAndDay.add(decideSecondIndex(whatDay, info.currentStaNo, info.arriveStaNo))
                stationsList.add(info.arriveStaNo)
            } else {
                //todo
                //ここは二路線のうちはこれでいいけど拡張したらダイクストラを使ったコードにする必要がある。
                //ここマジでヤバイ。乗り換え駅が新長田しかない前提、やばすぎ
                if (info.currentLineNo == 0 && info.currentStaNo == 0){ //新長田駅は西神線でもあるから目的駅が西神線ならそっちにする
                    directAndDay.add(decideSecondIndex(whatDay, 8, info.arriveStaNo))
                    linesList.add(info.arriveLineNo)
                    stationsList.drop(0)
                    stationsList.add(8)
                    stationsList.add(info.arriveStaNo)
                } else if (info.currentLineNo == 0){
                    directAndDay.add(decideSecondIndex(whatDay, info.currentStaNo, 0))
                    directAndDay.add(decideSecondIndex(whatDay, 8, info.arriveStaNo))
                    linesList.add(info.arriveLineNo)
                    stationsList.add(0)
                    stationsList.add(8)
                    stationsList.add(info.arriveStaNo)
                } else {
                    directAndDay.add(decideSecondIndex(whatDay, info.currentStaNo, 8))
                    directAndDay.add(decideSecondIndex(whatDay, 0, info.arriveStaNo))
                    linesList.add(info.arriveLineNo)
                    stationsList.add(8)
                    stationsList.add(0)
                    stationsList.add(info.arriveStaNo)
                }
            }
            //todo
            //時刻表はもう確定してる
            //時間から最適な電車を探す
            val timesList = decideTrain(info.time, isItArrive, dataOfTimeTable, stationsList, linesList, directAndDay)//これがsecondScreenInfoのtransferTime
            val stationsName = mutableListOf<String>() //文字列の情報がgetInfoにしかないことに今気づいた。secondInfoのtransferTime
            val linesName = mutableListOf<String>() //secondInfoのuseLineやけど、ごり押し。路線増えたらどうするねん
            val usedata = useData(linesList, stationsList)

            val staKaiganList = mutableListOf("新長田", "駒ヶ林", "苅藻", "御崎公園", "和田岬", "中央市場前", "ハーバーランド", "みなと元町", "旧居留地・大丸前", "三宮・花時計前")
            val staSeisinList = mutableListOf("西神中央", "西神南", "伊川谷", "学園都市", "総合運動公園", "名谷", "妙法寺", "板宿", "新長田", "長田", "上沢", "湊川公園", "大倉山", "県庁前", "三宮", "新神戸", "谷上")
            var flag = 0
            for (i in linesList){
                if (i == 0){
                    linesName.add("神戸市営地下鉄海岸線")
                    stationsName.add(staKaiganList[stationsList[flag]])
                    flag++
                    stationsName.add(staKaiganList[stationsList[flag]])
                } else if (i == 1) {
                    linesName.add("神戸市営地下鉄西神線")
                    stationsName.add(staSeisinList[stationsList[flag]])
                    flag++
                    stationsName.add(staSeisinList[stationsList[flag]])
                }
                flag++
            }
            //これで画面遷移に必要なデータがそろった
            //神戸市営地下鉄しか使えない酷いコード
            val toSecond = secondScreenInfo(stationsName, timesList, linesName, usedata)
        }
    }

    //出発駅のインデックスと到着駅のインデックスから判断。乗り換えあるなら路線を合わせる必要あり
    private fun decideSecondIndex(whatDay: Int, currentStaNo: Int, arriveStaNo: Int): Int {
        var secondIndex = -1
        if (whatDay == 0 && currentStaNo < arriveStaNo){
            secondIndex = 0
        } else if (whatDay == 1 && currentStaNo < arriveStaNo){
            secondIndex = 1
        } else if (whatDay == 0 && currentStaNo > arriveStaNo){
            secondIndex = 2
        } else if (whatDay == 1 && currentStaNo > arriveStaNo){
            secondIndex = 3
        }
        return secondIndex
    }

    //時刻表データからどの電車にのるか判断する
    //dataOfTimeTableの三つ目のindexをlistにしたものを返す
    //firstIndexには路線情報、secondIndexには向きと曜日の情報、stationsには乗り降りする駅の情報が入る。
    //乗り降りする駅の数は路線の倍あるからfirstIndexｔとsecondIndexは半分にする
    private fun decideTrain(time: Int, currentOrArrive: Int, timeTable: MutableList<MutableList<MutableList<StationInfo>>>, stations: MutableList<Int>,
                            firstIndex: MutableList<Int>, secondIndex: MutableList<Int>): MutableList<Int>{
        val returnIndex = mutableListOf<Int>()
        var timeOfTrain = time
        val numberOfTrains = timeTable.size
        val numberOfToRide = stations.size
        if (currentOrArrive == 0){
            for (i in 0 until numberOfToRide step 2){
                for (j in 0 until numberOfTrains){
                    if (timeTable[firstIndex[i/2]][secondIndex[i/2]][j].departureTimes[stations[i]] > timeOfTrain){
                        returnIndex.add(timeTable[firstIndex[i/2]][secondIndex[i/2]][j].departureTimes[stations[i]])
                        timeOfTrain = timeTable[firstIndex[i/2]][secondIndex[i/2]][j].departureTimes[stations[i+1]] //到着駅（乗換駅）の時間に変えた
                        returnIndex.add(timeOfTrain)
                    }
                }
            }
        } else {
            for (i in numberOfToRide downTo 0 step 2){
                for (j in numberOfTrains downTo 0){
                    if (timeTable[firstIndex[i/2]][secondIndex[i/2]][j].departureTimes[stations[i]] < timeOfTrain){
                        returnIndex.add(timeTable[firstIndex[i/2]][secondIndex[i/2]][j].departureTimes[stations[i]])
                        timeOfTrain = timeTable[firstIndex[i/2]][secondIndex[i/2]][j].departureTimes[stations[i-1]] //到着駅（乗換駅）の時間に変えた
                        returnIndex.add(timeOfTrain)
                    }
                }
            }
        }
        return returnIndex
    }






    //路線データのcsvをkariListにつっこんで行く関数
    private fun readCsv(filename: String): MutableList<StationInfo> {
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
    private fun getInfo(): usersInfo {
        val currentSta = enterStartSta.text.toString()
        val arriveSta = enterEndSta.text.toString()
        val hour = jikann.text.toString().toInt()
        val minute = hun.text.toString().toInt()
        val time = (hour.toString() + minute.toString()).toInt()



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

        return usersInfo(
            time = time,
            currentStaNo = currentStaNo,
            arriveStaNo = arriveStaNo,
            currentLineNo = currentLine,
            arriveLineNo = arriveLine
        )
    }
}
