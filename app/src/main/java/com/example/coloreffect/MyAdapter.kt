package com.example.coloreffect

//import com.example.coloreffect.Controller.start
import com.example.coloreffect.NoteDataReader
import androidx.recyclerview.widget.RecyclerView
import com.example.coloreffect.R
import com.example.coloreffect.CityNote
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
import androidx.core.view.GravityCompat
import com.example.coloreffect.FragmentForNV
import android.os.Environment
import com.bumptech.glide.Glide
import android.database.sqlite.SQLiteOpenHelper
import com.example.coloreffect.DatabaseHelper
import android.database.sqlite.SQLiteDatabase
import kotlin.Throws
import android.content.ContentValues
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View.OnLongClickListener
import com.example.coloreffect.WeatherSpec
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
import android.widget.*
import com.example.coloreffect.DatabaseHelperForHistory
import com.example.coloreffect.MainForGSON
import com.example.coloreffect.WeatherForGSON
import com.example.coloreffect.WindForGSON

class MyAdapter(  // Здесь нам нужен только читатель данных
    private val noteDataReader: NoteDataReader
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    // Слушатель, который будет устанавливаться извне
    private var itemMenuClickListener: OnMenuItemClickListener? = null

    // Вызывается при создании новой карточки списка
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
// Создаем новый элемент пользовательского интерфейса
// Через Inflater
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_list_item, parent, false)
        // Здесь можно установить всякие параметры
        return ViewHolder(v)
    }

    // Привязываем данные к карточке
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(noteDataReader.getPosition(position))
    }

    override fun getItemCount(): Int {
        return noteDataReader.count
    }

    // Установка слушателя
    fun setOnMenuItemClickListener(onMenuItemClickListener: OnMenuItemClickListener?) {
        itemMenuClickListener = onMenuItemClickListener
    }

    // Интерфейс для обработки меню
    interface OnMenuItemClickListener {
        fun onItemEditClick(note: CityNote?)
        fun onItemDeleteClick(note: CityNote?)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textNote: TextView
        private var note: CityNote? = null
        fun bind(note: CityNote?) {
            this.note = note
            if (note != null) {
                textNote.text = note.title
            }
        }

        private fun showPopupMenu(view: View) {
// Покажем меню на элементе
            val popup = PopupMenu(view.context, view)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.context_menu, popup.menu)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                // Обработка выбора пункта меню
                // Делегируем обработку слушателю
                when (item.itemId) {
                    R.id.menu_edit -> {
                        itemMenuClickListener!!.onItemEditClick(note)
                        return@OnMenuItemClickListener true
                    }
                    R.id.menu_delete -> {
                        itemMenuClickListener!!.onItemDeleteClick(note)
                        return@OnMenuItemClickListener true
                    }
                }
                false
            })
            popup.show()
        }

        init {
            textNote = itemView.findViewById(R.id.category_name_text_view)
            // При тапе на элементе – вытащим  меню
            textNote.setOnClickListener {
                if (itemMenuClickListener != null) {
                    showPopupMenu(textNote)
                }
            }
        }
    }
}