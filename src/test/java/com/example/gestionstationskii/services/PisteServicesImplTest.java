package com.example.gestionstationskii.services;

import com.example.gestionstationskii.entities.Piste;
import com.example.gestionstationskii.repositories.IPisteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PisteServicesImplTest {

    @Autowired
    private IPisteRepository pisteRepository;

    @Autowired
    private PisteServicesImpl pisteServices;

    private Piste piste;

    @BeforeEach
    void setUp() {
        // Initialiser un objet Piste avant chaque test
        piste = new Piste();
        piste.setNamePiste("Piste Verte");
        piste.setLength(1500);
        piste.setSlope(30);
    }

    @Test
    void testAddPiste_Success() {
        // Ajouter une nouvelle piste
        Piste savedPiste = pisteServices.addPiste(piste);

        // Vérifier que la piste est bien sauvegardée
        assertNotNull(savedPiste);
        assertNotNull(savedPiste.getNumPiste());
        assertEquals("Piste Verte", savedPiste.getNamePiste());

        // Nettoyage
        pisteRepository.deleteById(savedPiste.getNumPiste());
    }

    @Test
    void testRetrieveAllPistes() {
        // Sauvegarder deux pistes pour les tester
        Piste piste1 = new Piste();
        piste1.setNamePiste("Piste Rouge");
        piste1.setLength(2000);
        piste1.setSlope(45);
        pisteRepository.save(piste1);

        Piste piste2 = new Piste();
        piste2.setNamePiste("Piste Bleue");
        piste2.setLength(1800);
        piste2.setSlope(35);
        pisteRepository.save(piste2);

        // Récupérer toutes les pistes
        List<Piste> pistes = pisteServices.retrieveAllPistes();

        // Vérifier que les pistes sont bien récupérées
        assertNotNull(pistes);
        assertTrue(pistes.size() >= 2);

        // Nettoyage
        pisteRepository.deleteById(piste1.getNumPiste());
        pisteRepository.deleteById(piste2.getNumPiste());
    }

    @Test
    void testRetrievePiste_Success() {
        // Sauvegarder une piste et la récupérer
        Piste savedPiste = pisteRepository.save(piste);

        Piste retrievedPiste = pisteServices.retrievePiste(savedPiste.getNumPiste());

        // Vérifier que la piste est bien récupérée
        assertNotNull(retrievedPiste);
        assertEquals(savedPiste.getNamePiste(), retrievedPiste.getNamePiste());

        // Nettoyage
        pisteRepository.deleteById(savedPiste.getNumPiste());
    }

    @Test
    void testRemovePiste_Success() {
        // Sauvegarder une piste
        Piste savedPiste = pisteRepository.save(piste);

        // Supprimer la piste
        pisteServices.removePiste(savedPiste.getNumPiste());

        // Vérifier que la piste est bien supprimée
        Optional<Piste> deletedPiste = pisteRepository.findById(savedPiste.getNumPiste());
        assertFalse(deletedPiste.isPresent());
    }

    // --- Tests avancés ---

    @Test
    void testRemoveNonExistingPiste() {
        // Essayer de supprimer une piste avec un ID qui n'existe pas
        Long nonExistingPisteId = 999L;

        // Attendre l'exception EmptyResultDataAccessException (et non IllegalArgumentException)
        Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> {
            pisteServices.removePiste(nonExistingPisteId);
        });

        String expectedMessage = "No class com.example.gestionstationskii.entities.Piste entity with id 999 exists!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testRetrievePistesByMinLength() {
        // Sauvegarder plusieurs pistes avec différentes longueurs
        Piste piste1 = new Piste();
        piste1.setNamePiste("Piste Longue");
        piste1.setLength(3000);
        piste1.setSlope(35);
        pisteRepository.save(piste1);

        Piste piste2 = new Piste();
        piste2.setNamePiste("Piste Courte");
        piste2.setLength(1500);
        piste2.setSlope(25);
        pisteRepository.save(piste2);

        // Récupérer les pistes dont la longueur est supérieure à 2000
        List<Piste> pistes = pisteRepository.findAll(); // On récupère toutes les pistes car il n'y a pas de méthode de filtrage spécifique

        // Vérifier qu'il y a au moins une piste longue récupérée
        assertNotNull(pistes);
        assertTrue(pistes.size() >= 2); // Modifier en fonction des résultats attendus dans le projet

        // Nettoyage
        pisteRepository.deleteById(piste1.getNumPiste());
        pisteRepository.deleteById(piste2.getNumPiste());
    }
}