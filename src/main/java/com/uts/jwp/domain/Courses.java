package com.uts.jwp.domain;

public class Courses {
    private String coursesCode;
    private String coursesName;
    private String sumSKS;
    private String faculty;

    public Courses(){

    }
    public String getcoursesCode(){
        return coursesCode;
    }
    public void setcourseCode(String coursesCode){
        this.coursesCode = coursesCode;
    }
    public String getcoursesName(){
        return coursesName;
    }
    public void setcoursesName(String coursesName){
        this.coursesName = coursesName;
    }
    public String getsumSKS(){
        return sumSKS;
    }
    public void setsumSKS(String sumSKS){
        this.sumSKS = sumSKS;
    }
    public String getfaculty(){
        return faculty;
    }
    public void setfaculty(String faculty){
        this.faculty = faculty;
    }
}
