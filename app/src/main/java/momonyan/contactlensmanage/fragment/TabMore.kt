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

class TabMore : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab_more, container, false)

        sharedPreferences = activity!!.getSharedPreferences("Data", Context.MODE_PRIVATE)

        view.makerText.text = sharedPreferences.getString("maker", getString(R.string.not_setting))
        view.lensText.text = sharedPreferences.getString("lens", getString(R.string.not_setting))
        view.typeText.text = sharedPreferences.getString("type", getString(R.string.not_setting))
        view.otherText.text = sharedPreferences.getString("other", getString(R.string.not_setting))

        view.makerText_2.text = sharedPreferences.getString("maker2", getString(R.string.not_setting))
        view.lensText_2.text = sharedPreferences.getString("lens2", getString(R.string.not_setting))
        view.typeText_2.text = sharedPreferences.getString("type2", getString(R.string.not_setting))
        view.otherText_2.text = sharedPreferences.getString("other2", getString(R.string.not_setting))

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