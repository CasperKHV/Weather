package com.casperkhv.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class CheckBoxWeatherResultFragment : Fragment() {
    private val pressureTextView by bindView<TextView>(R.id.textview_pressure)
    private val feelsTextView by bindView<TextView>(R.id.textview_feels)
    private val humidityTextView by bindView<TextView>(R.id.textview_humidity)
    private var pressure: String? = null
    private var feels: String? = null
    private var humidity: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check_box_weather_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (pressure != null) {
            pressureTextView.visibility = View.VISIBLE
            pressureTextView.text = pressure
        }
        if (feels != null) {
            feelsTextView.visibility = View.VISIBLE
            feelsTextView.text = feels
        }
        if (humidity != null) {
            humidityTextView.visibility = View.VISIBLE
            humidityTextView.text = humidity
        }
    }

    companion object {
        fun newInstance(
            pressure: String?,
            feels: String?,
            humidity: String?
        ): CheckBoxWeatherResultFragment {
            val fragment = CheckBoxWeatherResultFragment()
            fragment.pressure = pressure
            fragment.feels = feels
            fragment.humidity = humidity
            return fragment
        }
    }
}