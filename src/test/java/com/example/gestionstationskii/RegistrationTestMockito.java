package com.example.gestionstationskii;

import com.example.gestionstationskii.entities.*;
import com.example.gestionstationskii.repositories.*;
import com.example.gestionstationskii.services.RegistrationServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationTestMockito {

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    private Skier skier;
    private Course course;
    private Registration registration;

    @BeforeEach
    void setup() {
        // Initialize mock entities
        skier = new Skier();
        skier.setNumSkier(1L);
        skier.setDateOfBirth(LocalDate.of(2000, 1, 1));

        course = new Course();
        course.setNumCourse(1L);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);

        registration = new Registration();
        registration.setNumRegistration(1L);
        registration.setNumWeek(1);
    }

    @Test
    void testCreateRegistration_Success() {
        // Mock behavior
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // Call the service method
        Registration result = registrationServices.addRegistrationAndAssignToSkier(registration, skier.getNumSkier());

        // Assert and verify
        assertNotNull(result);
        assertEquals(1L, result.getNumRegistration());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void testReadRegistration_Success() {
        // Mock behavior
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));

        // Call repository method
        Registration found = registrationRepository.findById(1L).orElse(null);

        // Assert and verify
        assertNotNull(found);
        assertEquals(1L, found.getNumRegistration());
        verify(registrationRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateRegistration_Success() {
        // Mock behavior
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // Update and save
        registration.setNumWeek(2);
        Registration updatedRegistration = registrationRepository.save(registration);

        // Assert and verify
        assertNotNull(updatedRegistration);
        assertEquals(2, updatedRegistration.getNumWeek());
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void testDeleteRegistration_Success() {
        // Call repository method to delete
        registrationRepository.deleteById(1L);

        // Verify delete behavior
        verify(registrationRepository, times(1)).deleteById(1L);
    }

    @Test
    void testAssignRegistrationToCourse_Success() {
        // Mock repository behavior
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // Call service method
        Registration result = registrationServices.assignRegistrationToCourse(1L, 1L);

        // Assert and verify
        assertNotNull(result);
        assertEquals(course.getNumCourse(), result.getCourse().getNumCourse());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }


    }