package com.rain;

import com.rain.config.SpringAppInitializer;
import com.rain.entity.Student;
import com.rain.repository.StudentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author rain
 * created on 2018/4/23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringAppInitializer.class)
public class JpaTest {
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void testQuery() {
        Student student = studentRepository.findStudentByStudentName("rain");
        System.out.println(student);
    }

    @Test
    public void testPageable() {
        Sort sort = new Sort(Sort.Direction.DESC, "studentId");
        Pageable pageable = new PageRequest(PAGE, SIZE, sort);
        Page<Student> students = studentRepository.findAll(pageable);
        List<Student> contents = students.getContent();
        for (Student student : contents) {
            System.out.println(student);
        }
    }
}