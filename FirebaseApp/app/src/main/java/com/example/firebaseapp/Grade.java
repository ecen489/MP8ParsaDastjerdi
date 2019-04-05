package com.example.firebaseapp;

import java.io.Serializable;


public class Grade implements Serializable {

    public int course_id;
    public String course_name;
    public String grade;
    public int student_id;

    public Grade() {
        // default constructor
    }

    public Grade(int course, String name, String grad, int id){
        this.course_id = course;
        this.course_name = name;
        this.grade = grad;
        this.student_id = id;
    }
}
