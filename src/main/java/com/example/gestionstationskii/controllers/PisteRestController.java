package com.example.gestionstationskii.controllers;

import com.example.gestionstationskii.entities.Color;
import com.example.gestionstationskii.entities.Piste;
import com.example.gestionstationskii.entities.Skier;
import com.example.gestionstationskii.services.IPisteServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "\uD83C\uDFBF Piste Management")
@RestController
@RequestMapping("/piste")
@RequiredArgsConstructor
public class PisteRestController {

    private final IPisteServices pisteServices;

    // DTO class to encapsulate Piste data
    public static class PisteDTO {

  
        String namePiste;
        String color;
        int length;
        int slope;
        Set<Long> skierIds; // Exposing only skier IDs
    }

    // Helper method to map Piste entity to DTO
    private PisteDTO toDTO(Piste piste) {
        PisteDTO dto = new PisteDTO();
        dto.namePiste = piste.getNamePiste();
        dto.color = piste.getColor().name();
        dto.length = piste.getLength();
        dto.slope = piste.getSlope();
        dto.skierIds = piste.getSkiers()
                .stream()
                .map(Skier::getNumSkier)  // Method reference used here
                .collect(Collectors.toSet());
        return dto;
    }


    // Helper method to map DTO to Piste entity
    private Piste toEntity(PisteDTO dto) {
        Piste piste = new Piste();
        piste.setNamePiste(dto.namePiste);
        piste.setColor(Color.valueOf(dto.color));
        piste.setLength(dto.length);
        piste.setSlope(dto.slope);
        return piste;
    }

    @Operation(description = "Add Piste")
    @PostMapping("/add")
    public PisteDTO addPiste(@RequestBody PisteDTO pisteDTO) {
        Piste piste = toEntity(pisteDTO);
        Piste savedPiste = pisteServices.addPiste(piste);
        return toDTO(savedPiste);
    }

    @Operation(description = "Retrieve all Pistes")
    @GetMapping("/all")
    public List<PisteDTO> getAllPistes() {
        return pisteServices.retrieveAllPistes()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Operation(description = "Retrieve Piste by Id")
    @GetMapping("/get/{id-piste}")
    public PisteDTO getById(@PathVariable("id-piste") Long numPiste) {
        Piste piste = pisteServices.retrievePiste(numPiste);
        return toDTO(piste);
    }

    @Operation(description = "Delete Piste by Id")
    @DeleteMapping("/delete/{id-piste}")
    public void deleteById(@PathVariable("id-piste") Long numPiste) {
        pisteServices.removePiste(numPiste);
    }
}
