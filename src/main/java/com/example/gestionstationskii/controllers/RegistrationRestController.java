package com.example.gestionstationskii.controllers;

import com.example.gestionstationskii.entities.Registration;
import com.example.gestionstationskii.entities.Support;
import com.example.gestionstationskii.services.IRegistrationServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "\uD83D\uDDD3️ Registration Management")
@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationRestController {

    private final IRegistrationServices registrationServices;

    // DTO for Registration Data
    public static class RegistrationDTO {
<<<<<<< HEAD
         int numWeek;
         Long skierId;
         Long courseId;
=======
        int numWeek;
        Long skierId;
        Long courseId;
>>>>>>> gestion_piste
    }

    // Helper method to convert Registration entity to DTO
    private RegistrationDTO toDTO(Registration registration) {
        RegistrationDTO dto = new RegistrationDTO();
        dto.numWeek = registration.getNumWeek();
        dto.skierId = registration.getSkier() != null ? registration.getSkier().getNumSkier() : null;
        dto.courseId = registration.getCourse() != null ? registration.getCourse().getNumCourse() : null;
        return dto;
    }

    // Helper method to convert DTO to Registration entity
    private Registration toEntity(RegistrationDTO dto) {
        Registration registration = new Registration();
        registration.setNumWeek(dto.numWeek);
        return registration;
    }

    @Operation(description = "Add Registration and Assign to Skier")
    @PutMapping("/addAndAssignToSkier/{numSkieur}")
    public RegistrationDTO addAndAssignToSkier(
            @RequestBody RegistrationDTO registrationDTO,
            @PathVariable("numSkieur") Long numSkieur) {
        Registration registration = toEntity(registrationDTO);
        Registration savedRegistration = registrationServices.addRegistrationAndAssignToSkier(registration, numSkieur);
        return toDTO(savedRegistration);
    }

    @Operation(description = "Assign Registration to Course")
    @PutMapping("/assignToCourse/{numRegis}/{numCourse}")
    public RegistrationDTO assignToCourse(
            @PathVariable("numRegis") Long numRegistration,
            @PathVariable("numCourse") Long numCourse) {
        Registration savedRegistration = registrationServices.assignRegistrationToCourse(numRegistration, numCourse);
        return toDTO(savedRegistration);
    }

    @Operation(description = "Add Registration and Assign to Skier and Course")
    @PutMapping("/addAndAssignToSkierAndCourse/{numSkieur}/{numCourse}")
    public RegistrationDTO addAndAssignToSkierAndCourse(
            @RequestBody RegistrationDTO registrationDTO,
            @PathVariable("numSkieur") Long numSkieur,
            @PathVariable("numCourse") Long numCourse) {
        Registration registration = toEntity(registrationDTO);
        Registration savedRegistration = registrationServices.addRegistrationAndAssignToSkierAndCourse(
                registration, numSkieur, numCourse);
        return toDTO(savedRegistration);
    }

    @Operation(description = "Numbers of the weeks when an instructor has given lessons in a given support")
    @GetMapping("/numWeeks/{numInstructor}/{support}")
    public List<Integer> numWeeksCourseOfInstructorBySupport(
            @PathVariable("numInstructor") Long numInstructor,
            @PathVariable("support") Support support) {
        return registrationServices.numWeeksCourseOfInstructorBySupport(numInstructor, support);
    }
}
