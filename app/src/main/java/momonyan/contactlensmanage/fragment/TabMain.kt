package momonyan.contactlensmanage.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.tab_main.view.*
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.tab_main, container, false)
        sharedPreferences = activity!!.getSharedPreferences("Data", Context.MODE_PRIVATE)
        setLimit()

        //日付設定時のリスナ作成
        val DateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
            AlertDialog.Builder(activity)
                .setTitle("コンタクトレンズ 更新")
                .setMessage("更新してもよろしいですか？")
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    //ログ出力
                    Log.d("DatePicker", "year:$year monthOfYear:$monthOfYear dayOfMonth:$dayOfMonth")

                    v.setTimeText.text = getString(R.string.set_time, nowMonthOfYear, nowDayOfMonth)
                    v.minuteTimeText.text = getString(R.string.now_time2, monthOfYear + 1, dayOfMonth)
                    val edit = sharedPreferences.edit()

                    //期限日
                    edit.putInt("Year", year)
                    edit.putInt("Month", monthOfYear + 1)
                    edit.putInt("Day", dayOfMonth)

                    //設定日
                    edit.putInt("SetYear", nowYear)
                    edit.putInt("SetMonth", nowMonthOfYear)
                    edit.putInt("SetDay", nowDayOfMonth)

                    //保存
                    edit.apply()

                    //更新
                    setLimit()
                })
                .setNegativeButton("Cancel", null)
                .show()
        }

        v.timeSetButton.setOnClickListener {
            when (sharedPreferences.getString("auto_setting", "not")) {
                "2Week" -> {
                    AlertDialog.Builder(activity)
                        .setTitle("コンタクトレンズ 更新")
                        .setMessage("2Week\n更新してもよろしいですか？")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
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
                        })
                        .setNegativeButton("Cancel", null)
                        .show()
                }
                "1Month" -> {
                    AlertDialog.Builder(activity)
                        .setTitle("コンタクトレンズ 更新")
                        .setMessage("1Month\n更新してもよろしいですか？")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
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
                        })
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
                    datePickerDialog = DatePickerDialog(context, DateSetListener, year, monthOfYear, dayOfMonth)

                    //日付設定ダイアログの表示
                    datePickerDialog.show()
                }
            }
        }
        return v
    }


    private  fun setLimit() {
        val year = sharedPreferences.getInt("Year", 0)
        val month = sharedPreferences.getInt("Month", 0)
        val day = sharedPreferences.getInt("Day", 0)

        v.manyText.setTextColor(resources.getColor(R.color.default_text))

        if (month > 0 && day > 0 && year > 0) {
            v.minuteTimeText.text = getString(R.string.now_time2, month, day)
            v.setTimeText.text = getString(
                R.string.set_time,
                sharedPreferences.getInt("SetMonth", 0),
                sharedPreferences.getInt("SetDay", 0)
            )

            if (year - nowYear < 0) {
                v.manyText.text = getString(R.string.limit)
            } else if ((year - nowYear == 0) && (month - nowMonthOfYear == 0) && (day - nowDayOfMonth == 0)) {
                v.manyText.text = getString(R.string.limit_days, 0)
                v.manyText.setTextColor(resources.getColor(R.color.red))
            } else if ((year - nowYear >= 0) && (month - nowMonthOfYear >= 0) && (day - nowDayOfMonth >= 0)) {
                val limitMonth = month - nowMonthOfYear + ((year - nowYear) * 12)
                val limitDay = day - nowDayOfMonth

                if (limitMonth > 0) {
                    v.manyText.text = getString(R.string.limit_month_days, limitMonth, limitDay)
                } else {
                    v.manyText.text = getString(R.string.limit_days, limitDay)
                    if (limitDay <= 3) {
                        v.manyText.setTextColor(resources.getColor(R.color.red))
                    }
                }
            } else {
                v.manyText.text = getString(R.string.limit)
            }

        } else {
            v.minuteTimeText.text = getString(R.string.not_setting)
            v.manyText.text = getString(R.string.not_setting)
            v.setTimeText.text = getString(R.string.not_setting)
        }

        var stock = sharedPreferences.getInt("stock", 0) - 1

        if(stock > 0) {
            v.stockText.text = getString(R.string.box_num, stock)
        }else{
            v.stockText.text = getString(R.string.box_enp)
            stock = 0
        }
            val edit = sharedPreferences.edit()
            edit.putInt("stock", stock)
            edit.apply()

    }
}