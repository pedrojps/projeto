package com.example.myapplication.common.comparator;

import java.util.Comparator;
import java.util.Date;

public class DateComparator implements Comparator<Date> {

    @Override
    public int compare(Date d1, Date d2) {
        return d1.compareTo(d2);
    }

    @Override
    public Comparator<Date> reversed() {
        return (d1, d2) -> d2.compareTo(d1);
    }

    public static DateComparator create() {
        return new DateComparator();
    }
}
