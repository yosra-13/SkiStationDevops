package com.example.gestionstationskii.entities;

import com.example.gestionstationskii.entities.Skier;
import com.example.gestionstationskii.entities.Subscription;
import com.example.gestionstationskii.repositories.ISkierRepository;
import com.example.gestionstationskii.repositories.ISubscriptionRepository;
import com.example.gestionstationskii.services.SkierServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class SkierTest {

        @Autowired
        private ISkierRepository skierRepository;

        @Autowired
        private ISubscriptionRepository subscriptionRepository; // Assure-toi d'avoir un repository pour Subscription

        @Autowired
        private SkierServicesImpl skierServices;

        private Subscription subscription;
        private Skier skier;

        @BeforeEach
        void setUp() {
            // Préparer une souscription pour les tests
            subscription = new Subscription();
            subscription.setTypeSub(TypeSubscription.ANNUAL); // Assure-toi d'avoir une valeur pour le type
            subscription = subscriptionRepository.save(subscription);

            // Préparer un skieur pour les tests
            skier = new Skier();
            skier.setFirstName("John");
            skier.setLastName("Doe");
            skier.setDateOfBirth(LocalDate.of(1990, 1, 1));
            skier.setCity("Example City");
            skier.setSubscription(subscription);
        }



        @Test
        void testRetrieveAllSkiers() {
            // Sauvegarder plusieurs skieurs pour les tests
            Skier skier1 = new Skier();
            skier1.setFirstName("Alice");
            skier1.setLastName("Wonderland");
            skierRepository.save(skier1);

            Skier skier2 = new Skier();
            skier2.setFirstName("Bob");
            skier2.setLastName("Builder");
            skierRepository.save(skier2);

            // Récupérer tous les skieurs via le service
            List<Skier> skiers = skierServices.retrieveAllSkiers();

            // Vérifier que les skieurs sont bien récupérés
            assertNotNull(skiers);
            assertTrue(skiers.size() >= 2);

            // Nettoyage
            skierRepository.deleteById(skier1.getNumSkier());
            skierRepository.deleteById(skier2.getNumSkier());
        }




    }
