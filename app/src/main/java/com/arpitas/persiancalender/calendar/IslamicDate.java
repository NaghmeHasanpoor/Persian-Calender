package com.arpitas.persiancalender.calendar;

import com.arpitas.persiancalender.Constants;

public class IslamicDate extends AbstractDate {
    private int day;
    private int month;
    private int year;

    public IslamicDate(int year, int month, int day) {
        setYear(year);
        this.day = 1;
        setMonth(month);
        setDayOfMonth(day);
    }

    public int getDayOfMonth() {
        return day;
    }

    public void setDayOfMonth(int day) {
        // TODO This check is not very exact! But it's not worth of it
        if (day < 1 || day > 30)
            throw new DayOutOfRangeException(
                    Constants.DAY + " " + day + " " + Constants.IS_OUT_OF_RANGE);

        this.day = day;
    }

    public int getDayOfWeek() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if (month < 1 || month > 12)
            throw new MonthOutOfRangeException(
                    Constants.MONTH + " " + month + " " + Constants.IS_OUT_OF_RANGE);

        setDayOfMonth(day);

        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (year == 0)
            throw new YearOutOfRangeException(Constants.YEAR_0_IS_INVALID);

        this.year = year;
    }

    public String getEvent() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    public boolean isLeapYear() {
        throw new RuntimeException(Constants.NOT_IMPLEMENTED_YET);
    }

    @Override
    public IslamicDate clone() {
        return new IslamicDate(getYear(), getMonth(), getDayOfMonth());
    }
}
