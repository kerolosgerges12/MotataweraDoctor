package com.kmk.motatawera.doctor.model;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Timer;

public class QuizModel {
    private String id;
    private String title;
    private Boolean QuizActive;
    private Boolean QuizFinished;
    private String doctor_id;
    private String subject_id;
    private String subject_name;
    private int subject_branch;
    private int subject_department;
    private int subject_grad;
     @ServerTimestamp
     private Date time;


    public QuizModel() {
    }


    public QuizModel(String id, String title, Boolean quizActive, Boolean quizFinished, String doctor_id, String subject_id, String subject_name, int subject_branch, int subject_department, int subject_grad, Date time) {
        this.id = id;
        this.title = title;
        QuizActive = quizActive;
        QuizFinished = quizFinished;
        this.doctor_id = doctor_id;
        this.subject_id = subject_id;
        this.subject_name = subject_name;
        this.subject_branch = subject_branch;
        this.subject_department = subject_department;
        this.subject_grad = subject_grad;
        this.time = time;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getQuizActive() {
        return QuizActive;
    }

    public void setQuizActive(Boolean quizActive) {
        QuizActive = quizActive;
    }

    public Boolean getQuizFinished() {
        return QuizFinished;
    }

    public void setQuizFinished(Boolean quizFinished) {
        QuizFinished = quizFinished;
    }


    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public int getSubject_branch() {
        return subject_branch;
    }

    public void setSubject_branch(int subject_branch) {
        this.subject_branch = subject_branch;
    }

    public int getSubject_department() {
        return subject_department;
    }

    public void setSubject_department(int subject_department) {
        this.subject_department = subject_department;
    }

    public int getSubject_grad() {
        return subject_grad;
    }

    public void setSubject_grad(int subject_grad) {
        this.subject_grad = subject_grad;
    }
}
