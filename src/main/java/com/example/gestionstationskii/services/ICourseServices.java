package com.example.gestionstationskii.services;


import com.example.gestionstationskii.entities.Course;

import java.util.List;

public interface ICourseServices {

    String assignInstructorToCourse(Long instructorId, Long courseId);

    List<Course> retrieveAllCourses();

    Course  addCourse(Course  course);

    Course updateCourse(Course course);

    Course retrieveCourse(Long numCourse);

    String assignCourseToRegistration(Long courseId, Long registrationId);
}
