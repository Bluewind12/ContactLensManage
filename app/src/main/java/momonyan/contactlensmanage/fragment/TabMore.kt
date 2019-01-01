package momonyan.contactlensmanage.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.tab_more.view.*
import momonyan.contactlensmanage.R
import java.util.*

class TabMore : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab_more, container, false)

        sharedPreferences = activity!!.getSharedPreferences("Data",Context.MODE_PRIVATE)

        view.makerText.text = sharedPreferences.getString("maker", getString(R.string.not_setting))
        view.lensText.text = sharedPreferences.getString("lens", getString(R.string.not_setting))
        view.typeText.text = sharedPreferences.getString("type", getString(R.string.not_setting))
        view.otherText.text = sharedPreferences.getString("other", getString(R.string.not_setting))


        val nowCalendar = Calendar.getInstance()
        val nowYear = nowCalendar.get(Calendar.YEAR)
        val nowMonthOfYear = nowCalendar.get(Calendar.MONTH) + 1
        val nowDayOfMonth = nowCalendar.get(Calendar.DAY_OF_MONTH)

        val year = sharedPreferences.getInt("Year", 0)
        val month = sharedPreferences.getInt("Month", 0)
        val day = sharedPreferences.getInt("Day", 0)

        if (month > 0 && day > 0 && year > 0) {
            view.moreLimitText.text = getString(R.string.now_time2, month, day)
            view.moreLimitTimeText.setTextColor(resources.getColor(R.color.default_text))
            if (year - nowYear < 0) {
                view.moreLimitTimeText.text = getString(R.string.limit)
            } else if ((year - nowYear == 0) && (month - nowMonthOfYear == 0) && (day - nowDayOfMonth == 0)) {
                view.moreLimitTimeText.text = getString(R.string.limit_days, 0)
                view.moreLimitTimeText.setTextColor(resources.getColor(R.color.red))
            } else if ((year - nowYear >= 0) && (month - nowMonthOfYear >= 0) && (day - nowDayOfMonth >= 0)) {
                val limitMonth = month - nowMonthOfYear + ((year - nowYear) * 12)
                val limitDay = day - nowDayOfMonth

                if (limitMonth > 0) {
                    view.moreLimitTimeText.text = getString(R.string.limit_month_days, limitMonth, limitDay)
                } else {
                    view.moreLimitTimeText.text = getString(R.string.limit_days, limitDay)
                    if (limitDay <= 3) {
                        view.moreLimitTimeText.setTextColor(resources.getColor(R.color.red))
                    }
                }
            } else {
                view.moreLimitTimeText.text = getString(R.string.limit)
            }

        } else {
            view.moreLimitTimeText.text = getString(R.string.not_setting)

        }

        return view
    }

}