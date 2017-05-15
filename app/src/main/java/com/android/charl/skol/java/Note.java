package com.android.charl.skol.java;

/**
 * Created by charl on 29/11/2016.
 */

public class Note {

    private Homework itsHomework;
    private double note;
    private double coefficient;

    public Note(Homework itsHomework, double note, double coefficient) {
        this.itsHomework = itsHomework;
        this.note = note;
        this.coefficient = coefficient;
    }

    public Homework getItsHomework() {
        return itsHomework;
    }

    public void setItsHomework(Homework itsHomework) {
        this.itsHomework = itsHomework;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }
}
