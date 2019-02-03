package com.sirmasolutions.application.services;

import com.sirmasolutions.application.employees.Employee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileOperator {

    private static final String FILE_PATH = "src\\main\\resources\\employees.txt";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String LINE_SPLITTER = ",";

    /* The only job for this class is to read the file with readFile() method.
     *  This method iterates through every line of the employees.txt file, until it finds line with null value.
     *  It trims the spaces between the information and checks for NULL values in dates, if there is NULL it replace it
     *  with the current date. After those operations it parser the employees'
     *  ids to int and the two dates to string types.
     *  And last, but not least, it stores the elements of every line in a new object of type
     *  (check employees -> class Employee) and stores it to the list 'employeeList'. */
    public static List<Employee> readFile() {

        List<Employee> employeeList = new ArrayList<>();

        try {
            String[] employeeInfo;
            File file = new File(FILE_PATH);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);

            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) break;

                employeeInfo = line.split(LINE_SPLITTER);
                int employeeId = Integer.parseInt(employeeInfo[0].trim());
                int projectId = Integer.parseInt(employeeInfo[1].trim());
                String startDate = employeeInfo[2].trim();
                String endDate = employeeInfo[3].trim().equalsIgnoreCase("null")
                        ? dtf.format(LocalDate.now())
                        : employeeInfo[3].trim();

                employeeList.add(new Employee(employeeId, projectId, startDate, endDate));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return employeeList;
    }
}
