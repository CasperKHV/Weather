package com.casperkhv.weather

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Date

class CitiesListFragment : Fragment() {
    private var errorCode = false
    private var notesDataSource: NoteDataSource? = null
    private var noteDataReader: NoteDataReader? = null
    private var adapterRV: MyAdapter? = null
    private var savedCity: SharedPreferences? = null
    private val checkBoxPressure by bindView<CheckBox>(R.id.checkbox_pressure)
    private val checkBoxFeels by bindView<CheckBox>(R.id.checkbox_feels)
    private val checkBoxHumidity by bindView<CheckBox>(R.id.checkbox_humidity)
    private val descriptionText by bindView<TextView>(R.id.textview_description)
    private var citiesListListener: CitiesListListener? = null


    internal interface CitiesListListener {
        fun onListItemClick(id: Int, dataForBundle: DataForBundle?, descriptionText: TextView?)
        fun transfer(
            notesDataSourceNoteDataSource: NoteDataSource?,
            noteDataReader: NoteDataReader?,
            adapter: MyAdapter?
        )
    }

    override fun onAttach(context: Context) {
        citiesListListener = try {
            context as CitiesListListener
        } catch (e: ClassCastException) {
            throw IllegalArgumentException("$context must implement CitiesListListener")
        } catch (e: NullPointerException) {
            throw IllegalArgumentException("$context must implement CitiesListListener")
        }
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_cities_list, container, false)
        initDataSource()
        val citiesCategoriesRecyclerView: RecyclerView = rootView.findViewById(R.id.recycler_view)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = RecyclerView.VERTICAL
        citiesCategoriesRecyclerView.layoutManager = layoutManager
        adapterRV = MyAdapter()
        citiesCategoriesRecyclerView.adapter = adapterRV
        citiesListListener!!.transfer(notesDataSource, noteDataReader, adapterRV)
        if (noteDataReader!!.count == 0) {
            initCities()
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePreferences()
        checkBoxPressure.isChecked = savedCity!!.getBoolean(CHECK_BOX_PRESSURE, false)
        checkBoxFeels.isChecked = savedCity!!.getBoolean(CHECK_BOX_FEELS, false)
        checkBoxHumidity.isChecked = savedCity!!.getBoolean(CHECK_BOX_HUMIDITY, false)
        val previousWeatherId = savedCity!!.getInt(PREVIOUS_WEATHER_ID, -1)
        if (previousWeatherId != -1) {
            showActivity(previousWeatherId)
        }
    }

