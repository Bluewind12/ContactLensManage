package momonyan.contactlensmanage.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.tab_main.view.*
import me.mattak.moment.Moment
import me.mattak.moment.minus
import momonyan.contactlensmanage.AlarmPush
import momonyan.contactlensmanage.R
import java.util.*

class TabMain : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var v: View

    val nowCalendar = Calendar.getInstance()
    val nowYear = nowCalendar.get(Calendar.YEAR)
    val nowMonthOfYear = nowCalendar.get(Calendar.MONTH) + 1
    val nowDayOfMonth = nowCalendar.get(Calendar.DAY_OF_MONTH)

    var fragLR = false
    var editFrag = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.tab_main, container, false)
        sharedPreferences = activity!!.getSharedPreferences("Data", Context.MODE_PRIVATE)

        //初期セット
        fragLR = true
        setLimit()
        fragLR = false
        setLimit()

        editFrag = true

        //日付設定時のリスナ作成
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            AlertDialog.Builder(activity)
                .setTitle("コンタクトレンズ 更新")
                .setMessage("更新してもよろしいですか？")
                .setPositiveButton("OK") { _, _ ->
                    //ログ出力
                    Log.d("DatePicker", "year:$year monthOfYear:$monthOfYear dayOfMonth:$dayOfMonth")
                    val edit = sharedPreferences.edit()

                    if (fragLR) {
                        v.setTimeText.text = getString(R.string.set_time, nowMonthOfYear, nowDayOfMonth)
                        v.minuteTimeText.text = getString(R.string.now_time2, monthOfYear + 1, dayOfMonth)

                        //期限日
                        edit.putInt("Year", year)
                        edit.putInt("Month", monthOfYear + 1)
                        edit.putInt("Day", dayOfMonth)

                        //設定日
                        edit.putInt("SetYear", nowYear)
                        edit.putInt("SetMonth", nowMonthOfYear)
                        edit.putInt("SetDay", nowDayOfMonth)
                    } else {
                        v.setTimeTextR.text = getString(R.string.set_time, nowMonthOfYear, nowDayOfMonth)
                        v.minuteTimeTextR.text = getString(R.string.now_time2, monthOfYear + 1, dayOfMonth)

                        //期限日
                        edit.putInt("Year2", year)
                        edit.putInt("Month2", monthOfYear + 1)
                        edit.putInt("Day2", dayOfMonth)

                        //設定日
                        edit.putInt("SetYear2", nowYear)
                        edit.putInt("SetMonth2", nowMonthOfYear)
                        edit.putInt("SetDay2", nowDayOfMonth)

                    }
                    //保存
                    edit.apply()

                    //更新
                    setLimit()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        v.timeSetButton.setOnClickListener {
            fragLR = true
            when (sharedPreferences.getString("auto_setting", "not")) {
                "2Week" -> {
                    AlertDialog.Builder(activity)
                        .setTitle("コンタクトレンズ 更新")
                        .setMessage("2Week\n更新してもよろしいですか？")
                        .setPositiveButton("OK") { _, _ ->
                            val stackCalendar = Calendar.getInstance()
                            stackCalendar.add(Calendar.DAY_OF_MONTH, 14)
                            val addYear = stackCalendar.get(Calendar.YEAR)
                            val addMonth = stackCalendar.get(Calendar.MONTH)
                            val addDay = stackCalendar.get(Calendar.DAY_OF_MONTH)

                            v.setTimeText.text = getString(R.string.set_time, nowMonthOfYear, nowDayOfMonth)
                            v.minuteTimeText.text = getString(R.string.now_time2, addMonth + 1, addDay)
                            val edit = sharedPreferences.edit()

                            //期限日
                            edit.putInt("Year", addYear)
                            edit.putInt("Month", addMonth + 1)
                            edit.putInt("Day", addDay)

                            //設定日
                            edit.putInt("SetYear", nowYear)
                            edit.putInt("SetMonth", nowMonthOfYear)
                            edit.putInt("SetDay", nowDayOfMonth)

                            //保存
                            edit.apply()

                            setLimit()
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
                "1Month" -> {
                    AlertDialog.Builder(activity)
                        .setTitle("コンタクトレンズ 更新")
                        .setMessage("1Month\n更新してもよろしいですか？")
                        .setPositiveButton("OK") { _, _ ->
                            val stackCalendar = Calendar.getInstance()
                            stackCalendar.add(Calendar.DAY_OF_MONTH, 14)
                            val addYear = stackCalendar.get(Calendar.YEAR)
                            val addMonth = stackCalendar.get(Calendar.MONTH)
                            val addDay = stackCalendar.get(Calendar.DAY_OF_MONTH)

                            v.setTimeText.text = getString(R.string.set_time, nowMonthOfYear, nowDayOfMonth)
                            v.minuteTimeText.text = getString(R.string.now_time2, addMonth + 1, addDay)
                            val edit = sharedPreferences.edit()

                            //期限日
                            edit.putInt("Year", addYear)
                            edit.putInt("Month", addMonth + 1)
                            edit.putInt("Day", addDay)

                            //設定日
                            edit.putInt("SetYear", nowYear)
                            edit.putInt("SetMonth", nowMonthOfYear)
                            edit.putInt("SetDay", nowDayOfMonth)

                            //保存
                            edit.apply()

                            setLimit()
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
                else -> {
                    //日付情報の初期設定
                    val calendar = Calendar.getInstance()
                    val year = calendar.get(Calendar.YEAR)
                    val monthOfYear = calendar.get(Calendar.MONTH)
                    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

                    //日付設定ダイアログの作成
                    datePickerDialog = DatePickerDialog(context!!, dateSetListener, year, monthOfYear, dayOfMonth)

                    //日付設定ダイアログの表示
                    datePickerDialog.show()
                }
            }
        }
        v.lSettingButton.setOnClickListener {
            fragLR = true
            when (sharedPreferences.getString("auto_setting", "not")) {
                "2Week" -> {
                    AlertDialog.Builder(activity)
                        .setTitle("コンタクトレンズ 更新")
                        .setMessage("2Week\n更新してもよろしいですか？")
                        .setPositiveButton("OK") { _, _ ->
                            val stackCalendar = Calendar.getInstance()
                            stackCalendar.add(Calendar.DAY_OF_MONTH, 14)
                            val addYear = stackCalendar.get(Calendar.YEAR)
                            val addMonth = stackCalendar.get(Calendar.MONTH)
                            val addDay = stackCalendar.get(Calendar.DAY_OF_MONTH)

                            v.setTimeText.text = getString(R.string.set_time, nowMonthOfYear, nowDayOfMonth)
                            v.minuteTimeText.text = getString(R.string.now_time2, addMonth + 1, addDay)
                            val edit = sharedPreferences.edit()

                            //期限日
                            edit.putInt("Year", addYear)
                            edit.putInt("Month", addMonth + 1)
                            edit.putInt("Day", addDay)

                            //設定日
                            edit.putInt("SetYear", nowYear)
                            edit.putInt("SetMonth", nowMonthOfYear)
                            edit.putInt("SetDay", nowDayOfMonth)

                            //保存
                            edit.apply()

                            setLimit()
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
                "1Month" -> {
                    AlertDialog.Builder(activity)
                        .setTitle("コンタクトレンズ 更新")
                        .setMessage("1Month\n更新してもよろしいですか？")
                        .setPositiveButton("OK") { _, _ ->
                            val stackCalendar = Calendar.getInstance()
                            stackCalendar.add(Calendar.DAY_OF_MONTH, 14)
                            val addYear = stackCalendar.get(Calendar.YEAR)
                            val addMonth = stackCalendar.get(Calendar.MONTH)
                            val addDay = stackCalendar.get(Calendar.DAY_OF_MONTH)

                            v.setTimeText.text = getString(R.string.set_time, nowMonthOfYear, nowDayOfMonth)
                            v.minuteTimeText.text = getString(R.string.now_time2, addMonth + 1, addDay)
                            val edit = sharedPreferences.edit()

                            //期限日
                            edit.putInt("Year", addYear)
                            edit.putInt("Month", addMonth + 1)
                            edit.putInt("Day", addDay)

                            //設定日
                            edit.putInt("SetYear", nowYear)
                            edit.putInt("SetMonth", nowMonthOfYear)
                            edit.putInt("SetDay", nowDayOfMonth)

                            //保存
                            edit.apply()

                            setLimit()
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
                else -> {
                    //日付情報の初期設定
                    val calendar = Calendar.getInstance()
                    val year = calendar.get(Calendar.YEAR)
                    val monthOfYear = calendar.get(Calendar.MONTH)
                    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

                    //日付設定ダイアログの作成
                    datePickerDialog = DatePickerDialog(context!!, dateSetListener, year, monthOfYear, dayOfMonth)

                    //日付設定ダイアログの表示
                    datePickerDialog.show()
                }
            }
        }
        v.rSettingButton.setOnClickListener {
            fragLR = false
            when (sharedPreferences.getString("auto_setting", "not")) {
                "2Week" -> {
                    AlertDialog.Builder(activity)
                        .setTitle("コンタクトレンズ 更新")
                        .setMessage("2Week\n更新してもよろしいですか？")
                        .setPositiveButton("OK") { _, _ ->
                            val stackCalendar = Calendar.getInstance()
                            stackCalendar.add(Calendar.DAY_OF_MONTH, 14)
                            val addYear = stackCalendar.get(Calendar.YEAR)
                            val addMonth = stackCalendar.get(Calendar.MONTH)
                            val addDay = stackCalendar.get(Calendar.DAY_OF_MONTH)

                            v.setTimeTextR.text = getString(R.string.set_time, nowMonthOfYear, nowDayOfMonth)
                            v.minuteTimeTextR.text = getString(R.string.now_time2, addMonth + 1, addDay)
                            val edit = sharedPreferences.edit()

                            //期限日
                            edit.putInt("Year2", addYear)
                            edit.putInt("Month2", addMonth + 1)
                            edit.putInt("Day2", addDay)

                            //設定日
                            edit.putInt("SetYear2", nowYear)
                            edit.putInt("SetMonth2", nowMonthOfYear)
                            edit.putInt("SetDay2", nowDayOfMonth)

                            //保存
                            edit.apply()

                            setLimit()
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
                "1Month" -> {
                    AlertDialog.Builder(activity)
                        .setTitle("コンタクトレンズ 更新")
                        .setMessage("1Month\n更新してもよろしいですか？")
                        .setPositiveButton("OK") { _, _ ->
                            val stackCalendar = Calendar.getInstance()
                            stackCalendar.add(Calendar.DAY_OF_MONTH, 14)
                            val addYear = stackCalendar.get(Calendar.YEAR)
                            val addMonth = stackCalendar.get(Calendar.MONTH)
                            val addDay = stackCalendar.get(Calendar.DAY_OF_MONTH)

                            v.setTimeText.text = getString(R.string.set_time, nowMonthOfYear, nowDayOfMonth)
                            v.minuteTimeText.text = getString(R.string.now_time2, addMonth + 1, addDay)
                            val edit = sharedPreferences.edit()

                            //期限日
                            edit.putInt("Year2", addYear)
                            edit.putInt("Month2", addMonth + 1)
                            edit.putInt("Day2", addDay)

                            //設定日
                            edit.putInt("SetYear2", nowYear)
                            edit.putInt("SetMonth2", nowMonthOfYear)
                            edit.putInt("SetDay2", nowDayOfMonth)

                            //保存
                            edit.apply()

                            setLimit()
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
                else -> {
                    //日付情報の初期設定
                    val calendar = Calendar.getInstance()
                    val year = calendar.get(Calendar.YEAR)
                    val monthOfYear = calendar.get(Calendar.MONTH)
                    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

                    //日付設定ダイアログの作成
                    datePickerDialog = DatePickerDialog(context!!, dateSetListener, year, monthOfYear, dayOfMonth)

                    //日付設定ダイアログの表示
                    datePickerDialog.show()
                }
            }
        }

        if (sharedPreferences.getBoolean("LRSetting", false)) {
            v.limitDay1.visibility = View.VISIBLE
            v.setTimeTextR.visibility = View.VISIBLE
            v.imageView.setImageResource(R.drawable.l_icon)
            v.timeSetButton.visibility = View.GONE
            v.lSettingButton.visibility = View.VISIBLE
            v.rSettingButton.visibility = View.VISIBLE
        } else {
            v.limitDay1.visibility = View.GONE
            v.setTimeTextR.visibility = View.GONE
            v.imageView.setImageResource(R.drawable.lr_icon)
            v.timeSetButton.visibility = View.VISIBLE
            v.lSettingButton.visibility = View.GONE
            v.rSettingButton.visibility = View.GONE
        }

        return v
    }


    private fun setLimit() {
        val year: Int
        val month: Int
        val day: Int
        val limitSetText: TextView
        val many: TextView
        val setTime: TextView
        val calendar = Calendar.getInstance()

        //設定日の取得
        if (fragLR) {
            limitSetText = v.minuteTimeText
            many = v.manyText
            setTime = v.setTimeText
            year = sharedPreferences.getInt("Year", 0)
            month = sharedPreferences.getInt("Month", 0)
            day = sharedPreferences.getInt("Day", 0)
        } else {
            limitSetText = v.minuteTimeTextR
            many = v.manyTextR
            setTime = v.setTimeTextR
            year = sharedPreferences.getInt("Year2", 0)
            month = sharedPreferences.getInt("Month2", 0)
            day = sharedPreferences.getInt("Day2", 0)
        }
        //色戻し
        many.setTextColor(resources.getColor(R.color.default_text))

        //カレンダーの設定
        calendar.set(year, month - 1, day, 0, 0, 0)
        val nowMoment = Moment() // 現在の日時
        val setDateMoment = Moment(calendar.time, TimeZone.getDefault(), Locale.JAPAN) // 現在の日時
        Log.d("TEST_TAG", "${nowMoment.format("yyyy年MM月dd日 HH時mm分ss秒　タイムゾーン：ZZZZ")}")
        Log.d("TEST_TAG", "${setDateMoment.format("yyyy年MM月dd日 HH時mm分ss秒　タイムゾーン：ZZZZ")}")
        if (month > 0 && day > 0 && year > 0) {
            limitSetText.text = getString(R.string.now_time2, month, day)
            if (fragLR) {
                setTime.text = getString(
                    R.string.set_time,
                    sharedPreferences.getInt("SetMonth", 0),
                    sharedPreferences.getInt("SetDay", 0)
                )
            } else {
                setTime.text = getString(
                    R.string.set_time,
                    sharedPreferences.getInt("SetMonth2", 0),
                    sharedPreferences.getInt("SetDay2", 0)
                )
            }

            if (nowMoment > setDateMoment) {
                many.text = getString(R.string.limit)
            } else {
                val limit = setDateMoment.minus(nowMoment)
                val limitMonth = ((limit.monthsFloor) + (limit.yearsFloor * 12)).toInt()
                val limitDay = limit.daysFloorUnit.toInt() + 1

                if (limitMonth > 0) {
                    many.text = getString(R.string.limit_month_days, limitMonth, limitDay)
                } else {
                    many.text = getString(R.string.limit_days, limitDay)
                    if (limitDay <= 3) {
                        many.setTextColor(resources.getColor(R.color.red))
                    }
                }
                Log.e("CHECK2", "$limitDay $limitMonth")
            }

        } else {
            limitSetText.text = getString(R.string.not_setting)
            many.text = getString(R.string.not_setting)
            setTime.text = getString(R.string.not_setting)
        }


        var stock = sharedPreferences.getInt("stock", 0)
        var stock2 = sharedPreferences.getInt("stock2", 0)
        if (editFrag) {
            if (fragLR) {
                stock--
            } else {
                stock2--
            }
        }
        if (!sharedPreferences.getBoolean("LRSetting", false)) {
            if (stock - 1 >= 0) {
                v.stockText.text = getString(R.string.box_num, stock)
            } else {
                v.stockText.text = getString(R.string.box_enp)
                stock = 0
            }
        } else {
            if (stock - 1 >= 0 && stock2 - 1 >= 0) {
                v.stockText.text = getString(R.string.box_num2, stock, stock2)
            } else if (stock - 1 < 0 && stock2 - 1 >= 0) {
                v.stockText.text = getString(R.string.box_num3, stock2)
                stock = 0
            } else if (stock - 1 >= 0 && stock2 - 1 < 0) {
                v.stockText.text = getString(R.string.box_num4, stock)
                stock2 = 0
            } else {
                v.stockText.text = getString(R.string.box_enp)
                stock = 0
                stock2 = 0
            }
        }
        if (editFrag) {
            val edit = sharedPreferences.edit()
            if (fragLR) {
                edit.putInt("stock", stock)
            } else {
                edit.putInt("stock2", stock2)
            }
            edit.apply()
        }
        //通知の更新
        if (sharedPreferences.getBoolean("push", false) && editFrag) {
            if (sharedPreferences.getBoolean("setLR", false)) {
                AlarmPush().setNotification(2, sharedPreferences, activity)//LR
            } else {
                if (fragLR) {
                    AlarmPush().setNotification(0, sharedPreferences, activity)//L
                } else {
                    AlarmPush().setNotification(1, sharedPreferences, activity)//R
                }
            }

        }
    }
}