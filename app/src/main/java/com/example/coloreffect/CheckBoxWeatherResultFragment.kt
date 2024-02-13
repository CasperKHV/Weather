package com.example.coloreffect

//import com.example.coloreffect.Controller.start
import com.example.coloreffect.NoteDataReader
import androidx.recyclerview.widget.RecyclerView
import com.example.coloreffect.R
import com.example.coloreffect.CityNote
import android.widget.TextView
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
import android.content.Intent
import com.example.coloreffect.WeatherResult
import com.example.coloreffect.CitiesListFragment
import android.content.SharedPreferences
import android.content.DialogInterface
import android.widget.EditText
import androidx.core.view.GravityCompat
import com.example.coloreffect.FragmentForNV
import android.os.Environment
import com.bumptech.glide.Glide
import android.database.sqlite.SQLiteOpenHelper
import com.example.coloreffect.DatabaseHelper
import android.database.sqlite.SQLiteDatabase
import kotlin.Throws
import android.content.ContentValues
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
import com.example.coloreffect.CheckBoxWeatherResultFragment
import android.content.pm.PackageManager
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import com.example.coloreffect.HistoryNote
import androidx.fragment.app.Fragment
import com.example.coloreffect.DatabaseHelperForHistory
import com.example.coloreffect.MainForGSON
import com.example.coloreffect.WeatherForGSON
import com.example.coloreffect.WindForGSON

class CheckBoxWeatherResultFragment : Fragment() {
    var pressureTextView: TextView? = null
    var feelsTextView: TextView? = null
    var humidityTextView: TextView? = null
    var pressure: String? = null
    var feels: String? = null
    var humidity: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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