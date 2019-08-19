package momonyan.contactlensmanage.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.tab_more.view.*
import momonyan.contactlensmanage.ContactDataHolder
import momonyan.contactlensmanage.R

class TabMore : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab_more, container, false)

        val adRequest = AdRequest.Builder().build()
        view.adMore.loadAd(adRequest)
        sharedPreferences = activity!!.getSharedPreferences("Data", Context.MODE_PRIVATE)
        //Holder
        val contactHolder = ContactDataHolder
        contactHolder.maker = sharedPreferences.getString("maker", getString(R.string.not_setting))
        contactHolder.lens = sharedPreferences.getString("lens", getString(R.string.not_setting))
        contactHolder.type = sharedPreferences.getString("type", getString(R.string.not_setting))
        contactHolder.otherMemos = sharedPreferences.getString("other", getString(R.string.not_setting))

        //Holder
        val contactHolder2 = ContactDataHolder
        contactHolder2.maker = sharedPreferences.getString("maker2", getString(R.string.not_setting))
        contactHolder2.lens = sharedPreferences.getString("lens2", getString(R.string.not_setting))
        contactHolder2.type = sharedPreferences.getString("type2", getString(R.string.not_setting))
        contactHolder2.otherMemos = sharedPreferences.getString("other2", getString(R.string.not_setting))

        //表示
        view.makerText.text = contactHolder.maker
        view.lensText.text = contactHolder.lens
        view.typeText.text = contactHolder.type
        view.otherText.text = contactHolder.otherMemos
        view.makerText_2.text = contactHolder2.maker
        view.lensText_2.text = contactHolder2.lens
        view.typeText_2.text = contactHolder2.type
        view.otherText_2.text = contactHolder2.otherMemos

        if (sharedPreferences.getBoolean("LRSetting", false)) {
            view.imageView3.setImageResource(R.drawable.l_icon)
            view.cardView2_2.visibility = View.VISIBLE
        } else {
            view.imageView3.setImageResource(R.drawable.lr_icon)
            view.cardView2_2.visibility = View.GONE
        }

        return view
    }

}