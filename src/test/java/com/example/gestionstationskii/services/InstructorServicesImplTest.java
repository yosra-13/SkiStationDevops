package com.example.gestionstationskii.services;

import com.example.gestionstationskii.entities.Course;
import com.example.gestionstationskii.entities.Instructor;
import com.example.gestionstationskii.repositories.ICourseRepository;
import com.example.gestionstationskii.repositories.IInstructorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class InstructorServicesImplTest {
    @Autowired
    private ICourseRepository courseRepository;

    @Autowired
    private IInstructorRepository instructorRepository;

    @Autowired
    private InstructorServicesImpl instructorServices;

    private Course course1;
    private Course course2;
    private Instructor instructor;

    @BeforeEach
    void setUp() {
        // Préparer des cours pour les tests
        course1 = new Course();
        course1.setLevel(1);
        course1.setPrice(250.0f);
        course1 = courseRepository.save(course1);

        course2 = new Course();
        course2.setLevel(2);
        course2.setPrice(350.0f);
        course2 = courseRepository.save(course2);

        // Préparer un instructeur pour les tests
        instructor = new Instructor();
        instructor.setFirstName("Alice");
        instructor.setLastName("Johnson");
        instructor.setDateOfHire(LocalDate.now());
    }

    @Test
    void testCreateInstructor_Success() {
        Instructor savedInstructor = instructorServices.addInstructor(instructor);
        assertNotNull(savedInstructor.getNumInstructor(), "L'instructeur n'a pas été créé avec succès.");
    }

    @Test
    void testRetrieveInstructor_Success() {
        Instructor savedInstructor = instructorServices.addInstructor(instructor);
        Instructor retrievedInstructor = instructorServices.retrieveInstructor(savedInstructor.getNumInstructor());
        assertNotNull(retrievedInstructor, "L'instructeur n'a pas été récupéré avec succès.");
        assertEquals(instructor.getFirstName(), retrievedInstructor.getFirstName());
    }

    @Test
    void testUpdateInstructor_Success() {
        Instructor savedInstructor = instructorServices.addInstructor(instructor);
        savedInstructor.setFirstName("UpdatedName");
        Instructor updatedInstructor = instructorServices.updateInstructor(savedInstructor);
        assertEquals("UpdatedName", updatedInstructor.getFirstName(), "La mise à jour de l'instructeur a échoué.");
    }

    @Test
    void testDeleteInstructor_Success() {
        Instructor savedInstructor = instructorServices.addInstructor(instructor);
        instructorServices.retrieveInstructor(savedInstructor.getNumInstructor());
        instructorRepository.deleteById(savedInstructor.getNumInstructor());
        assertFalse(instructorRepository.findById(savedInstructor.getNumInstructor()).isPresent(),
                "L'instructeur n'a pas été supprimé correctement.");
    }

    @Test
    void testAssignMultipleCoursesToInstructor() {
        // Assigner plusieurs cours
        Set<Course> courses = new HashSet<>();
        courses.add(course1);
        courses.add(course2);
        instructor.setCourses(courses);

        Instructor savedInstructor = instructorServices.addInstructor(instructor);

        assertNotNull(savedInstructor.getCourses(), "Les cours n'ont pas été assignés correctement.");
        assertEquals(2, savedInstructor.getCourses().size(), "L'instructeur n'est pas associé à plusieurs cours.");
    }


}