package com.rain.repository;

import com.rain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JpaRepository是什么
 *
 *
 * @author rain
 * created on 2018/4/22
 */
public interface StudentRepository extends JpaRepository<Student, Integer> {
    /**
     * 通过学生姓名查找学生信息, 学生姓名唯一
     * @param studentName 学生姓名
     * @return 学生信息
     */
    Student findStudentByStudentName(String studentName);
}