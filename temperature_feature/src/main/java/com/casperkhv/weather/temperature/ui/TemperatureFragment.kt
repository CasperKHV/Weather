package com.casperkhv.weather.temperature.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.casperkhv.weather.temperature.R

class TemperatureFragment : Fragment() {

    private val viewModel: TemperatureViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_temperature, container, false)
    }

    companion object {

        fun newInstance() = TemperatureFragment()
    }
}