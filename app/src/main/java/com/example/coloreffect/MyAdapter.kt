package com.example.coloreffect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(  // Здесь нам нужен только читатель данных
    private val noteDataReader: NoteDataReader
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    // Слушатель, который будет устанавливаться извне
    private var itemMenuClickListener: OnMenuItemClickListener? = null

    // Вызывается при создании новой карточки списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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
            textNote.text = note!!.title
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