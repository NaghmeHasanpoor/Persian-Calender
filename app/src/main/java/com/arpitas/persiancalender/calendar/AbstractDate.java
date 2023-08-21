package com.arpitas.persiancalender.calendar;

public abstract class AbstractDate {

    public abstract int getYear();

    public abstract void setYear(int year);

    public abstract int getMonth();

    public abstract void setMonth(int month);

    public abstract int getDayOfMonth();

    public abstract void setDayOfMonth(int day);

    public abstract int getDayOfWeek();

    public abstract String getEvent();

    public abstract boolean isLeapYear();

    public abstract AbstractDate clone();
}
