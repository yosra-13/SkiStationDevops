package com.example.gestionstationskii.services;

import com.example.gestionstationskii.entities.Course;
import com.example.gestionstationskii.entities.Instructor;
import com.example.gestionstationskii.entities.Registration;
import com.example.gestionstationskii.repositories.ICourseRepository;
import com.example.gestionstationskii.repositories.IInstructorRepository;
import com.example.gestionstationskii.repositories.IRegistrationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
@AllArgsConstructor
@Service
public class CourseServicesImpl implements  ICourseServices{

    private ICourseRepository courseRepository;
    private IRegistrationRepository registrationRepository;
    private static final Logger logger = LoggerFactory.getLogger(CourseServicesImpl.class);
    private final IInstructorRepository instructorRepository;

    @Override
    public String assignInstructorToCourse(Long instructorId, Long courseId) {
        // Fetch instructor and course
        Instructor instructor = instructorRepository.findById(instructorId).orElse(null);
        Course course = courseRepository.findById(courseId).orElse(null);

        if (instructor == null || course == null) {
            logger.info("Instructor or Course not found");
            return "Instructor or Course not found";
        }

        // Debugging: Print instructor and course info
        logger.info("Instructor ID: {}", instructor.getNumInstructor());
        logger.info("Number of Courses: {}", instructor.getCourses().size());

        if (instructor.getCourses().size() >= 3) {
            return "Instructor cannot have more than 3 active courses";
        }

        // Assign the course to the instructor
        instructor.getCourses().add(course);
        course.setInstructor(instructor);

        // Save the course
        courseRepository.save(course);

        return "Instructor assigned to course successfully";
    }



    @Override
    public List<Course> retrieveAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course retrieveCourse(Long numCourse) {
        return courseRepository.findById(numCourse).orElse(null);
    }

    @Override
    public String assignCourseToRegistration(Long courseId, Long registrationId) {
        // Find the course by ID
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            return "Course with ID " + courseId + " not found";
        }

        // Find the registration by ID
        Registration registration = registrationRepository.findById(registrationId).orElse(null);
        if (registration == null) {
            return "Registration with ID " + registrationId + " not found";
        }

        // Set the course for the registration
        registration.setCourse(course);

        // Save the updated registration
        registrationRepository.save(registration);

        return "Course with ID " + courseId + " has been assigned to registration ID " + registrationId;
    }

}
