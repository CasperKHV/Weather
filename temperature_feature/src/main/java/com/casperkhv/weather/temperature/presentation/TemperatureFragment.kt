package com.casperkhv.weather.temperature.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.casperkhv.weather.base_device.view.bindView
import com.casperkhv.weather.temperature.R
import com.casperkhv.weather.temperature.data.TemperatureRepositoryImpl
import com.casperkhv.weather.temperature.data.source.TemperatureService
import com.casperkhv.weather.temperature.domain.TemperatureDataUseCaseImpl
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

internal class TemperatureFragment : Fragment() {

    /**
     * Если у ViewModel в конструкторе есть параметры, то этот класс не будет создаваться автоматически.
     * Требуется создать его руками в фабрике.
     */
    private val viewModel: TemperatureViewModel by viewModels(
        factoryProducer = { createViewModelFactory() },
    )
    private val temperatureTextView by bindView<TextView>(R.id.temperature_text_view)
    private val temperatureRefreshButton by bindView<Button>(R.id.temperature_refresh_button)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_temperature, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        temperatureRefreshButton.setOnClickListener {
            viewModel.onRefreshClicked()
        }

        /*
         * LiveData решает за нас вопросы работы с жизненным циклом. Если экран уничтожен или невидим пользователю,
         * то observer не будет вызван. Как только экран будет видим пользователю, вызовется observer и это
         * будет гарантировать наличие View для отрисовки данных на экране.
         * viewLifecycleOwner - это держатель жизненного цикла. Это может быть View или Activity.
         */
        viewModel.getViewState().observe(viewLifecycleOwner) { uiModel ->
            temperatureTextView.text = uiModel.temperature
        }
    }

    private fun createViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TemperatureViewModel(
                    temperatureDataGetUseCase = TemperatureDataUseCaseImpl(
                        temperatureRepository = TemperatureRepositoryImpl(
                            temperatureService = Retrofit.Builder()
                                .baseUrl("https://api.openweathermap.org/data/2.5/")
                                .addConverterFactory(
                                    /*
                                     * Json - это класс библиотеки kotlinx.serialization для работы с файлами
                                     * формата JSON.
                                     */
                                    Json {
                                        ignoreUnknownKeys = true
                                        explicitNulls = false
                                        isLenient = true
                                        coerceInputValues = true
                                    }.asConverterFactory(
                                        MediaType.get("application/json; charset=UTF8")
                                    )
                                )
                                .build()
                                .create(TemperatureService::class.java)
                        )
                    ),
                ) as T
            }
        }
    }

    companion object {

        fun newInstance() = TemperatureFragment()
    }
}