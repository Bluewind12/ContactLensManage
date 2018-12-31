package momonyan.contactlensmanage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.setting_layout.view.*
import kotlinx.android.synthetic.main.tab_more.*
import kotlinx.android.synthetic.main.tab_more.view.*


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


        dlg.setPositiveButton("決定") { dialog, which ->

            edit = sharedPreferences.edit()

            makerText.text = layout.makerIn.text
            lensText.text = layout.lensIn.text
            typeText.text = layout.typeIn.text
            otherText.text = layout.otherIn.text

            edit.putString("maker", layout.makerIn.text.toString())
            edit.putString("lens", layout.lensIn.text.toString())
            edit.putString("type", layout.typeIn.text.toString())
            edit.putString("other", layout.otherIn.text.toString())

            edit.apply()
        }

        dlg.setNegativeButton("キャンセル") { dialog, which ->

        }
        // AlertDialogを表示する
        dlg.show()
    }
}
