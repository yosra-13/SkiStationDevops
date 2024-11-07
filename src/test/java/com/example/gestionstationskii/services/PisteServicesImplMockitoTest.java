package com.example.gestionstationskii.services;

import com.example.gestionstationskii.entities.Color;
import com.example.gestionstationskii.entities.Piste;
import com.example.gestionstationskii.repositories.IPisteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class PisteServicesImplTest {
    @Mock
    private IPisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteServices;

    private Piste piste;

    @BeforeEach // tet'hal kbal kol methode pretty self explanitory
    void setUp() {
        MockitoAnnotations.openMocks(this); // on initialise notre mock environment
        piste = new Piste();
        piste.setNumPiste(1L);
        piste.setNamePiste("Blue Mountain");
        piste.setColor(Color.BLUE);
        piste.setLength(500);
        piste.setSlope(25);
    }


    @Test
    void testRetrieveAllPistes() {
        List<Piste> pistes = new ArrayList<>();
        pistes.add(piste);
        when(pisteRepository.findAll()).thenReturn(pistes);

        List<Piste> result = pisteServices.retrieveAllPistes();

        assertEquals(1, result.size()); // houni on check if the returned lisst feha 1 element
        verify(pisteRepository, times(1)).findAll(); // we ensure that the findAll method was invoked once on the mock repository
    }

    @Test
     void testAddPiste() {
        when(pisteRepository.save(any(Piste.class))).thenReturn(piste);

        Piste result = pisteServices.addPiste(piste);

        assertEquals(piste.getNumPiste(), result.getNumPiste());
        verify(pisteRepository, times(1)).save(any(Piste.class));
    }

    @Test
    void testRemovePiste() {
        doNothing().when(pisteRepository).deleteById(anyLong()); // deletebyId retourne null so we don't expeect any actual return value hence doNothing

        pisteServices.removePiste(1L);

        verify(pisteRepository, times(1)).deleteById(anyLong()); //making sure deleteById was called once
    }

    @Test
    void testRetrievePiste() {
        when(pisteRepository.findById(anyLong())).thenReturn(Optional.of(piste)); // tells Mockito to return an Optional containing the piste when findById() is called.

        Piste result = pisteServices.retrievePiste(1L); //retrievePiste(1L) is called.

        assertEquals(piste.getNumPiste(), result.getNumPiste()); //confirms that the returned Piste has the expected NumPiste.
        verify(pisteRepository, times(1)).findById(anyLong());
    }


    @Test
    void testRetrievePisteNotFound() {
        when(pisteRepository.findById(anyLong())).thenReturn(Optional.empty()); //simulates the scenario where no Piste is found for the given ID.

        Piste result = pisteServices.retrievePiste(1L); // li lfouk khalet lmock  ykoul ay long nhotouh rajaali null donc result= null

        assertNull(result); //making sure raw null when no piste is found
        verify(pisteRepository, times(1)).findById(anyLong());
    }
}