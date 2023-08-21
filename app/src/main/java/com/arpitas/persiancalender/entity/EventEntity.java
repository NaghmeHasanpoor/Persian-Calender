package com.arpitas.persiancalender.entity;


import com.arpitas.persiancalender.calendar.PersianDate;

public class EventEntity {
    private PersianDate date;
    private String title;
    private boolean holiday;

    public EventEntity() {
    }

    public EventEntity(PersianDate date, String title, boolean holiday) {
        this.date = date;
        this.title = title;
        this.holiday = holiday;
    }

    public void setDate(PersianDate date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    public PersianDate getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public boolean isHoliday() {
        return holiday;
    }

}