package com.example.gestionstationskii.entities;

import com.example.gestionstationskii.entities.Skier;
import com.example.gestionstationskii.entities.Piste;
import com.example.gestionstationskii.entities.Registration;
import com.example.gestionstationskii.entities.Subscription;
import com.example.gestionstationskii.entities.TypeSubscription;
import com.example.gestionstationskii.repositories.ISkierRepository;
import com.example.gestionstationskii.repositories.IPisteRepository;
import com.example.gestionstationskii.repositories.IRegistrationRepository;
import com.example.gestionstationskii.repositories.ISubscriptionRepository;
import com.example.gestionstationskii.services.ISkierServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class SkierTest {

    @Autowired
    private ISkierRepository skierRepository;

    @Autowired
    private IPisteRepository pisteRepository;

    @Autowired
    private ISubscriptionRepository subscriptionRepository;

    @Autowired
    private IRegistrationRepository registrationRepository;

    @Autowired
    private ISkierServices skierServices;

    private Skier skier1;
    private Subscription subscription;
    private Piste piste;

    @BeforeEach
    void setup() {
        skier1 = new Skier();
        skier1.setFirstName("John");
        skier1.setLastName("Doe");
        skier1.setDateOfBirth(LocalDate.of(1995, 5, 15));
        skier1.setCity("Paris");

        subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.now());
        skier1.setSubscription(subscription);

        skier1 = skierServices.addSkier(skier1);
    }

    @Test
    @Order(1)
    void testAddSkier() {
        assertNotNull(skier1);
        assertNotNull(skier1.getNumSkier());
        assertEquals("John", skier1.getFirstName());
        assertEquals("Doe", skier1.getLastName());
    }

    @Test
    @Order(2)
    void testRetrieveAllSkiers() {
        List<Skier> skiers = skierServices.retrieveAllSkiers();
        assertNotNull(skiers);
        assertFalse(skiers.isEmpty());
        assertTrue(skiers.stream().anyMatch(s -> s.getNumSkier().equals(skier1.getNumSkier())));
    }


    @Test
    @Order(3)
    void testRetrieveSkier_Success() {
        Skier retrievedSkier = skierServices.retrieveSkier(skier1.getNumSkier());
        assertNotNull(retrievedSkier);
        assertEquals(skier1.getNumSkier(), retrievedSkier.getNumSkier());
    }

    @Test
    @Order(4)
    void testRetrieveSkier_NotFound() {
        Skier retrievedSkier = skierServices.retrieveSkier(999L);
        assertNull(retrievedSkier);
    }

    @Test
    @Order(5)
    void testAssignSkierToSubscription() {
        Subscription newSubscription = new Subscription();
        newSubscription.setTypeSub(TypeSubscription.MONTHLY);
        newSubscription.setStartDate(LocalDate.now());
        subscription = subscriptionRepository.save(newSubscription);

        Skier updatedSkier = skierServices.assignSkierToSubscription(skier1.getNumSkier(), subscription.getNumSub());
        assertNotNull(updatedSkier);
        assertEquals(TypeSubscription.MONTHLY, updatedSkier.getSubscription().getTypeSub());
    }



    @Test
    @Order(6)
    void testRetrieveSkiersBySubscriptionType() {
        List<Skier> annualSkiers = skierServices.retrieveSkiersBySubscriptionType(TypeSubscription.ANNUAL);
        assertNotNull(annualSkiers);
        assertFalse(annualSkiers.isEmpty());
        assertTrue(annualSkiers.stream().anyMatch(s -> s.getNumSkier().equals(skier1.getNumSkier())));
    }



    @Test
    @Order(7)
    void testDeleteSkier() {
        Skier deletableSkier = new Skier();
        deletableSkier.setFirstName("Deletable");
        deletableSkier.setLastName("Skier");
        deletableSkier = skierRepository.save(deletableSkier);

        skierServices.removeSkier(deletableSkier.getNumSkier());
        Skier deletedSkier = skierServices.retrieveSkier(deletableSkier.getNumSkier());
        assertNull(deletedSkier);
    }
}

