package com.sirmasolutions.application.employees;

public class Employee {
    private int empId;
    private int projectId;
    private String startDate;
    private String endDate;

    public Employee(int empId, int projectId, String startDate, String endDate) {
        this.empId = empId;
        this.projectId = projectId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getEmpId() {
        return empId;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
