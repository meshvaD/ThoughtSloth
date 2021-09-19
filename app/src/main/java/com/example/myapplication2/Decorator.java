package com.example.myapplication2;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class Decorator implements DayViewDecorator {

    private final int[] colors;
    private final CalendarDay date;


    public Decorator(CalendarDay date, int[] colors) {
        //this.color = color;
        this.date = date;
        this.colors = colors;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date.getYear() == day.getYear() &&
                date.getMonth() == day.getMonth() &&
                date.getDay() == day.getDay();
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan((new CustmMultipleDotSpan(5, colors)));
    }


}