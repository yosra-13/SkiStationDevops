package com.example.gestionstationskii.services;

import com.example.gestionstationskii.entities.*;
import java.util.List;

public interface IRegistrationServices {

	Registration addRegistrationAndAssignToSkier(Registration registration, Long numSkier);
	Registration assignRegistrationToCourse(Long numRegistration, Long numCourse);
	Registration addRegistrationAndAssignToSkierAndCourse(Registration registration, Long numSkieur, Long numCours);

    // New method: Retrieve a registration by its ID
    Registration retrieveRegistration(Long numRegistration);


    // New method: Delete a registration by its ID
    void deleteRegistration(Long numRegistration);

    // New method: Update an existing registration
    Registration updateRegistration(Registration registration);

    List<Integer> numWeeksCourseOfInstructorBySupport(Long numInstructor, Support support);
}

