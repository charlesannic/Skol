package com.android.charl.skol.java;

import java.util.ArrayList;

/**
 * Created by charl on 29/11/2016.
 */

public class Course {

    private long id;
    private String name;
    private Color color;
    private ArrayList<Schedule> schedules;
    private Teacher teacher;
    private double coefficient;
    private int notification;

    public Course(long id, String name, Color color, ArrayList<Schedule> schedules, Teacher teacher, double coefficient, int notification) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.schedules = schedules;
        this.teacher = teacher;
        this.coefficient = coefficient;
        this.notification = notification;
    }

    public Course(String name, Color color, ArrayList<Schedule> schedules, Teacher teacher, double coefficient, int notification) {
        this.name = name;
        this.color = color;
        this.schedules = schedules;
        this.teacher = teacher;
        this.coefficient = coefficient;
        this.notification = notification;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(ArrayList<Schedule> schedules) {
        this.schedules = schedules;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }
}
