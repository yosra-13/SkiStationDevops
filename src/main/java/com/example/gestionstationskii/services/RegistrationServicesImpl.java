package com.example.gestionstationskii.services;

import com.example.gestionstationskii.entities.*;
import com.example.gestionstationskii.repositories.ICourseRepository;
import com.example.gestionstationskii.repositories.IRegistrationRepository;
import com.example.gestionstationskii.repositories.ISkierRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class RegistrationServicesImpl implements IRegistrationServices {

    private final IRegistrationRepository registrationRepository;
    private final ISkierRepository skierRepository;
    private final ICourseRepository courseRepository;
    @Override
    public Registration addRegistrationAndAssignToSkier(Registration registration, Long numSkier) {
        if (registration == null) {
            log.warn("Registration object is null");
            return null;
        }

        Skier skier = skierRepository.findById(numSkier).orElse(null);
        if (skier == null) {
            log.warn("Skier with ID {} not found", numSkier);
            return null;
        }

        registration.setSkier(skier);
        return registrationRepository.save(registration);
    }

    @Override
    public Registration assignRegistrationToCourse(Long numRegistration, Long numCourse) {
        Registration registration = registrationRepository.findById(numRegistration).orElse(null);
        if (registration == null) {
            log.warn("Registration with ID {} not found", numRegistration);
            return null;
        }

        Course course = courseRepository.findById(numCourse).orElse(null);
        if (course == null) {
            log.warn("Course with ID {} not found", numCourse);
            return null;
        }

        registration.setCourse(course);
        return registrationRepository.save(registration);
    }

    @Transactional
    @Override
    public Registration addRegistrationAndAssignToSkierAndCourse(Registration registration, Long numSkieur, Long numCours) {
        if (registration == null) {
            log.warn("Registration object is null");
            return null;
        }

        Skier skier = skierRepository.findById(numSkieur).orElse(null);
        Course course = courseRepository.findById(numCours).orElse(null);

        if (skier == null) {
            log.warn("Skier with ID {} not found", numSkieur);
            return null;
        }

        if (course == null) {
            log.warn("Course with ID {} not found", numCours);
            return null;
        }

        registration.setSkier(skier);
        registration.setCourse(course);
        return registrationRepository.save(registration);
    }
}
