package com.sirmasolutions.application.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatesOperator {
    public static Date parseDate(String date) {
        Date actualDate = new Date();
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            actualDate = format.parse(date);
            return actualDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return actualDate;
    }
}
