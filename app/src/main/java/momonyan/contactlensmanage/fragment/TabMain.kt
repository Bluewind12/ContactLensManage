package momonyan.contactlensmanage.fragment

import android.app.DatePickerDialog
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

    private lateinit var datePickerDialog: DatePickerDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //日付設定時のリスナ作成
        val DateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
            //ログ出力
            Log.d("DatePicker", "year:$year monthOfYear:$monthOfYear dayOfMonth:$dayOfMonth")
        }

        var view = inflater.inflate(R.layout.tab_main, container, false)
        view.timeSetButton.setOnClickListener {
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

        return view
    }

}