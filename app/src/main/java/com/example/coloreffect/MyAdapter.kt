package com.example.coloreffect;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    // Здесь нам нужен только читатель данных
    private NoteDataReader noteDataReader;
    // Слушатель, который будет устанавливаться извне
    private OnMenuItemClickListener itemMenuClickListener;

    public MyAdapter(NoteDataReader noteDataReader) {
        this.noteDataReader = noteDataReader;
    }

    // Вызывается при создании новой карточки списка
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// Создаем новый элемент пользовательского интерфейса
// Через Inflater
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_item, parent, false);
// Здесь можно установить всякие параметры
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Привязываем данные к карточке
    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        holder.bind(noteDataReader.getPosition(position));
    }

    @Override
    public int getItemCount() {
        return noteDataReader.getCount();
    }

    // Установка слушателя
    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.itemMenuClickListener = onMenuItemClickListener;
    }

    // Интерфейс для обработки меню
    public interface OnMenuItemClickListener {
        void onItemEditClick(CityNote note);

        void onItemDeleteClick(CityNote note);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textNote;
        private CityNote note;

        public ViewHolder(View itemView) {
            super(itemView);
            textNote = itemView.findViewById(R.id.category_name_text_view);
// При тапе на элементе – вытащим  меню
            textNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemMenuClickListener != null) {
                        showPopupMenu(textNote);
                    }
                }
            });
        }

        public void bind(CityNote note) {
            this.note = note;
            textNote.setText(note.getTitle());
        }

        private void showPopupMenu(View view) {
// Покажем меню на элементе
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.context_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                // Обработка выбора пункта меню
                @Override
                public boolean onMenuItemClick(MenuItem item) {
// Делегируем обработку слушателю
                    switch (item.getItemId()) {
                        case R.id.menu_edit:
                            itemMenuClickListener.onItemEditClick(note);
                            return true;
                        case R.id.menu_delete:
                            itemMenuClickListener.onItemDeleteClick(note);
                            return true;
                    }
                    return false;
                }
            });
            popup.show();
        }


    }
}
