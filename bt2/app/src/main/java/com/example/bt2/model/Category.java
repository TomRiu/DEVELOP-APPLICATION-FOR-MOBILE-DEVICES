package com.example.bt2.model;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private int id;
    private String name;
    private Category parent;
    private String note;

    public Category() {
    }

    public Category(int id, String name, Category parent, String note) {
        this.id = id;
        this.name = name;
        this.parent = parent;
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

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
