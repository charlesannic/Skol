package com.android.charl.skol.java;

import java.util.Date;

/**
 * Created by charl on 29/11/2016.
 */

public class Homework {

    private double id;
    private String name;
    private String description;
    private String categorie;
    private Course itsCourse;
    private boolean isImportant;
    private Date date;
    private Note note;

    public Homework(double id, String name, String description, String categorie, Course itsCourse, boolean isImportant, Date date, Note note) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categorie = categorie;
        this.itsCourse = itsCourse;
        this.isImportant = isImportant;
        this.date = date;
        this.note = note;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Course getItsCourse() {
        return itsCourse;
    }

    public void setItsCourse(Course itsCourse) {
        this.itsCourse = itsCourse;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }
}
