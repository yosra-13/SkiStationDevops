package com.example.gestionstationskii.controllers;

import com.example.gestionstationskii.entities.Piste;
import com.example.gestionstationskii.entities.Skier;
import com.example.gestionstationskii.entities.TypeSubscription;
import com.example.gestionstationskii.services.ISkierServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "\uD83C\uDFC2 Skier Management")
@RestController
@RequestMapping("/skier")
@RequiredArgsConstructor
public class SkierRestController {

    private final ISkierServices skierServices;

    // Inner DTO class to encapsulate Skier data
    public static class SkierDTO {
        String firstName;
        String lastName;
        LocalDate dateOfBirth;
        String city;
        Long subscriptionId;
        Set<Long> pisteIds;  // Only expose Piste IDs
    }

    // Helper method to convert Skier entity to DTO
    private SkierDTO toDTO(Skier skier) {
        SkierDTO dto = new SkierDTO();
        dto.firstName = skier.getFirstName();
        dto.lastName = skier.getLastName();
        dto.dateOfBirth = skier.getDateOfBirth();
        dto.city = skier.getCity();
        dto.subscriptionId = skier.getSubscription() != null ? skier.getSubscription().getNumSub():null;
        dto.pisteIds = skier.getPistes()
                .stream()
                .map(Piste::getNumPiste)
                .collect(Collectors.toSet());
        return dto;
    }

    // Helper method to convert DTO to Skier entity
    private Skier toEntity(SkierDTO dto) {
        Skier skier = new Skier();
        skier.setFirstName(dto.firstName);
        skier.setLastName(dto.lastName);
        skier.setDateOfBirth(dto.dateOfBirth);
        skier.setCity(dto.city);
        return skier;
    }

    @Operation(description = "Add Skier")
    @PostMapping("/add")
    public SkierDTO addSkier(@RequestBody SkierDTO skierDTO) {
        Skier skier = toEntity(skierDTO);
        Skier savedSkier = skierServices.addSkier(skier);
        return toDTO(savedSkier);
    }

    @Operation(description = "Add Skier And Assign To Course")
    @PostMapping("/addAndAssign/{numCourse}")
    public SkierDTO addSkierAndAssignToCourse(@RequestBody SkierDTO skierDTO,
                                              @PathVariable("numCourse") Long numCourse) {
        Skier skier = toEntity(skierDTO);
        Skier savedSkier = skierServices.addSkierAndAssignToCourse(skier, numCourse);
        return toDTO(savedSkier);
    }

    @Operation(description = "Assign Skier To Subscription")
    @PutMapping("/assignToSub/{numSkier}/{numSub}")
    public SkierDTO assignToSubscription(@PathVariable("numSkier") Long numSkier,
                                         @PathVariable("numSub") Long numSub) {
        Skier savedSkier = skierServices.assignSkierToSubscription(numSkier, numSub);
        return toDTO(savedSkier);
    }

    @Operation(description = "Assign Skier To Piste")
    @PutMapping("/assignToPiste/{numSkier}/{numPiste}")
    public SkierDTO assignToPiste(@PathVariable("numSkier") Long numSkier,
                                  @PathVariable("numPiste") Long numPiste) {
        Skier savedSkier = skierServices.assignSkierToPiste(numSkier, numPiste);
        return toDTO(savedSkier);
    }

    @Operation(description = "Retrieve Skiers By Subscription Type")
    @GetMapping("/getSkiersBySubscription")
    public List<SkierDTO> retrieveSkiersBySubscriptionType(@RequestParam TypeSubscription typeSubscription) {
        return skierServices.retrieveSkiersBySubscriptionType(typeSubscription)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Operation(description = "Retrieve Skier by Id")
    @GetMapping("/get/{id-skier}")
    public SkierDTO getById(@PathVariable("id-skier") Long numSkier) {
        Skier skier = skierServices.retrieveSkier(numSkier);
        return toDTO(skier);
    }

    @Operation(description = "Delete Skier by Id")
    @DeleteMapping("/delete/{id-skier}")
    public void deleteById(@PathVariable("id-skier") Long numSkier) {
        skierServices.removeSkier(numSkier);
    }

    @Operation(description = "Retrieve all Skiers")
    @GetMapping("/all")
    public List<SkierDTO> getAllSkiers() {
        return skierServices.retrieveAllSkiers()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
