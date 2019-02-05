package momonyan.contactlensmanage

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
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
import kotlinx.android.synthetic.main.tab_more.*
import net.nend.android.NendAdInterstitial
import java.util.*


open class TabActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: TabAdapter? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor
    private var settingViewFrag = true

    private var settingChangeAd: Int = 0
    private var tabChangeAd: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settingChangeAd = resources.getInteger(R.integer.ad_id_pop_setting)
        tabChangeAd = resources.getInteger(R.integer.ad_id_pop_tab)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancelAll()

        //広告設定
        NendAdInterstitial.loadAd(this, getString(R.string.ad_apk_pop_setting), settingChangeAd)
        NendAdInterstitial.loadAd(this, getString(R.string.ad_apk_pop_tab), tabChangeAd)

        sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE)
        mSectionsPagerAdapter = TabAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                NendAdInterstitial.showAd(this@TabActivity, tabChangeAd)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        fab.setOnClickListener {
            if (settingViewFrag) {
                settingViewFrag = false
                settingDialogCreate()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu2 -> {
                AlertDialog.Builder(this)
                    .setTitle("Webページを開きます")
                    .setMessage("「プライバシーポリシー」\n「利用素材について」\nのページを開いてもよろしいですか？")
                    .setPositiveButton("はい") { _, _ ->
                        val uri = Uri.parse(getString(R.string.privacy_policy))
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                    .setNegativeButton("いいえ", null)
                    .show()
            }
            R.id.menu3 -> {
                AlertDialog.Builder(this)
                    .setTitle("Webページを開きます")
                    .setMessage("「意見・感想・報告について」\nのページを開いてもよろしいですか？")
                    .setPositiveButton("はい") { _, _ ->
                        val uri = Uri.parse(getString(R.string.enquete_url))
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                    .setNegativeButton("いいえ", null)
                    .show()
                return true
            }
        }
        return true
    }

    private fun settingDialogCreate() {
        val dlg = AlertDialog.Builder(this)
        val layout = this.layoutInflater.inflate(R.layout.setting_layout, null)

        //ダイアログボックス
        dlg.setTitle("設定画面")
        dlg.setView(layout)
        layout.stockIn.setText(sharedPreferences.getInt("stock", 0).toString(), TextView.BufferType.NORMAL)
        layout.stockIn2.setText(sharedPreferences.getInt("stock2", 0).toString(), TextView.BufferType.NORMAL)

        //通知時刻設定など
        var pushHour = sharedPreferences.getInt("pushHour", 0)
        var pushMinute = sharedPreferences.getInt("pushMinute", 0)
        layout.pushTimeIn.setText(String.format("%02d:%02d", pushHour, pushMinute), TextView.BufferType.NORMAL)
        //スイッチ設定
        val push = sharedPreferences.getBoolean("push", false)
        layout.switch1.isChecked = push
        layout.switch1.setOnCheckedChangeListener { _, isChecked ->
            layout.pushTimeIn.isEnabled = isChecked
        }

        layout.pushTimeIn.isEnabled = push
        layout.pushTimeIn.setFocusable(false)
        layout.pushTimeIn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val timepick = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minuteTime ->
                    // 設定 ボタンクリック時の処理
                    layout.pushTimeIn.setText(
                        String.format("%02d:%02d", hourOfDay, minuteTime),
                        TextView.BufferType.NORMAL
                    )
                    pushHour = hourOfDay
                    pushMinute = minuteTime
                },
                hour,
                minute,
                true
            )
            timepick.show()
        }

        //自動更新スピナーの初期位置
        when (sharedPreferences.getString("auto_setting", "not")) {
            "2Week" -> layout.spinner.setSelection(0)
            "1Month" -> layout.spinner.setSelection(1)
            else -> layout.spinner.setSelection(2)
        }

        //EditTextに文字の挿入
        //L(LR)
        setShardTexts(layout.makerIn, "maker")
        setShardTexts(layout.lensIn, "lens")
        setShardTexts(layout.typeIn, "type")
        setShardTexts(layout.otherIn, "other")
        //R
        setShardTexts(layout.makerIn_2, "maker2")
        setShardTexts(layout.lensIn_2, "lens2")
        setShardTexts(layout.typeIn_2, "type2")
        setShardTexts(layout.otherIn_2, "other2")

        //LR管理法
        //true : LR別
        //false: LR同時
        val checkLR = sharedPreferences.getBoolean("LRSetting", false)
        if (!checkLR) {
            layout.settingRLTextView.text = getString(R.string.lr_both)
            layout.cardViewR.visibility = View.GONE

            layout.stockLayout.hint = getString(R.string.box_hint)

            layout.stockLayout2.visibility = View.GONE
            layout.view2.visibility = View.GONE
        } else {
            layout.settingRLTextView.text = "Lのみ"
            layout.cardViewR.visibility = View.VISIBLE

            layout.stockLayout.hint = getString(R.string.box_hint_l)
            layout.stockLayout2.hint = getString(R.string.box_hint_r)

            layout.stockLayout2.visibility = View.VISIBLE
            layout.view2.visibility = View.VISIBLE
        }
        //トグルボタン設定
        layout.toggleButton.isChecked = checkLR
        layout.toggleButton.setOnClickListener {
            if (!layout.toggleButton.isChecked) {
                layout.settingRLTextView.text = getString(R.string.lr_both)
                layout.cardViewR.visibility = View.GONE

                layout.stockLayout.hint = getString(R.string.box_hint)

                layout.stockLayout2.visibility = View.GONE
                layout.view2.visibility = View.GONE
            } else {
                layout.settingRLTextView.text = "Lのみ"
                layout.cardViewR.visibility = View.VISIBLE

                layout.stockLayout.hint = getString(R.string.box_hint_l)
                layout.stockLayout2.hint = getString(R.string.box_hint_r)

                layout.stockLayout2.visibility = View.VISIBLE
                layout.view2.visibility = View.VISIBLE

            }
        }

        dlg.setPositiveButton("決定") { _, _ ->

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
            var stock = 0
            var stock2 = 0

            if (layout.stockIn.text.toString() != "") {
                edit.putInt("stock", layout.stockIn.text.toString().toInt())
                stock = layout.stockIn.text.toString().toInt()
            }
            if (layout.stockIn2.text.toString() != "") {
                edit.putInt("stock2", layout.stockIn2.text.toString().toInt())
                stock2 = layout.stockIn2.text.toString().toInt()
            }
            //自動更新
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

            //通知
            edit.putInt("pushHour", pushHour)
            edit.putInt("pushMinute", pushMinute)
            edit.putBoolean("push", layout.switch1.isChecked)

            edit.apply()


            //在庫個数の表示更新
            if (!layout.toggleButton.isChecked) {
                if (stock - 1 >= 0) {
                    stockText.text = getString(R.string.box_num, stock)
                } else {
                    stockText.text = getString(R.string.box_enp)
                }
            } else {
                if (stock - 1 >= 0 && stock2 - 1 >= 0) {
                    stockText.text = getString(R.string.box_num2, stock, stock2)
                } else if (stock - 1 < 0 && stock2 - 1 >= 0) {
                    stockText.text = getString(R.string.box_num3, stock2)
                } else if (stock - 1 >= 0 && stock2 - 1 < 0) {
                    stockText.text = getString(R.string.box_num4, stock)
                } else {
                    stockText.text = getString(R.string.box_enp)
                }
            }

            //通知更新
            deleteAlarm(0)
            deleteAlarm(1)
            deleteAlarm(2)

            if (layout.switch1.isChecked) {
                if (!layout.toggleButton.isChecked) {
                    AlarmPush().setNotification(2, sharedPreferences, applicationContext)//LR
                } else {
                    AlarmPush().setNotification(0, sharedPreferences, applicationContext)//L
                    AlarmPush().setNotification(1, sharedPreferences, applicationContext)//R
                }
            }
            settingViewFrag = true
            NendAdInterstitial.showAd(this, settingChangeAd)
        }

        dlg.setNegativeButton("キャンセル") { _, _ ->
            settingViewFrag = true
            NendAdInterstitial.showAd(this, settingChangeAd)
        }

        dlg.setOnCancelListener {
            settingViewFrag = true
            NendAdInterstitial.showAd(this, settingChangeAd)
        }

        // AlertDialogを表示する
        dlg.show()
    }

    private fun setShardTexts(editText: EditText, string: String) {
        editText.setText(sharedPreferences.getString(string, ""), TextView.BufferType.NORMAL)
    }

    private fun deleteAlarm(requestCode: Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, Notifier::class.java)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }

}