    inner class MyViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup?) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.category_list_item, parent, false)),
        View.OnClickListener {
        private val categoryNameTextView: TextView
        private var note: CityNote? = null
        fun bind(note: CityNote?) {
            this.note = note
            categoryNameTextView.text = note!!.title
        }

        override fun onClick(view: View) {
            showActivity(this.layoutPosition)
        }

        private fun showPopupMenu(view: View) {
            val popup = PopupMenu(view.context, view)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.main_context_menu, popup.menu)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_edit -> {
                        editElement(note)
                        return@OnMenuItemClickListener true
                    }

                    R.id.menu_delete -> {
                        deleteElement(note)
                        return@OnMenuItemClickListener true
                    }
                }
                false
            })
            popup.show()
        }

        init {
            itemView.setOnClickListener(this)
            categoryNameTextView =
                itemView.findViewById<View>(R.id.category_name_text_view) as TextView

            categoryNameTextView.setOnLongClickListener {
                showPopupMenu(categoryNameTextView)
                true
            }
            categoryNameTextView.setOnClickListener(this)
        }
    }

    inner class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return MyViewHolder(inflater, parent)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bind(noteDataReader!!.getPosition(position))
        }

        override fun getItemCount(): Int {
            return noteDataReader!!.count
        }
    }

    private fun showActivity(categoryId: Int) {
        val thread: Thread = object : Thread() {
            override fun run() {
                val controller = Controller()
                var resultPressure: String? = null
                var resultFeels: String? = null
                var resultHumidity: String? = null
                var iconCode: String? = null
                val currentDate = Date()
                val city = noteDataReader!!.getPosition(categoryId).description
                savedCity!!.edit().putInt(PREVIOUS_WEATHER_ID, categoryId).apply()
                val cityNamesForAPI = resources.getStringArray(R.array.city_names_for_load_weather)
                val weather = controller.start(requireActivity(), city)
                if (weather == null) {
                    errorCode = true
                    return
                }
                val cityForBundle = weather.name
                val resultWeather = WeatherSpec.getWeather(activity, weather)
                val resultWeatherHistory =
                    WeatherSpec.getWeatherHistory(activity, weather, currentDate)
                val dateForHistory = WeatherSpec.getDate(currentDate)
                if (checkBoxPressure.isChecked) {
                    resultPressure = WeatherSpec.getPressure(activity, weather)
                }
                if (checkBoxFeels.isChecked) {
                    resultFeels = WeatherSpec.getFeels(activity, weather)
                }
                if (checkBoxHumidity.isChecked) {
                    resultHumidity = WeatherSpec.getHumidity(activity, weather)
                }
                iconCode = weather.weather[0]!!.icon
                val dataForBundle = DataForBundle(
                    resultPressure,
                    resultFeels,
                    resultHumidity,
                    resultWeather,
                    dateForHistory,
                    resultWeatherHistory,
                    iconCode,
                    categoryId,
                    cityForBundle
                )
                citiesListListener!!.onListItemClick(categoryId, dataForBundle, descriptionText)
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            Log.e("CitiesListFragment", "Thread (name: ${thread.name}) was interrupted", e)
        }
        if (errorCode) {
            errorCode = false
            Toast.makeText(activity, "City not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializePreferences() {
        savedCity = requireActivity().getSharedPreferences(SAVED_CITY, Context.MODE_PRIVATE)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        savedCity!!.edit().putBoolean(CHECK_BOX_PRESSURE, checkBoxPressure.isChecked).apply()
        savedCity!!.edit().putBoolean(CHECK_BOX_FEELS, checkBoxFeels.isChecked).apply()
        savedCity!!.edit().putBoolean(CHECK_BOX_HUMIDITY, checkBoxHumidity.isChecked).apply()
        super.onSaveInstanceState(outState)
    }

    private fun editElement(note: CityNote?) {
        val factory = LayoutInflater.from(activity)
        val alertView = factory.inflate(R.layout.layout_add_city_note, null)
        val builder = AlertDialog.Builder(
            requireActivity()
        )
        builder.setView(alertView)
        builder.setTitle(R.string.alert_title_add)
        val editTextNote = alertView.findViewById<EditText>(R.id.editTextNote)
        val editTextNoteTitle = alertView.findViewById<EditText>(R.id.editTextNoteTitle)
        editTextNoteTitle.setText(note!!.title)
        editTextNote.setText(note.description)
        builder.setNegativeButton(R.string.alert_cancel, null)
        builder.setPositiveButton(R.string.refresh_the_note) { dialog, id ->
            notesDataSource!!.editNote(
                note,
                editTextNoteTitle.text.toString(),
                editTextNote.text.toString()
            )
            dataUpdated()
        }
        builder.show()
    }

    private fun deleteElement(note: CityNote?) {
        notesDataSource!!.deleteNote(note)
        dataUpdated()
    }

    private fun dataUpdated() {
        noteDataReader!!.refresh()
        adapterRV!!.notifyDataSetChanged()
    }

    private fun initDataSource() {
        notesDataSource = NoteDataSource(activity)
        notesDataSource!!.open()
        noteDataReader = notesDataSource!!.noteDataReader
    }

    private fun initCities() {
        notesDataSource!!.addNote(
            requireActivity().resources.getStringArray(R.array.cityes_selection)[0],
            requireActivity().resources.getStringArray(R.array.city_names_for_load_weather)[0]
        )
        notesDataSource!!.addNote(
            requireActivity().resources.getStringArray(R.array.cityes_selection)[1],
            requireActivity().resources.getStringArray(R.array.city_names_for_load_weather)[1]
        )
        notesDataSource!!.addNote(
            requireActivity().resources.getStringArray(R.array.cityes_selection)[2],
            requireActivity().resources.getStringArray(R.array.city_names_for_load_weather)[2]
        )
        dataUpdated()
    }

    companion object {
        const val SAVED_CITY = "savedCity"
        const val RESULT_OK_STRING = "Ok"
        private const val CHECK_BOX_PRESSURE = "checkBoxPressure"
        private const val CHECK_BOX_FEELS = "checkBoxFeels"
        private const val CHECK_BOX_HUMIDITY = "checkBoxWeek"
        const val PREVIOUS_WEATHER_ID = "previous weather id"
        const val REQUEST_CODE = 100
    }
}