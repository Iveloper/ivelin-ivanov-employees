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

    public static List<Employee> readFile() {
        List<Employee> employeeList = new ArrayList<>();
        //Reading the file content line by line
        try {

            String[] employeeInfo;
            File file = new File(FILE_PATH);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);

            while (true) {
                String line = bufferedReader.readLine();
                if(line == null) break;

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
