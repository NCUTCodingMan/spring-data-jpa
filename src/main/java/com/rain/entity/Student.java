package com.rain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 实体与数据库表需要建立一一对应的关系
 *
 * @author rain
 * created on 2018/4/23
 */
@Table(name = "tab_student")
@Entity
public class Student {
    @Id
    @Column(name = "student_id")
    private Integer studentId;
    @Column(name = "student_name")
    private String studentName;
    @Column(name = "sex")
    private Integer sex;

    public Student() {}

    public Student(Integer studentId, String studentName, Integer sex) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.sex = sex;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", sex=" + sex +
                '}';
    }
}
