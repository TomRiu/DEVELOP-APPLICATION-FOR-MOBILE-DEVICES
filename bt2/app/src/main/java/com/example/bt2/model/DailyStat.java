package com.example.bt2.model;

import java.util.Date;

public class DailyStat {
    private Date day;
    private double income;
    private double outcome;

    public DailyStat(Date day, double income, double outcome) {
        this.day = day;
        this.income = income;
        this.outcome = outcome;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getOutcome() {
        return outcome;
    }

    public void setOutcome(double outcome) {
        this.outcome = outcome;
    }
}
