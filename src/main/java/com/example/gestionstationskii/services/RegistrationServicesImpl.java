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
        Skier skier = skierRepository.findById(numSkier).orElse(null);
        registration.setSkier(skier);
        return registrationRepository.save(registration);
    }

    @Override
    public Registration assignRegistrationToCourse(Long numRegistration, Long numCourse) {
        Registration registration = registrationRepository.findById(numRegistration).orElse(null);
        Course course = courseRepository.findById(numCourse).orElse(null);
        registration.setCourse(course);
        return registrationRepository.save(registration);
    }

    @Transactional
    @Override
    public Registration addRegistrationAndAssignToSkierAndCourse(Registration registration, Long numSkieur, Long numCours) {
        Skier skier = skierRepository.findById(numSkieur).orElse(null);
        Course course = courseRepository.findById(numCours).orElse(null);
        if (skier != null && course != null) {
            registration.setSkier(skier);
            registration.setCourse(course);
            return registrationRepository.save(registration);
        }
        return null;
    }

    // New method: Retrieve a registration by its ID
    @Override
    public Registration retrieveRegistration(Long numRegistration) {
        return registrationRepository.findById(numRegistration).orElse(null);
    }



    // New method: Delete a registration by its ID
    @Override
    public void deleteRegistration(Long numRegistration) {
        registrationRepository.deleteById(numRegistration);
    }

    // New method: Update an existing registration
    @Override
    public Registration updateRegistration(Registration registration) {
        return registrationRepository.save(registration);
    }

    @Override
    public List<Integer> numWeeksCourseOfInstructorBySupport(Long numInstructor, Support support) {
        return registrationRepository.numWeeksCourseOfInstructorBySupport(numInstructor, support);
    }
}
