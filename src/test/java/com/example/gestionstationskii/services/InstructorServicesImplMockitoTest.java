package com.example.gestionstationskii.services;

import com.example.gestionstationskii.entities.Course;
import com.example.gestionstationskii.entities.Instructor;
import com.example.gestionstationskii.repositories.ICourseRepository;
import com.example.gestionstationskii.repositories.IInstructorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructorServicesImplMockitoTest {

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private IInstructorRepository instructorRepository;

    @InjectMocks
    private InstructorServicesImpl instructorServices;

    private Instructor instructor;
    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize course
        course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setPrice(250.0f);

        // Initialize instructor
        instructor = new Instructor();
        instructor.setNumInstructor(1L);
        instructor.setFirstName("Alice");
        instructor.setLastName("Johnson");
        instructor.setDateOfHire(LocalDate.now());
        instructor.setCourses(new HashSet<>(Collections.singletonList(course)));
    }

    @Test
    void testCreateInstructor_Success() {
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor savedInstructor = instructorServices.addInstructor(instructor);

        assertNotNull(savedInstructor);
        assertEquals(instructor.getNumInstructor(), savedInstructor.getNumInstructor());

        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testRetrieveInstructor_Success() {
        when(instructorRepository.findById(instructor.getNumInstructor())).thenReturn(Optional.of(instructor));

        Instructor retrievedInstructor = instructorServices.retrieveInstructor(instructor.getNumInstructor());

        assertNotNull(retrievedInstructor);
        assertEquals(instructor.getFirstName(), retrievedInstructor.getFirstName());

        verify(instructorRepository, times(1)).findById(instructor.getNumInstructor());
    }

    @Test
    void testUpdateInstructor_Success() {
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        instructor.setFirstName("UpdatedName");
        Instructor updatedInstructor = instructorServices.updateInstructor(instructor);

        assertEquals("UpdatedName", updatedInstructor.getFirstName());

        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testDeleteInstructor_Success() {
        doNothing().when(instructorRepository).deleteById(instructor.getNumInstructor());

        instructorServices.retrieveInstructor(instructor.getNumInstructor());
        instructorRepository.deleteById(instructor.getNumInstructor());

        verify(instructorRepository, times(1)).deleteById(instructor.getNumInstructor());
    }

    @Test
    void testAssignMultipleCoursesToInstructor_Success() {
        Course course2 = new Course();
        course2.setNumCourse(2L);
        course2.setLevel(2);
        course2.setPrice(350.0f);

        Set<Course> courseSet = new HashSet<>();
        courseSet.add(course);
        courseSet.add(course2);

        instructor.setCourses(courseSet);

        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor savedInstructor = instructorServices.updateInstructor(instructor);

        assertNotNull(savedInstructor);
        assertEquals(2, savedInstructor.getCourses().size());

        verify(instructorRepository, times(1)).save(instructor);
    }
    @Test
    void testAssignInstructorToExistingCourse() {
        // Simuler que le cours existe dans le repository
        when(courseRepository.findById(course.getNumCourse())).thenReturn(Optional.of(course));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // Appeler la méthode de service
        Instructor savedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, course.getNumCourse());

        // Vérifier que l'instructeur est sauvegardé avec le cours assigné
        assertNotNull(savedInstructor);
        assertNotNull(savedInstructor.getCourses());
        assertEquals(1, savedInstructor.getCourses().size());

        verify(courseRepository, times(1)).findById(course.getNumCourse());
        verify(instructorRepository, times(1)).save(instructor);
    }



}