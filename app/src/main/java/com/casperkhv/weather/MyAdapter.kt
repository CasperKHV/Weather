package com.casperkhv.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    private val noteDataReader: NoteDataReader
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private var itemMenuClickListener: OnMenuItemClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(noteDataReader.getPosition(position))
    }

    override fun getItemCount(): Int {
        return noteDataReader.count
    }

    fun setOnMenuItemClickListener(onMenuItemClickListener: OnMenuItemClickListener?) {
        itemMenuClickListener = onMenuItemClickListener
    }

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
            val popup = PopupMenu(view.context, view)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.context_menu, popup.menu)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
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
            textNote.setOnClickListener {
                if (itemMenuClickListener != null) {
                    showPopupMenu(textNote)
                }
            }
        }
    }
}