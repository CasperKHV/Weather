package com.casperkhv.weather.temperature.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.casperkhv.weather.temperature.domain.TemperatureDataGetUseCase
import com.casperkhv.weather.temperature.presentation.model.TemperatureDataUiModel

internal class TemperatureViewModel(
    private val temperatureDataGetUseCase: TemperatureDataGetUseCase,
) : ViewModel() {

    /**
     * LiveData хранит некое состояние, которое могут слушать пользователи вью модели.
     */
    private val viewState: MutableLiveData<TemperatureDataUiModel> = MutableLiveData()

    init {
        loadInfo()
    }

    fun onRefreshClicked() {
        loadInfo()
    }

    fun getViewState(): LiveData<TemperatureDataUiModel> {
        return viewState
    }

    private fun loadInfo() {
        val data = temperatureDataGetUseCase.getTemperatureData("Saint Petersburg,ru")
        val uiModel = TemperatureDataUiModel(
            temperature = ((data.temperature * 10).toInt() / 10f).toString(),
        )
        viewState.value = uiModel
    }
}