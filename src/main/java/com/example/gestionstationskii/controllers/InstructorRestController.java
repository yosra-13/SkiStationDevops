package com.example.gestionstationskii.controllers;

import com.example.gestionstationskii.entities.Course;
import com.example.gestionstationskii.entities.Instructor;
import com.example.gestionstationskii.services.IInstructorServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "\uD83D\uDC69\u200D\uD83C\uDFEB Instructor Management")
@RestController
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorRestController {

    private final IInstructorServices instructorServices;

    // DTO for Instructor data
    public static class InstructorDTO {

        String firstName;
        String lastName;
        LocalDate dateOfHire;
        Set<Long> courseIds;  // Only IDs of courses

    }

    // Helper method to map DTO to Instructor entity
    private Instructor toEntity(InstructorDTO dto) {
        Instructor instructor = new Instructor();
        instructor.setFirstName(dto.firstName);
        instructor.setLastName(dto.lastName);
        instructor.setDateOfHire(dto.dateOfHire);
        return instructor; // Courses will be assigned in service logic if needed
    }

    // Helper method to map Instructor entity to DTO
    private InstructorDTO toDTO(Instructor instructor) {
        InstructorDTO dto = new InstructorDTO();
        dto.firstName = instructor.getFirstName();
        dto.lastName = instructor.getLastName();
        dto.dateOfHire = instructor.getDateOfHire();
        dto.courseIds = instructor.getCourses()
                .stream()
                .map(Course::getNumCourse)  // Method reference used here
                .collect(Collectors.toSet());
        return dto;
    }

    @Operation(description = "Add Instructor")
    @PostMapping("/add")
    public InstructorDTO addInstructor(@RequestBody InstructorDTO instructorDTO) {
        Instructor instructor = toEntity(instructorDTO);
        Instructor savedInstructor = instructorServices.addInstructor(instructor);
        return toDTO(savedInstructor);
    }

    @Operation(description = "Add Instructor and Assign To Course")
    @PutMapping("/addAndAssignToCourse/{numCourse}")
    public InstructorDTO addAndAssignToInstructor(
            @RequestBody InstructorDTO instructorDTO,
            @PathVariable("numCourse") Long numCourse) {
        Instructor instructor = toEntity(instructorDTO);
        Instructor savedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, numCourse);
        return toDTO(savedInstructor);
    }

    @Operation(description = "Retrieve all Instructors")
    @GetMapping("/all")
    public List<InstructorDTO> getAllInstructors() {
        return instructorServices.retrieveAllInstructors()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Operation(description = "Update Instructor")
    @PutMapping("/update")
    public InstructorDTO updateInstructor(@RequestBody InstructorDTO instructorDTO) {
        Instructor instructor = toEntity(instructorDTO);
        Instructor updatedInstructor = instructorServices.updateInstructor(instructor);
        return toDTO(updatedInstructor);
    }

    @Operation(description = "Retrieve Instructor by Id")
    @GetMapping("/get/{id-instructor}")
    public InstructorDTO getById(@PathVariable("id-instructor") Long numInstructor) {
        Instructor instructor = instructorServices.retrieveInstructor(numInstructor);
        return toDTO(instructor);
    }

}

}

