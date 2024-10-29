package com.example.gestionstationskii;

import com.example.gestionstationskii.entities.Course;
import com.example.gestionstationskii.entities.Instructor;
import com.example.gestionstationskii.entities.Registration;
import com.example.gestionstationskii.repositories.ICourseRepository;
import com.example.gestionstationskii.repositories.IInstructorRepository;
import com.example.gestionstationskii.repositories.IRegistrationRepository;
import com.example.gestionstationskii.services.ICourseServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)  // Ensure ordered execution
     class CourseTest {

    @Autowired
    private ICourseRepository courseRepository;

    @Autowired
    private IRegistrationRepository registrationRepository;

    @Autowired
    IInstructorRepository instructorRepository;

    @Autowired
    ICourseServices cs;

    private Course course1;
    private Registration registration;

    // Initialize a new course before each test to avoid null issues
    @BeforeEach
    void setup() {
        course1 = new Course();
        course1.setLevel(1);
        course1.setPrice(150.0f);
        course1 = cs.addCourse(course1);  // Save the course to the repository for use in tests
    }

    @Test
    @Order(1)
    void testAddCourse() {
        // Assert: Verify the course was saved and has a generated ID
        assertNotNull(course1);
        assertNotNull(course1.getNumCourse());
        assertEquals(1, course1.getLevel());
    }

    @Test
    @Order(2)
    void testRetrieveAllCourses() {
        // Act: Retrieve all courses
        List<Course> courses = cs.retrieveAllCourses();

        // Assert: Ensure the course list is not empty and contains the newly added course
        assertNotNull(courses);
        assertFalse(courses.isEmpty());
        assertTrue(courses.stream().anyMatch(c -> c.getNumCourse().equals(course1.getNumCourse())));
    }

    @Test
    @Order(3)
    void testUpdateCourse() {
        // Arrange: Update the course's level and price
        course1.setLevel(2);
        course1.setPrice(200.0f);

        // Act: Update the course using the service
        Course updatedCourse = cs.updateCourse(course1);

        // Assert: Ensure the updated course has the new values
        assertNotNull(updatedCourse);
        assertEquals(2, updatedCourse.getLevel());
        assertEquals(200.0f, updatedCourse.getPrice());
    }

    @Test
    @Order(4)
    void testRetrieveCourse_Success() {
        // Act: Retrieve the course by its ID
        Course retrievedCourse = cs.retrieveCourse(course1.getNumCourse());

        // Assert: Ensure the retrieved course matches the original course
        assertNotNull(retrievedCourse);
        assertEquals(course1.getNumCourse(), retrievedCourse.getNumCourse());
    }

    @Test
    @Order(5)
    void testRetrieveCourse_NotFound() {
        // Act: Try to retrieve a course with a non-existent ID
        Course retrievedCourse = cs.retrieveCourse(999L);

        // Assert: Ensure the result is null since the course does not exist
        assertNull(retrievedCourse);
    }

    @Test
    void testAssignCourseToRegistration_Success() {
        // Arrange: Create and save a new registration
        registration = new Registration();
        registration.setNumWeek(1);
        registration = registrationRepository.save(registration);

        // Act: Assign the saved course to the new registration
        String result = cs.assignCourseToRegistration(course1.getNumCourse(), registration.getNumRegistration());

        // Assert: Validate the output message
        assertEquals(
                "Course with ID " + course1.getNumCourse() + " has been assigned to registration ID " + registration.getNumRegistration(),
                result
        );

        // Verify that the course is correctly set in the registration
        Registration updatedRegistration = registrationRepository.findById(registration.getNumRegistration()).orElse(null);
        assertNotNull(updatedRegistration);
        assertNotNull(updatedRegistration.getCourse());
        assertEquals(course1.getNumCourse(), updatedRegistration.getCourse().getNumCourse());

        // Clean up
        registrationRepository.deleteById(registration.getNumRegistration());
        courseRepository.deleteById(course1.getNumCourse());
    }
    @Test
    @Order(6)
    void testDeleteCourse() {
        // Arrange: Create and save a new course
        Course newCourse = new Course();
        newCourse.setLevel(3);
        newCourse.setPrice(300.0f);
        newCourse = courseRepository.save(newCourse);

        // Act: Delete the course
        courseRepository.deleteById(newCourse.getNumCourse());

        // Assert: Ensure the course is no longer in the database
        Course deletedCourse = cs.retrieveCourse(newCourse.getNumCourse());
        assertNull(deletedCourse);
    }

    @Test
    @Order(7)
    void testAssignCourseToInvalidRegistration() {
        // Arrange: Create and save a course
        Course course = new Course();
        course.setLevel(2);
        course.setPrice(200.0f);
        course = courseRepository.save(course);

        // Act & Assert: Try assigning the course to a non-existent registration and ensure the error message is correct
        String result = cs.assignCourseToRegistration(course.getNumCourse(), 999L);

        assertEquals("Registration with ID 999 not found", result);
    }
    @Test
    @Order(8)
    void testAssignInstructorToCourse_LimitExceeded() {

        Instructor instructor = new Instructor();
        instructor.setFirstName("John");
        instructor.setLastName("Doe");
        instructor.setDateOfHire(LocalDate.now());
        instructor = instructorRepository.save(instructor);

        for (int i = 1; i <= 3; i++) {
            Course course = new Course();
            course.setLevel(i);
            course.setPrice(100.0f * i);
            course.setInstructor(instructor);
            courseRepository.save(course);
        }


        Course newCourse = new Course();
        newCourse.setLevel(4);
        newCourse.setPrice(400.0f);
        courseRepository.save(newCourse);

        String result = cs.assignInstructorToCourse(instructor.getNumInstructor(), newCourse.getNumCourse());

        // Assert: Vérifier que l'ajout est refusé
        assertEquals("Instructor cannot have more than 3 active courses", result);
    }

}
