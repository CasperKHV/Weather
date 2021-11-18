package com.example.coloreffect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class CheckBoxWeatherResultFragment : Fragment() {

    var pressureTextView: TextView? = null
    var feelsTextView: TextView? = null
    var humidityTextView: TextView? = null
    var pressure: String? = null
    var feels: String? = null
    var humidity: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_check_box_weather_result, container, false)
        pressureTextView = view.findViewById(R.id.textview_pressure)
        feelsTextView = view.findViewById(R.id.textview_feels)
        humidityTextView = view.findViewById(R.id.textview_humidity)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (pressure != null) {
            pressureTextView!!.visibility = View.VISIBLE
            pressureTextView!!.text = pressure
        }
        if (feels != null) {
            feelsTextView!!.visibility = View.VISIBLE
            feelsTextView!!.text = feels
        }
        if (humidity != null) {
            humidityTextView!!.visibility = View.VISIBLE
            humidityTextView!!.text = humidity
        }
    }

    companion object {

        fun newInstance(pressure: String?, feels: String?, humidity: String?): CheckBoxWeatherResultFragment {
            val fragment = CheckBoxWeatherResultFragment()
            fragment.pressure = pressure
            fragment.feels = feels
            fragment.humidity = humidity
            return fragment
        }
    }
}