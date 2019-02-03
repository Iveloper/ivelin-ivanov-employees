package com.sirmasolutions.application.employees;

public class CommonHours {

    private int emp1Id;
    private int emp2Id;
    private long commonDays;

    public CommonHours(int emp1Id, int emp2Id, long commonDays) {
        this.emp1Id = emp1Id;
        this.emp2Id = emp2Id;
        this.commonDays = commonDays;
    }

    public int getEmp1Id() {
        return emp1Id;
    }

    public int getEmp2Id() {
        return emp2Id;
    }

    public long getCommonDays() {
        return commonDays;
    }

    public void setCommonDays(long commonDays) {
        this.commonDays += commonDays;
    }
}
