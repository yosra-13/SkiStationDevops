package com.example.gestionstationskii.services;

import com.example.gestionstationskii.entities.Piste;
import com.example.gestionstationskii.repositories.IPisteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PisteServicesImplMockitoTest {

    @Mock
    private IPisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteServices;

    private Piste piste;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Créer une piste factice pour les tests
        piste = new Piste();
        piste.setNumPiste(1L);
        piste.setNamePiste("Piste Verte");
        piste.setLength(1500);
        piste.setSlope(30);
    }

    @Test
    void testAddPiste_Success() {
        // Simuler l'ajout d'une piste
        when(pisteRepository.save(any(Piste.class))).thenReturn(piste);

        // Appel du service pour ajouter la piste
        Piste savedPiste = pisteServices.addPiste(piste);

        // Vérifier que la piste est sauvegardée correctement
        assertNotNull(savedPiste);
        assertEquals("Piste Verte", savedPiste.getNamePiste());

        // Vérifier que le repository a bien appelé la méthode save
        verify(pisteRepository, times(1)).save(any(Piste.class));
    }

    @Test
    void testRetrieveAllPistes() {
        // Simuler la récupération de plusieurs pistes
        List<Piste> pistes = Arrays.asList(
                piste,
                new Piste(2L, "Piste Bleue", null, 1800, 40, null)
        );
        when(pisteRepository.findAll()).thenReturn(pistes);

        // Appel du service pour récupérer toutes les pistes
        List<Piste> retrievedPistes = pisteServices.retrieveAllPistes();

        // Vérifier que les pistes sont bien récupérées
        assertNotNull(retrievedPistes);
        assertEquals(2, retrievedPistes.size());

        // Vérifier que le repository a bien appelé la méthode findAll
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void testRemovePiste_Success() {
        // Appel du service pour supprimer une piste
        pisteServices.removePiste(piste.getNumPiste());

        // Vérifier que le repository a bien appelé la méthode deleteById
        verify(pisteRepository, times(1)).deleteById(piste.getNumPiste());
    }

    @Test
    void testRetrievePiste_Success() {
        // Simuler la récupération d'une piste par son ID
        when(pisteRepository.findById(piste.getNumPiste())).thenReturn(Optional.of(piste));

        // Appel du service pour récupérer la piste
        Piste retrievedPiste = pisteServices.retrievePiste(piste.getNumPiste());

        // Vérifier que la piste est bien récupérée
        assertNotNull(retrievedPiste);
        assertEquals("Piste Verte", retrievedPiste.getNamePiste());

        // Vérifier que le repository a bien appelé la méthode findById
        verify(pisteRepository, times(1)).findById(piste.getNumPiste());
    }

    // --- Tests avancés ---

    @Test
    void testRemoveNonExistingPiste() {
        // Simuler la suppression d'une piste inexistante
        doThrow(new RuntimeException("Piste not found")).when(pisteRepository).deleteById(anyLong());

        // Vérifier qu'une exception est levée lors de la suppression
        assertThrows(RuntimeException.class, () -> {
            pisteServices.removePiste(999L);
        });

        // Vérifier que la méthode deleteById a été appelée
        verify(pisteRepository, times(1)).deleteById(999L);
    }

    @Test
    void testRetrievePistesByMinLength() {
        // Simuler plusieurs pistes avec des longueurs différentes
        Piste piste1 = new Piste(1L, "Piste Longue", null, 3000, 35, null);
        Piste piste2 = new Piste(2L, "Piste Courte", null, 1500, 25, null);
        List<Piste> pistes = Arrays.asList(piste1, piste2);

        // Simuler la méthode findAll pour renvoyer les pistes
        when(pisteRepository.findAll()).thenReturn(pistes);

        // Filtrer les pistes avec une longueur > 2000
        List<Piste> longPistes = pisteServices.retrieveAllPistes()
                .stream()
                .filter(p -> p.getLength() > 2000)
                .collect(Collectors.toList());

        // Vérifier que seule la piste longue est récupérée
        assertNotNull(longPistes);
        assertEquals(1, longPistes.size());
        assertEquals("Piste Longue", longPistes.get(0).getNamePiste());
    }
}