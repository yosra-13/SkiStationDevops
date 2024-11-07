package com.example.gestionstationskii.services;

import com.example.gestionstationskii.entities.Course;
import com.example.gestionstationskii.entities.Instructor;
import com.example.gestionstationskii.entities.Support;
import com.example.gestionstationskii.entities.TypeCourse;
import com.example.gestionstationskii.repositories.ICourseRepository;
import com.example.gestionstationskii.repositories.IInstructorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructorServicesImplMockitoTest {

    @InjectMocks
    private InstructorServicesImpl instructorServices;

    @Mock
    private IInstructorRepository instructorRepository;

    @Mock
    private ICourseRepository courseRepository;

    private Instructor instructor;
    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize your test objects
        instructor = new Instructor();
        instructor.setNumInstructor(1L);
        instructor.setFirstName("John");
        instructor.setLastName("Doe");

        course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.INDIVIDUAL); // Corrected TypeCourse
        course.setSupport(Support.SKI); // Corrected Support
        course.setPrice(100.0f);
        course.setTimeSlot(10);
    }

    @Test
    void testAddInstructor() {
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor savedInstructor = instructorServices.addInstructor(instructor);

        assertNotNull(savedInstructor);
        assertEquals("John", savedInstructor.getFirstName());
        assertEquals("Doe", savedInstructor.getLastName());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testRetrieveAllInstructors() {
        when(instructorRepository.findAll()).thenReturn(Arrays.asList(instructor));

        List<Instructor> instructors = instructorServices.retrieveAllInstructors();

        assertEquals(1, instructors.size());
        assertEquals("John", instructors.get(0).getFirstName());
        assertEquals("Doe", instructors.get(0).getLastName());
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void testUpdateInstructor() {
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor updatedInstructor = instructorServices.updateInstructor(instructor);

        assertNotNull(updatedInstructor);
        assertEquals("John", updatedInstructor.getFirstName());
        assertEquals("Doe", updatedInstructor.getLastName());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testRetrieveInstructor() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        Instructor foundInstructor = instructorServices.retrieveInstructor(1L);

        assertNotNull(foundInstructor);
        assertEquals("John", foundInstructor.getFirstName());
        assertEquals("Doe", foundInstructor.getLastName());
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    void testAddInstructorAndAssignToCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // Create a Set of courses to be assigned
        HashSet<Course> courses = new HashSet<>();
        courses.add(course);
        instructor.setCourses(courses);

        Instructor savedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, 1L);

        assertNotNull(savedInstructor);
        assertEquals(1, savedInstructor.getCourses().size());
        assertTrue(savedInstructor.getCourses().contains(course));
        verify(courseRepository, times(1)).findById(1L);
        verify(instructorRepository, times(1)).save(instructor);
    }



}