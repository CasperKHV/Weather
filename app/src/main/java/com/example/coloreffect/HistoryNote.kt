package com.example.coloreffect;

// Класс-отражение строк из таблицы
public class HistoryNote {
    private long id;
    private String date; //день сбора данных
    private String title; //город
    private String description; //сохранённые данные о погоде

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
