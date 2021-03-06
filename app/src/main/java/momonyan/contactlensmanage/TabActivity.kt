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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayout
import jp.co.runners.rateorfeedback.RateOrFeedback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.setting_layout.view.*
import kotlinx.android.synthetic.main.tab_main.*
import kotlinx.android.synthetic.main.tab_more.*
import java.util.*


class TabActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: TabAdapter? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor
    private var settingViewFrag = true

    private val tabAd = InterstitialAd(this)
    private val settingAd = InterstitialAd(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //AD
        MobileAds.initialize(this, "ca-app-pub-6499097800180510~5042437953")
        tabAd.adUnitId = getString(R.string.ad_tab_pop)
        tabAd.loadAd(AdRequest.Builder().build())

        settingAd.adUnitId = getString(R.string.ad_setting_pop)
        settingAd.loadAd(AdRequest.Builder().build())


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()

        sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE)
        mSectionsPagerAdapter = TabAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tabAd.isLoaded) {
                    tabAd.show()
                }
                viewAlertDialog()
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
            R.id.tabSetting -> settingDialogCreate()
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
            R.id.menuShortCutUrl -> {
                val uri = Uri.parse(sharedPreferences.getString("shortCutUrl", "http://"))
                val urlIntent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(urlIntent)
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
        layout.shortcutUrlTextInputEditText.setText(
            sharedPreferences.getString(
                "shortCutUrl",
                "http://"
            ), TextView.BufferType.NORMAL
        )

        //通知時刻設定など
        var pushHour = sharedPreferences.getInt("pushHour", 0)
        var pushMinute = sharedPreferences.getInt("pushMinute", 0)
        layout.pushTimeIn.setText(String.format("%02d:%02d", pushHour, pushMinute), TextView.BufferType.NORMAL)
        //スイッチ設定
        val push = sharedPreferences.getBoolean("push", false)
        layout.switch1.isChecked = push
        layout.switch1.setOnCheckedChangeListener { _, isChecked ->
            layout.pushTimeIn.isEnabled = isChecked
            layout.switch2.isEnabled = isChecked
        }

        //時間入力
        layout.pushTimeIn.isEnabled = push
        layout.pushTimeIn.isFocusable = false
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

        //振動機能
        layout.switch2.isEnabled = push
        layout.switch2.isChecked = sharedPreferences.getBoolean("Vibrate", true)

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

            //入力が正しいかの判別
            if (layout.stockIn.text.toString().matches(Regex("[0-9]+"))) {
                edit.putInt("stock", layout.stockIn.text.toString().toInt())
                stock = layout.stockIn.text.toString().toInt()
            }
            if (layout.stockIn2.text.toString().matches(Regex("[0-9]+"))) {
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
            edit.putBoolean("Vibrate", layout.switch2.isChecked)

            //URL
            edit.putString("shortCutUrl", layout.shortcutUrlTextInputEditText.text.toString())

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
            if (settingAd.isLoaded) {
                settingAd.show()
            }
        }

        dlg.setNegativeButton("キャンセル") { _, _ ->
            settingViewFrag = true
            if (settingAd.isLoaded) {
                settingAd.show()
            }
        }

        dlg.setOnCancelListener {
            settingViewFrag = true
            if (settingAd.isLoaded) {
                settingAd.show()
            }
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

    private fun viewAlertDialog() {
        RateOrFeedback(this)
            // レビュー用ストアURL
            .setPlayStoreUrl(getString(R.string.reviewUrl))
            // 改善点・要望の送信先メールアドレス
            .setFeedbackEmail(getString(R.string.reviewMail))
            // 一度、評価するか改善点を送信するを選択した場合、それ以降はダイアログが表示されません。
            // この値をインクリメントすることで再度ダイアログが表示されるようになります。
            .setReviewRequestId(0)
            // 前回ダイアログを表示してから次にダイアログを表示してよいタイミングまでの期間です。
            .setIntervalFromPreviousShowing(60 * 60 * 3)//3時間
            // アプリをインストールしてから、ここで指定された期間はダイアログを表示しません。
            .setNotShowTermSecondsFromInstall(60 * 60)//1時間
            .showIfNeeds()

    }


}
