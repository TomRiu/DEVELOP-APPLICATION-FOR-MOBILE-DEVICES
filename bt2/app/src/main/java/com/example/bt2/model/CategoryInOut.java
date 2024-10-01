package com.example.bt2.model;

public class CategoryInOut {
    private int id;
    private int idInOut;
    private Category category;

    public CategoryInOut() {
    }

    public CategoryInOut(int id, int idInOut, Category category) {
        this.id = id;
        this.idInOut = idInOut;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdInOut() {
        return idInOut;
    }

    public void setIdInOut(int idInOut) {
        this.idInOut = idInOut;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
