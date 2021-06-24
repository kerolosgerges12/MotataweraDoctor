package com.kmk.motatawera.doctor.model;

public class SubjectModel {
    private String id;
    private String doctor_id;
    private String name;
    private int branch;
    private int department;
    private int grad;


    public SubjectModel() {
    }

    public SubjectModel(String id, String doctor_id, String name, int branch, int department, int grad) {
        this.id = id;
        this.doctor_id = doctor_id;
        this.name = name;
        this.branch = branch;
        this.department = department;
        this.grad = grad;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public void setGrad(int grad) {
        this.grad = grad;
    }

    public int getBranch() {
        return branch;
    }

    public int getDepartment() {
        return department;
    }

    public int getGrad() {
        return grad;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
