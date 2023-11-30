package com.uts.jwp.domain;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.Size;


public class Courses {

    @NotBlank(message = "courseCode name is required")
    private String courseCode;

    @NotBlank(message = "CourseName is required")
    @Size(min = 5, max = 10)
    private String courseName;

    @NotNull(message = "Sumsks is required")
    private int sumSKS;

    @NotBlank(message = "Faculty is required")
    private String faculty;

    public Courses() {

    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getSumSKS() {
        return sumSKS;
    }

    public void setSumSKS(int sumSKS) {
        this.sumSKS = sumSKS;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    
 

}