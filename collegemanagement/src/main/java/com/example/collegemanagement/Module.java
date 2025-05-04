package com.example.collegemanagement;

public class Module {
    private String name;
    private double coefficient;
    private boolean hasTD;
    private boolean hasTP;

    private double tdMark = -1;   // Default: -1 means not filled
    private double tpMark = -1;
    private double examMark = -1;

    public Module(String name, double coefficient, boolean hasTD, boolean hasTP) {
        this.name = name;
        this.coefficient = coefficient;
        this.hasTD = hasTD;
        this.hasTP = hasTP;
    }

    // --- Getters ---
    public String getName() {
        return name;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public boolean hasTD() {
        return hasTD;
    }

    public boolean hasTP() {
        return hasTP;
    }

    public double getTdMark() {
        return tdMark;
    }

    public double getTpMark() {
        return tpMark;
    }

    public double getExamMark() {
        return examMark;
    }

    // --- Setters ---
    public void setTdMark(double tdMark) {
        this.tdMark = tdMark;
    }

    public void setTpMark(double tpMark) {
        this.tpMark = tpMark;
    }

    public void setExamMark(double examMark) {
        this.examMark = examMark;
    }
}
