package com.casperkhv.weather

import android.app.Activity
import android.content.Intent
import android.content.res.Resources.NotFoundException
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable

class WeatherResultFragment : Fragment(), View.OnClickListener {
    private var noteDataSourceForHistory: NoteDataSourceForHistory? = null
    private var noteDataReaderForHistory: NoteDataReaderForHistory? = null
    private var adapterRV: MyAdapter? = null
    private val historyListListener: HistoryListListener? = null
    var city: String? = null
    private var history: String? = null
    private var dateForHistory: String? = null
    private var dataForBundle: DataForBundle? = null
    private val weatherText by bindView<TextView>(R.id.textview_weather)
    private val shareButton by bindView<Button>(R.id.button_share)
    private var message: String? = null
    private val photoWeather by bindView<ImageView>(R.id.photoWeather)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather_result, container, false)
        noteDataSourceForHistory = NoteDataSourceForHistory(requireActivity())
        val citiesCategoriesRecyclerView: RecyclerView =
            view.findViewById(R.id.recycler_view_history)
        val layoutManager = LinearLayoutManager(requireActivity())
        layoutManager.orientation = RecyclerView.VERTICAL
        citiesCategoriesRecyclerView.layoutManager = layoutManager
        adapterRV = MyAdapter()
        citiesCategoriesRecyclerView.adapter = adapterRV
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shareButton.setOnClickListener(this)
        if (savedInstanceState != null) {
            dataForBundle = savedInstanceState.getSerializable(DATA_FOR_BUNDLE) as DataForBundle?
        }
        val photoWeatherCode: String?
        if (dataForBundle != null) {
            message = dataForBundle!!.message
            photoWeatherCode = dataForBundle!!.iconCode
            city = dataForBundle!!.city
            history = dataForBundle!!.history
            dateForHistory = dataForBundle!!.dateForHistory
        } else {
            throw RuntimeException("DataForBundle is empty")
        }
        if (photoWeatherCode != null) {
            var imageId = R.drawable.troll_weather
            when (photoWeatherCode) {
                "01d" -> imageId = R.drawable.a01d
                "01n" -> imageId = R.drawable.a01n
                "02d" -> imageId = R.drawable.a02d
                "02n" -> imageId = R.drawable.a02n
                "03d" -> imageId = R.drawable.a03d
                "03n" -> imageId = R.drawable.a03n
                "04d" -> imageId = R.drawable.a04d
                "04n" -> imageId = R.drawable.a04n
                "09d" -> imageId = R.drawable.a09d
                "09n" -> imageId = R.drawable.a09n
                "10d" -> imageId = R.drawable.a10d
                "10n" -> imageId = R.drawable.a10n
                "11d" -> imageId = R.drawable.a11d
                "11n" -> imageId = R.drawable.a11n
                "13d" -> imageId = R.drawable.a13d
                "13n" -> imageId = R.drawable.a13n
                "50d" -> imageId = R.drawable.a50d
                "50n" -> imageId = R.drawable.a50n
                else -> {}
            }
            try {
                photoWeather.setImageResource(imageId)
            } catch (e: NotFoundException) {
                e.printStackTrace()
                photoWeather.setImageResource(R.drawable.troll_weather)
            }
            registerForContextMenu(photoWeather)
        }
        if (message != null) {
            weatherText.text = message
            val intentResult = Intent()
            intentResult.putExtra(
                CitiesListFragment.Companion.RESULT_OK_STRING,
                resources.getString(R.string.repeat_choose_city)
            )
            requireActivity().setResult(Activity.RESULT_OK, intentResult)
        }
        if (city != null && dateForHistory != null && history != null) {
            initDataSource()
            Log.d("кол-во", Integer.toString(noteDataReaderForHistory!!.count))
            Log.d("дата", dateForHistory!!)
            if (noteDataReaderForHistory!!.getCountForAvoidRepetition(
                    city!!,
                    dateForHistory!!
                ) == 0
            ) {
                noteDataSourceForHistory!!.addNote(dateForHistory, city, history)
            } else {
                noteDataSourceForHistory!!.editNote(dateForHistory!!, city!!, history)
            }
        }
        val fragmentManager = childFragmentManager
        var checkBoxWeatherResultFragment =
            fragmentManager.findFragmentByTag(INNER_FRAGMENT_TAG) as CheckBoxWeatherResultFragment?
        if (checkBoxWeatherResultFragment == null) {
            val pressure = dataForBundle!!.resultPressure
            val feels = dataForBundle!!.resultFeels
            val humidity = dataForBundle!!.resultHumidity
            if (pressure != null || feels != null || humidity != null) {
                val fragmentTransaction = fragmentManager.beginTransaction()
                checkBoxWeatherResultFragment =
                    CheckBoxWeatherResultFragment.Companion.newInstance(pressure, feels, humidity)
                fragmentTransaction.replace(
                    R.id.inner_fragment_container,
                    checkBoxWeatherResultFragment,
                    INNER_FRAGMENT_TAG
                )
                fragmentTransaction.commit()
            }
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_share) {
            val intentShare = Intent(Intent.ACTION_SEND)
            intentShare.type = "text/plain"
            if (message != null) {
                intentShare.putExtra(Intent.EXTRA_TEXT, message)
            }
            val packageManager = requireActivity().packageManager
            if (!packageManager.queryIntentActivities(intentShare, 0).isEmpty()) {
                startActivity(intentShare)
                shareButton.setBackgroundColor(Color.GREEN)
            } else {
                shareButton.setBackgroundColor(Color.RED)
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = requireActivity().menuInflater
        inflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.context_menu_hide -> {
                photoWeather.visibility = View.GONE
                true
            }

            R.id.context_menu_set_background -> {
                photoWeather.setBackgroundColor(Color.BLUE)
                true
            }

            R.id.context_menu_delete_background -> {
                photoWeather.setBackgroundColor(Color.TRANSPARENT)
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (dataForBundle != null) {
            outState.putSerializable(DATA_FOR_BUNDLE, dataForBundle)
        }
        super.onSaveInstanceState(outState)
    }

    internal interface HistoryListListener {
        fun onListItemClick(id: Int, dataForBundle: DataForBundle?, descriptionText: TextView?)
        fun transfer(
            noteDataSourceForHistory: NoteDataSourceForHistory?,
            noteDataReaderForHistory: NoteDataReaderForHistory?,
            adapter: MyAdapter?
        )
    }

    inner class MyViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup?) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item_history, parent, false)),
        View.OnClickListener {
        private val categoryNameTextView: TextView
        private val photo: ImageView? = null
        private val textNote: TextView? = null
        private var note: HistoryNote? = null


        fun bind(note: HistoryNote?) {
            this.note = note
            categoryNameTextView.text = note!!.date
        }

        override fun onClick(view: View) {
            val factory = LayoutInflater.from(requireActivity())
            val alertView = factory.inflate(R.layout.layout_history_description, null)
            val builder = AlertDialog.Builder(requireActivity())
            builder.setView(alertView)
            val title = TextView(requireActivity())
            title.text = note!!.date
            title.setBackgroundColor(Color.DKGRAY)
            title.setPadding(10, 10, 10, 10)
            title.gravity = Gravity.CENTER
            title.setTextColor(Color.WHITE)
            title.textSize = 20f
            builder.setCustomTitle(title)
            val editTextNote = alertView.findViewById<TextView>(R.id.textDescriptionHistory)
            editTextNote.text = note!!.descriptionWeather
            builder.setNegativeButton(R.string.Close, null)
            builder.show()
        }

        private fun showPopupMenu(view: View) {
            val popup = PopupMenu(view.context, view)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.context_menu_for_history, popup.menu)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_for_history_delete -> {
                        deleteElement(note)
                        return@OnMenuItemClickListener true
                    }

                    R.id.menu_for_history_delete_all -> {
                        deleteHistoryForCity(city)
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
                itemView.findViewById<View>(R.id.item_of_history_name_text_view) as TextView

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
            holder.bind(noteDataReaderForHistory!!.getPosition(position))
        }

        override fun getItemCount(): Int {
            return noteDataReaderForHistory!!.count
        }
    }

    private fun deleteElement(note: HistoryNote?) {
        noteDataSourceForHistory!!.deleteNote(note)
        dataUpdated()
    }

    private fun deleteHistoryForCity(city: String?) {
        noteDataSourceForHistory!!.deleteHistoryForCity(city)
        dataUpdated()
    }

    private fun dataUpdated() {
        noteDataReaderForHistory!!.Refresh(city)
        adapterRV!!.notifyDataSetChanged()
    }

    private fun initDataSource() {
        noteDataSourceForHistory!!.open(city)
        noteDataReaderForHistory = noteDataSourceForHistory!!.noteDataReaderForHistory
    }

    companion object {
        private const val INNER_FRAGMENT_TAG = "inner_fragment_tag"
        const val DATA_FOR_BUNDLE = "data for bundle"
        fun newInstance(dataForBundle: Serializable?): WeatherResultFragment {
            val fragment = WeatherResultFragment()
            fragment.dataForBundle = dataForBundle as DataForBundle?
            return fragment
        }
    }
}