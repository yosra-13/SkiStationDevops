package com.example.gestionstationskii.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.gestionstationskii.entities.*;
import com.example.gestionstationskii.repositories.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

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
        Course course = courseRepository.findById(numCourse).orElse(null);

        if (registration == null || course == null) {
            log.warn("Registration or Course not found. Registration ID: {}, Course ID: {}", numRegistration, numCourse);
            return null;
        }

        registration.setCourse(course);
        return registrationRepository.save(registration);
    }

    @Transactional
    @Override
    public Registration addRegistrationAndAssignToSkierAndCourse(Registration registration, Long numSkieur, Long numCours) {
        Optional<Skier> optionalSkier = skierRepository.findById(numSkieur);
        Optional<Course> optionalCourse = courseRepository.findById(numCours);

        if (!optionalSkier.isPresent() || !optionalCourse.isPresent()) {
            log.warn("Skier or Course not found. Skier ID: {}, Course ID: {}", numSkieur, numCours);
            return null;
        }

        Skier skier = optionalSkier.get();
        Course course = optionalCourse.get();

        if (registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(
                registration.getNumWeek(), skier.getNumSkier(), course.getNumCourse()) >= 1) {
            log.info("Already registered to this course for week: {}", registration.getNumWeek());
            return null;
        }

        int ageSkieur = calculateAge(skier.getDateOfBirth());
        log.info("Skier's Age: {}", ageSkieur);

        return handleCourseRegistration(registration, skier, course, ageSkieur);
    }

    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private Registration handleCourseRegistration(Registration registration, Skier skier, Course course, int ageSkieur) {
        switch (course.getTypeCourse()) {
            case INDIVIDUAL:
                log.info("Assigning individual course");
                return assignRegistration(registration, skier, course);

            case COLLECTIVE_CHILDREN:
                return registerForCollectiveChildrenCourse(registration, skier, course, ageSkieur);

            default:
                return registerForAdultCourse(registration, skier, course, ageSkieur);
        }
    }

    private Registration registerForCollectiveChildrenCourse(Registration registration, Skier skier, Course course, int ageSkieur) {
        if (ageSkieur < 16) {
            log.info("Valid CHILD registration attempt");
            if (isCourseSlotAvailable(course, registration.getNumWeek())) {
                log.info("Course successfully added for child.");
                return assignRegistration(registration, skier, course);
            } else {
                log.info("Course full. Choose another week.");
            }
        } else {
            log.info("Age too high for children course. Try adult course.");
        }
        return null;
    }

    private Registration registerForAdultCourse(Registration registration, Skier skier, Course course, int ageSkieur) {
        if (ageSkieur >= 16) {
            log.info("Valid ADULT registration attempt");
            if (isCourseSlotAvailable(course, registration.getNumWeek())) {
                log.info("Course successfully added for adult.");
                return assignRegistration(registration, skier, course);
            } else {
                log.info("Course full. Choose another week.");
            }
        } else {
            log.info("Age too low for adult course. Try children course.");
        }
        return null;
    }

    private boolean isCourseSlotAvailable(Course course, int numWeek) {
        return registrationRepository.countByCourseAndNumWeek(course, numWeek) < 6;
    }

    private Registration assignRegistration(Registration registration, Skier skier, Course course) {
        registration.setSkier(skier);
        registration.setCourse(course);
        return registrationRepository.save(registration);
    }

    @Override
    public List<Integer> numWeeksCourseOfInstructorBySupport(Long numInstructor, Support support) {
        return registrationRepository.numWeeksCourseOfInstructorBySupport(numInstructor, support);
    }

}



