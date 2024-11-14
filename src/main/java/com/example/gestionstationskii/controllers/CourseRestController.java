package com.example.gestionstationskii.controllers;

import com.example.gestionstationskii.entities.Course;
import com.example.gestionstationskii.entities.TypeCourse;
import com.example.gestionstationskii.entities.Support;
import com.example.gestionstationskii.services.ICourseServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.stream.Collectors;


@Tag(name = "\uD83D\uDCDA Course Management")
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseRestController {

    private final ICourseServices courseServices;

    // DTO class for Course data
    public static class CourseDTO {

        String typeCourse;
        String support;
        float price;
        int level;
        int timeSlot;

    }

    // Helper method to convert CourseDTO to Course entity
    private Course toEntity(CourseDTO dto) {
        Course course = new Course();
        course.setTypeCourse(TypeCourse.valueOf(dto.typeCourse));
        course.setSupport(Support.valueOf(dto.support));
        course.setPrice(dto.price);
        course.setLevel(dto.level);
        course.setTimeSlot(dto.timeSlot);
        return course;
    }

    // Helper method to convert Course entity to CourseDTO
    private CourseDTO toDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.typeCourse = course.getTypeCourse().name();
        dto.support = course.getSupport().name();
        dto.price = course.getPrice();
        dto.level = course.getLevel();
        dto.timeSlot = course.getTimeSlot();
        return dto;
    }

    @Operation(description = "Add Course")
    @PostMapping("/add")
    public CourseDTO addCourse(@RequestBody CourseDTO courseDTO) {
        Course course = toEntity(courseDTO);
        Course savedCourse = courseServices.addCourse(course);
        return toDTO(savedCourse);
    }

    @Operation(description = "Retrieve all Courses")
    @GetMapping("/all")
    public List<CourseDTO> getAllCourses() {
        return courseServices.retrieveAllCourses()
                .stream()
                .map(this::toDTO)
                .toList();
                .collect(Collectors.toList());

    }

    @Operation(description = "Update Course")
    @PutMapping("/update")
    public CourseDTO updateCourse(@RequestBody CourseDTO courseDTO) {
        Course course = toEntity(courseDTO);
        Course updatedCourse = courseServices.updateCourse(course);
        return toDTO(updatedCourse);
    }

    @Operation(description = "Retrieve Course by Id")
    @GetMapping("/get/{id-course}")
    public CourseDTO getById(@PathVariable("id-course") Long numCourse) {
        Course course = courseServices.retrieveCourse(numCourse);
        return toDTO(course);
    }
}
