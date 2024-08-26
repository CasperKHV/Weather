package com.casperkhv.weather.temperature.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.casperkhv.weather.base_device.view.bindView
import com.casperkhv.weather.temperature.R

internal class TemperatureFragment : Fragment() {

    private val viewModel: TemperatureViewModel by viewModels()
    private val temperatureTextView by bindView<TextView>(R.id.temperature_text_view)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_temperature, container, false)
    }

    companion object {

        fun newInstance() = TemperatureFragment()
    }
}