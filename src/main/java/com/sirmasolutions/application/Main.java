package com.sirmasolutions.application;

import com.sirmasolutions.application.services.Service;

/*  NOTE:
 *  In order for the program to run successfully, we need to store the .txt file in the folder 'resources'.
 *  The name "employees.txt" for the file is mandatory! */
public class Main {
    public static void main(String[] args) {
        Service.work();
    }
}
