package momonyan.contactlensmanage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.setting_layout.view.*
import kotlinx.android.synthetic.main.tab_main.*
import kotlinx.android.synthetic.main.tab_main.view.*
import kotlinx.android.synthetic.main.tab_more.*


open class TabActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: TabAdapter? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val moreInfo = this.layoutInflater.inflate(R.layout.tab_more, null)

        sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE)
        mSectionsPagerAdapter = TabAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu1 -> {
                settingDialogCreate()
            }
        }
        return true
    }

    private fun settingDialogCreate() {
        val layout = this.layoutInflater.inflate(R.layout.setting_layout, null)
        //ダイアログボックス
        val dlg = AlertDialog.Builder(this)
        dlg.setTitle("設定画面")
        dlg.setView(layout)
        layout.stockIn.setText(sharedPreferences.getInt("stock", 0).toString(), TextView.BufferType.NORMAL)

        when (sharedPreferences.getString("auto_setting", "not")) {
            "2Week" -> layout.spinner.setSelection(0)
            "1Month" -> layout.spinner.setSelection(1)
            else -> layout.spinner.setSelection(2)
        }

        setShardTexts(layout.makerIn, "maker")
        setShardTexts(layout.lensIn, "lens")
        setShardTexts(layout.typeIn, "type")
        setShardTexts(layout.otherIn, "other")

        setShardTexts(layout.makerIn_2, "maker2")
        setShardTexts(layout.lensIn_2, "lens2")
        setShardTexts(layout.typeIn_2, "type2")
        setShardTexts(layout.otherIn_2, "other2")


        val checkLR = sharedPreferences.getBoolean("LRSetting", false)

        if (!checkLR) {
            layout.settingRLTextView.text = "LR両方"
            layout.cardViewR.visibility = View.GONE

        } else {
            layout.settingRLTextView.text = "Lのみ"
            layout.cardViewR.visibility = View.VISIBLE
        }

        layout.toggleButton.isChecked = checkLR

        layout.toggleButton.setOnClickListener {
            if (!layout.toggleButton.isChecked) {
                layout.settingRLTextView.text = "LR両方"
                layout.cardViewR.visibility = View.GONE
            } else {
                layout.settingRLTextView.text = "Lのみ"
                layout.cardViewR.visibility = View.VISIBLE
            }
        }

        dlg.setPositiveButton("決定") { dialog, which ->

            edit = sharedPreferences.edit()

            makerText.text = layout.makerIn.text
            lensText.text = layout.lensIn.text
            typeText.text = layout.typeIn.text
            otherText.text = layout.otherIn.text

            makerText_2.text = layout.makerIn_2.text
            lensText_2.text = layout.lensIn_2.text
            typeText_2.text = layout.typeIn_2.text
            otherText_2.text = layout.otherIn_2.text

            if (layout.toggleButton.isChecked) {
                imageView3.setImageResource(R.drawable.l_icon)
                cardView2_2.visibility = View.VISIBLE
                limitDay1.visibility = View.VISIBLE
                setTimeTextR.visibility = View.VISIBLE
                imageView.setImageResource(R.drawable.l_icon)
                timeSetButton.visibility = View.GONE
                lSettingButton.visibility = View.VISIBLE
                rSettingButton.visibility = View.VISIBLE
            } else {
                imageView3.setImageResource(R.drawable.lr_icon)
                cardView2_2.visibility = View.GONE
                limitDay1.visibility = View.GONE
                setTimeTextR.visibility = View.GONE
                imageView.setImageResource(R.drawable.lr_icon)
                timeSetButton.visibility = View.VISIBLE
                lSettingButton.visibility = View.GONE
                rSettingButton.visibility = View.GONE
            }

            edit.putInt("stock", layout.stockIn.text.toString().toInt())
            edit.putString("auto_setting", layout.spinner.selectedItem.toString())

            edit.putBoolean("LRSetting", layout.toggleButton.isChecked)

            edit.putString("maker", layout.makerIn.text.toString())
            edit.putString("lens", layout.lensIn.text.toString())
            edit.putString("type", layout.typeIn.text.toString())
            edit.putString("other", layout.otherIn.text.toString())

            edit.putString("maker2", layout.makerIn_2.text.toString())
            edit.putString("lens2", layout.lensIn_2.text.toString())
            edit.putString("type2", layout.typeIn_2.text.toString())
            edit.putString("other2", layout.otherIn_2.text.toString())

            edit.apply()
        }

        dlg.setNegativeButton("キャンセル") { dialog, which ->

        }
        // AlertDialogを表示する
        dlg.show()
    }

    private fun setShardTexts(editText: EditText, string: String) {
        editText.setText(sharedPreferences.getString(string, ""), TextView.BufferType.NORMAL)
    }

}
