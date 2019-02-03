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

    public static void work() {

        removeOneEmployeeProject(employeeList);
        getAllProjects(employeeList);
        removeDuplicates(commonHoursList);
        CommonHours ch = findEmployeesMostHours(commonHoursList);
        System.out.println("Employee " + ch.getEmp1Id() + " and Employee " + ch.getEmp2Id()
                + " have worked together the most - " + ch.getCommonDays() + " days.");
    }


    private static void removeOneEmployeeProject(List<Employee> employeeList) {
//da pribie kristiyan
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


    private static void allEmployeesWorkingForProject(Map<Integer, List<Employee>> employeesPerProject) {

        for (Map.Entry<Integer, List<Employee>> entry : employeesPerProject.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                for (int j = i + 1; j < entry.getValue().size(); j++) {
                    Service.employeesWithMostCommonHoursForProject(entry.getValue().get(i), entry.getValue().get(j));
                }
            }
        }
    }


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
                commonHoursList.add(new CommonHours(employee1.getEmpId(), employee2.getEmpId(), Math.abs(firstDiff)+1));
            } else {
                commonHoursList.add(new CommonHours(employee1.getEmpId(), employee2.getEmpId(), Math.abs(secondDiff)+1));
            }

        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

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

    private static CommonHours findEmployeesMostHours(List<CommonHours> commonHoursList) {
        commonHoursList.sort((CommonHours ch1, CommonHours ch2)
                -> Long.compare(ch2.getCommonDays(), ch1.getCommonDays()));

        return commonHoursList.get(0);
    }
}
