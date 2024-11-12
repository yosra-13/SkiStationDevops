package com.example.gestionstationskii;

import com.example.gestionstationskii.entities.*;
import com.example.gestionstationskii.repositories.*;
import com.example.gestionstationskii.services.RegistrationServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
@SpringBootTest
class RegistrationTest {

    @Autowired
    private IRegistrationRepository registrationRepository;

    @Autowired
    private ISkierRepository skierRepository;

    @Autowired
    private ICourseRepository courseRepository;

    @Autowired
    private RegistrationServicesImpl registrationServices;

    private Skier skier;
    private Course course;
    private Registration registration;

    @BeforeEach
    void setup() {
        // Create and save a Skier
        registrationRepository.deleteAll();
        skierRepository.deleteAll();
        courseRepository.deleteAll();
        skier = new Skier();
        skier.setNumSkier(1L);
        skier.setDateOfBirth(LocalDate.of(2000, 1, 1));
        skier = skierRepository.save(skier);

        // Create and save a Course
        course = new Course();
        course.setNumCourse(1L);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course = courseRepository.save(course);

        // Create a Registration
        registration = new Registration();
        registration.setNumWeek(1);
    }

    // * CREATE Test *
    @Order(1)
    @Test
    void testCreateRegistration_Success() {
        Registration result = registrationServices.addRegistrationAndAssignToSkier(registration, skier.getNumSkier());

        assertNotNull(result);
        assertEquals(1, registrationRepository.count());
    }

    // * READ Test *
    @Test
    void testReadRegistration_Success() {
        Registration savedRegistration = registrationServices.addRegistrationAndAssignToSkier(registration, skier.getNumSkier());

        Registration found = registrationRepository.findById(savedRegistration.getNumRegistration()).orElse(null);
        assertNotNull(found);
        assertEquals(savedRegistration.getNumRegistration(), found.getNumRegistration());
    }

    // * UPDATE Test *
    @Test
    void testUpdateRegistration_Success() {
        Registration savedRegistration = registrationServices.addRegistrationAndAssignToSkier(registration, skier.getNumSkier());

        // Update the registration (e.g., change the week number)
        savedRegistration.setNumWeek(2);
        Registration updatedRegistration = registrationRepository.save(savedRegistration);

        assertNotNull(updatedRegistration);
        assertEquals(2, updatedRegistration.getNumWeek());
    }

    // * DELETE Test *
    @Test
    void testDeleteRegistration_Success() {
        Registration savedRegistration = registrationServices.addRegistrationAndAssignToSkier(registration, skier.getNumSkier());

        // Delete the registration
        registrationRepository.deleteById(savedRegistration.getNumRegistration());

        // Verify the registration is deleted
        boolean exists = registrationRepository.existsById(savedRegistration.getNumRegistration());
        assertFalse(exists);
    }

    @Test
    void testAddRegistrationAndAssignToSkier_Success() {
        Registration result = registrationServices.addRegistrationAndAssignToSkier(registration, skier.getNumSkier());

        assertNotNull(result);
        assertEquals(skier.getNumSkier(), result.getSkier().getNumSkier());
        assertNotNull(result.getNumRegistration());
    }

    @Test
    void testAssignRegistrationToCourse_Success() {
        registration = registrationServices.addRegistrationAndAssignToSkier(registration, skier.getNumSkier());

        Registration result = registrationServices.assignRegistrationToCourse(
                registration.getNumRegistration(), course.getNumCourse());

        assertNotNull(result);
        assertEquals(course.getNumCourse(), result.getCourse().getNumCourse());
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse_Success() {
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(
                registration, skier.getNumSkier(), course.getNumCourse());

        assertNotNull(result);
        assertEquals(skier.getNumSkier(), result.getSkier().getNumSkier());
        assertEquals(course.getNumCourse(), result.getCourse().getNumCourse());
    }

    @Test
    void testCleanUp() {
        registrationRepository.deleteAll();
        skierRepository.deleteAll();
        courseRepository.deleteAll();

        assertFalse(registrationRepository.findAll().iterator().hasNext());
        assertFalse(skierRepository.findAll().iterator().hasNext());
        assertFalse(courseRepository.findAll().iterator().hasNext());
    }
}