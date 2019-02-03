package com.sirmasolutions.application.services;

import com.sirmasolutions.application.employees.CommonHours;
import com.sirmasolutions.application.employees.Employee;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Service {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static List<Employee> employeeList = FileOperator.readFile();
    private static List<CommonHours> commonHoursList = new ArrayList<>();

    /* Method "work" calls every other method in the Service class and prints to the console the two employees
     * who worked the most together. Also the sum of the days they spent working together. */
    public static void work() {

        removeOneEmployeeProject(employeeList);
        getAllProjects(employeeList);
        removeDuplicates(commonHoursList);
        CommonHours ch = findEmployeesMostHours(commonHoursList);
        System.out.println("Employee " + ch.getEmp1Id() + " and Employee " + ch.getEmp2Id()
                + " have worked together the most - " + ch.getCommonDays() + " days.");
    }

    /* This method implements the first sorting in order to find the two employees.
     * What this method does is to find if there are projects in the list that no more than one person is working on
     * and if there are any, it deletes them. */
    private static void removeOneEmployeeProject(List<Employee> employeeList) {

        for (int i = 0; i < employeeList.size(); i++) {

            boolean isProjectForSingleEmployee = true;
            Employee currentEmployee = employeeList.get(i);

            for (Employee nextEmployee : employeeList) {
                if (currentEmployee.getProjectId() == nextEmployee.getProjectId()
                        && currentEmployee.getEmpId() != nextEmployee.getEmpId()) {
                    isProjectForSingleEmployee = false;
                    break;
                }
            }
            if (isProjectForSingleEmployee) {
                employeeList.remove(currentEmployee);
            }
        }
    }

    /* This method stores all employees for a specific project in a HashMap.
     * Every key contains the project's ID, every value contains as many 'Employee' objects, as there are working
     * for the specific project. */
    private static void getAllProjects(List<Employee> employeeList) {

        Map<Integer, List<Employee>> employeesPerProject = new HashMap<>();

        for (Employee employee : employeeList) {

            int projectId = employee.getProjectId();

            if (!employeesPerProject.containsKey(projectId)) {
                employeesPerProject.put(employee.getProjectId(), new ArrayList<>());
            }

            employeesPerProject.get(projectId).add(employee);
        }

        Service.allEmployeesWorkingForProject(employeesPerProject);
    }

    /* This method iterates through every project in the HashMap.
     *  For every project, we are iterating through the employees working for it.
     *  And in the third for loop we compare every employee with each other. in
     *  order to find the most common days they were working on the current project we
     *  pass two objects - one for both employees to the 'employeesWithMostCommonHoursForProject'
     *  method. */
    private static void allEmployeesWorkingForProject(Map<Integer, List<Employee>> employeesPerProject) {

        for (Map.Entry<Integer, List<Employee>> entry : employeesPerProject.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                for (int j = i + 1; j < entry.getValue().size(); j++) {
                    Service.employeesWithMostCommonHoursForProject(entry.getValue().get(i), entry.getValue().get(j));
                }
            }
        }
    }

    /* This method takes two objects of type Employee as parameters.
     *  Firstly it parses the String type dates from the file to a Date type.
     *  Simplest way to find the days two employees were working together is to find the difference
     *  between the end date of Employee 1 and the start date of Employee 2 or vice versa.
     *  It strongly depends on which employee started first and if there are common days at all.
     *  Last, but not least, we take which one of the differences in days is greater.
     *  The greater value is what we need, and we are ready to add a new Object to the List of type
     *  (check employees -> class CommonHours).
     *  We add the two Employees' IDs and commonDays of which we get the absolute value, because in a case where we check
     *  the difference of (earlier date - later date) we will get the days difference as a negative number.
     *  Plus, we add +1 to the absolute value, because in our case, we do not count the end date,which is mandatory. */
    private static void employeesWithMostCommonHoursForProject(Employee employee1, Employee employee2) {

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        try {

            Date employee1StartDate = sdf.parse(employee1.getStartDate());
            Date employee1EndDate = sdf.parse(employee1.getEndDate());
            Date employee2StartDate = sdf.parse(employee2.getStartDate());
            Date employee2EndDate = sdf.parse(employee2.getEndDate());
            long firstDiff = ChronoUnit.DAYS.between(employee1EndDate.toInstant(), employee2StartDate.toInstant());
            long secondDiff = ChronoUnit.DAYS.between(employee2EndDate.toInstant(), employee1StartDate.toInstant());

            if (employee1StartDate.compareTo(employee2EndDate) > 0
                    || employee2StartDate.compareTo(employee1EndDate) > 0
                    || employee1.getEmpId() == employee2.getEmpId()) {
                return;
            }

            if (firstDiff > secondDiff) {
                commonHoursList.add(new CommonHours(employee1.getEmpId(), employee2.getEmpId(), Math.abs(firstDiff) + 1));
            } else {
                commonHoursList.add(new CommonHours(employee1.getEmpId(), employee2.getEmpId(), Math.abs(secondDiff) + 1));
            }

        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

    /* This method iterates through the newly created list, which we filled in the previous method,
     *  and checks if there are employees with same IDs for different Projects, if there are any -
     * it sums the common days for these objects and deletes them afterwards. */
    private static List<CommonHours> removeDuplicates(List<CommonHours> commonHoursList) {

        for (int i = 0; i < commonHoursList.size(); i++) {
            for (int j = i + 1; j < commonHoursList.size(); j++) {
                if ((commonHoursList.get(i).getEmp1Id() == commonHoursList.get(j).getEmp1Id()
                        && commonHoursList.get(i).getEmp2Id() == commonHoursList.get(j).getEmp2Id())
                        || (commonHoursList.get(i).getEmp1Id() == commonHoursList.get(j).getEmp2Id()
                        && commonHoursList.get(i).getEmp2Id() == commonHoursList.get(j).getEmp1Id())) {

                    commonHoursList.get(i).setCommonDays(commonHoursList.get(j).getCommonDays());
                    commonHoursList.remove(j);

                }
            }
        }
        return commonHoursList;
    }

    /*This method takes the list of type CommonHours without the duplicates, and the summed common days and
    * sorts every object's common days in descending order, so at the 0 index, we get the object with most
    * common days, which is the solution to our task.*/
    private static CommonHours findEmployeesMostHours(List<CommonHours> commonHoursList) {

        commonHoursList.sort((CommonHours ch1, CommonHours ch2)
                -> Long.compare(ch2.getCommonDays(), ch1.getCommonDays()));

        return commonHoursList.get(0);
    }
}
