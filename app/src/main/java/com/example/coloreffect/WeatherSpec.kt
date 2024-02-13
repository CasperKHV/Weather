package com.example.coloreffect

//import com.example.coloreffect.Controller.start
import com.example.coloreffect.NoteDataReader
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.coloreffect.R
import com.example.coloreffect.CityNote
import android.widget.TextView
import android.view.MenuInflater
import com.example.coloreffect.ModelForGSONWeatherClass
import androidx.appcompat.app.AppCompatActivity
import com.example.coloreffect.CitiesListFragment.CitiesListListener
import androidx.navigation.ui.AppBarConfiguration
import com.example.coloreffect.NoteDataSource
import android.os.Bundle
import com.example.coloreffect.MainActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI
import com.example.coloreffect.DataForBundle
import com.example.coloreffect.WeatherResultFragment
import com.example.coloreffect.WeatherResult
import com.example.coloreffect.CitiesListFragment
import android.widget.EditText
import androidx.core.view.GravityCompat
import com.example.coloreffect.FragmentForNV
import android.os.Environment
import com.bumptech.glide.Glide
import android.database.sqlite.SQLiteOpenHelper
import com.example.coloreffect.DatabaseHelper
import android.database.sqlite.SQLiteDatabase
import kotlin.Throws
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View.OnLongClickListener
import com.example.coloreffect.WeatherSpec
import android.widget.Toast
import retrofit2.http.GET
import com.example.coloreffect.NoteDataSourceForHistory
import com.example.coloreffect.NoteDataReaderForHistory
import com.example.coloreffect.WeatherResultFragment.HistoryListListener
import android.content.res.Resources.NotFoundException
import android.app.Activity
import android.content.*
import com.example.coloreffect.CheckBoxWeatherResultFragment
import android.content.pm.PackageManager
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import com.example.coloreffect.HistoryNote
import android.view.Gravity
import com.example.coloreffect.DatabaseHelperForHistory
import com.example.coloreffect.MainForGSON
import com.example.coloreffect.WeatherForGSON
import com.example.coloreffect.WindForGSON
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

internal object WeatherSpec {
    fun getWeather(context: Context?, position: Int, weather: ModelForGSONWeatherClass): String {
        return """
               ${context!!.getString(R.string.now_in)}${weather.name}": ${weather.main!!.temp}${
            context.getString(
                R.string.celsius
            )
        }
               ${context.getString(R.string.for_wind_beggining)}${weather.wind?.speed}${
            context.getString(
                R.string.for_wind
            )
        }
               ${weather.weather[0]?.description}
               """.trimIndent()
    }

    fun getWeatherHistory(
        context: Context?,
        weather: ModelForGSONWeatherClass,
        currentDate: Date?
    ): String {

        // Форматирование времени как "часы:минуты:секунды"
        val timeFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeText = timeFormat.format(currentDate)
        return """
               ${context!!.getString(R.string.at)}$timeText${context.getString(R.string.it_was)}: ${weather.main!!.temp}${
            context.getString(
                R.string.celsius
            )
        }
               ${context.getString(R.string.for_wind_beggining)}${weather.wind?.speed}${
            context.getString(
                R.string.for_wind
            )
        }
               ${weather.weather[0]?.description}
               """.trimIndent()
    }

    fun getDate(context: Context?, currentDate: Date?): String {
        // Форматирование времени как "день.месяц.год"
        val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(currentDate)
    }

    fun getPressure(context: Context?, weather: ModelForGSONWeatherClass): String {
        return Math.round(weather.main!!.pressure / 1.333333333333)
            .toString() + context!!.getString(R.string.mm_hg)
    }

    fun getFeels(context: Context?, weather: ModelForGSONWeatherClass): String {
        return context!!.getString(R.string.for_feels_beggining) + weather.main?.feels_like
            .toString() + context.getString(R.string.celsius)
    }

    fun getHumidity(context: Context?, weather: ModelForGSONWeatherClass): String {
        return context!!.getString(R.string.humidity) + weather.main?.humidity
            .toString() + context.getString(R.string.percent)
    }
}