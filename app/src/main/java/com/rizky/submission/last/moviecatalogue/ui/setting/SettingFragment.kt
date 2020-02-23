package com.rizky.submission.last.moviecatalogue.ui.setting


import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rizky.submission.last.moviecatalogue.R
import com.rizky.submission.last.moviecatalogue.reminder.RemainderReceiver
import kotlinx.android.synthetic.main.fragment_setting.*

/**
 * A simple [Fragment] subclass.
 */
class SettingFragment : Fragment() {

    private lateinit var remainderReceiver: RemainderReceiver
    private var dailyRemainder = "Daily Remainder"
    private var releaseRemainder = "Release Remainder"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        remainderReceiver = RemainderReceiver()

        //check remainder status to set checkbox
        checkbox_daily_remainder.isChecked =
            remainderReceiver.isAlarmSet(requireContext(), dailyRemainder)

        checkbox_release_remainder.isChecked =
            remainderReceiver.isAlarmSet(requireContext(), releaseRemainder)

        checkbox_daily_remainder.setOnClickListener{
            if(checkbox_daily_remainder.isChecked){
                remainderReceiver.setRemainder(requireContext(), dailyRemainder, resources.getString(R.string.daily_remainder))
            } else {
                remainderReceiver.cancelRemainder(requireContext(), dailyRemainder)
            }
        }
        checkbox_release_remainder.setOnClickListener{
            if(checkbox_release_remainder.isChecked){
                remainderReceiver.setRemainder(requireContext(), releaseRemainder, resources.getString(R.string.daily_remainder))
            } else {
                remainderReceiver.cancelRemainder(requireContext(), releaseRemainder)
            }
        }

        btn_change_language.setOnClickListener{
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
    }

}
