package com.kmk.motatawera.doctor.model;

public class DoctorModel {

    private String id;
    private String name;
    private String email;
    private String password;
    private int branch;
    private boolean isDoctor;
    private boolean isApproved;
    private boolean isDisable;
    private boolean isDeleted;


    public DoctorModel() {
    }

    public DoctorModel(String id, String name, String email, String password, int branch, boolean isDoctor, boolean isApproved, boolean isDisable, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.branch = branch;
        this.isDoctor = isDoctor;
        this.isApproved = isApproved;
        this.isDisable = isDisable;
        this.isDeleted = isDeleted;
    }

    public DoctorModel(String id, String name, String email, String password, String branch, boolean isDoctor, boolean isDoctor1, boolean isDoctor2, boolean isDoctor3) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }


    public int getBranch() {
        return branch;
    }


    public boolean isDoctor() {
        return isDoctor;
    }

    public void setDoctor(boolean doctor) {
        isDoctor = doctor;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean isDisable() {
        return isDisable;
    }

    public void setDisable(boolean disable) {
        isDisable = disable;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
