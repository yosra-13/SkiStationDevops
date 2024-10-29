package com.example.gestionstationskii;

import com.example.gestionstationskii.entities.Course;
import com.example.gestionstationskii.entities.Instructor;
import com.example.gestionstationskii.entities.Registration;
import com.example.gestionstationskii.repositories.ICourseRepository;
import com.example.gestionstationskii.repositories.IInstructorRepository;
import com.example.gestionstationskii.repositories.IRegistrationRepository;
import com.example.gestionstationskii.services.CourseServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class CourseServicesImplMockitoTest {

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private IInstructorRepository instructorRepository;

    @InjectMocks
    private CourseServicesImpl courseServices;

    private Course course;
    private Registration registration;
    private Instructor instructor;

    @BeforeEach
    void setUp() {
        // Initialize mock objects
        course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setPrice(150.0f);

        registration = new Registration();
        registration.setNumRegistration(1L);
        registration.setNumWeek(1);

        instructor = new Instructor();
        instructor.setNumInstructor(1L);
        instructor.setFirstName("John");
        instructor.setLastName("Doe");
        instructor.setDateOfHire(LocalDate.now());
        instructor.setCourses(new HashSet<>());
    }

    // Helper to mock instructor and course retrieval
    private void mockInstructorAndCourseFound() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
    }

    @Test
    void testAssignInstructorToCourse_Success() {
        // Mock repository responses
        mockInstructorAndCourseFound();

        // Verify that the mocks return expected objects
        assertTrue(instructorRepository.findById(1L).isPresent(), "Instructor should not be empty");
        assertTrue(courseRepository.findById(1L).isPresent(), "Course should not be empty");

        // Call the service method
        String result = courseServices.assignInstructorToCourse(1L, 1L);

        // Assert the expected result
        assertEquals("Instructor assigned to course successfully", result);

        // Verify that the course was saved with the correct instructor
        verify(courseRepository).save(course);
        assertEquals(instructor, course.getInstructor());
    }

    @Test
    void testAssignInstructorToCourse_InstructorNotFound() {
        // Mock instructor not found scenario
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Call the service method
        String result = courseServices.assignInstructorToCourse(1L, 1L);

        // Assert the expected failure message
        assertEquals("Instructor or Course not found", result);

        // Ensure save was never called
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void testAssignCourseToRegistration_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        String result = courseServices.assignCourseToRegistration(1L, 1L);

        assertEquals("Course with ID 1 has been assigned to registration ID 1", result);

        ArgumentCaptor<Registration> captor = ArgumentCaptor.forClass(Registration.class);
        verify(registrationRepository).save(captor.capture());

        Registration savedRegistration = captor.getValue();
        assertNotNull(savedRegistration.getCourse());
        assertEquals(1L, savedRegistration.getCourse().getNumCourse());
    }

    @Test
    void testAssignCourseToRegistration_CourseNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        String result = courseServices.assignCourseToRegistration(1L, 1L);

        assertEquals("Course with ID 1 not found", result);
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    void testAssignCourseToRegistration_RegistrationNotFound() {
        lenient().when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.findById(1L)).thenReturn(Optional.empty());

        String result = courseServices.assignCourseToRegistration(1L, 1L);

        assertEquals("Registration with ID 1 not found", result);
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    void testAddCourse() {
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course savedCourse = courseServices.addCourse(course);

        assertNotNull(savedCourse);
        assertEquals(course.getNumCourse(), savedCourse.getNumCourse());
        verify(courseRepository).save(course);
    }

    @Test
    void testRetrieveCourse_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course retrievedCourse = courseServices.retrieveCourse(1L);

        assertNotNull(retrievedCourse);
        assertEquals(course.getNumCourse(), retrievedCourse.getNumCourse());
    }

    @Test
    void testRetrieveCourse_NotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Course retrievedCourse = courseServices.retrieveCourse(1L);

        assertNull(retrievedCourse);
    }

    @Test
    void testRetrieveAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course));

        List<Course> courses = courseServices.retrieveAllCourses();

        assertNotNull(courses);
        assertEquals(1, courses.size());
        assertEquals(course.getNumCourse(), courses.get(0).getNumCourse());
    }

    @Test
    void testUpdateCourse() {
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course updatedCourse = courseServices.updateCourse(course);

        assertNotNull(updatedCourse);
        assertEquals(course.getNumCourse(), updatedCourse.getNumCourse());
        verify(courseRepository).save(course);
    }
}
