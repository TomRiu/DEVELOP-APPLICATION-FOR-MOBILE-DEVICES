package com.example.bt2.model;

import java.util.Date;

public class Transaction {
    private int id;
    private String name;
    private CategoryInOut category;
    private double amount;
    private Date day;
    private String note;

    public Transaction() {
    }

    public Transaction(int id, String name, CategoryInOut category, double amount, Date day, String note) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.day = day;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryInOut getCategory() {
        return category;
    }

    public void setCategory(CategoryInOut category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
