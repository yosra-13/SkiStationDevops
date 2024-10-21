package com.example.gestionstationskii;

import com.example.gestionstationskii.entities.Skier;
import com.example.gestionstationskii.entities.Subscription;
import com.example.gestionstationskii.entities.TypeSubscription;
import com.example.gestionstationskii.repositories.ISkierRepository;
import com.example.gestionstationskii.repositories.ISubscriptionRepository;
import com.example.gestionstationskii.services.SubscriptionServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Ensure test order
public class SubscriptionTest {

    @Autowired
    private ISubscriptionRepository subscriptionRepository;

    @Autowired
    private ISkierRepository skierRepository;

    @Autowired
    private SubscriptionServicesImpl subscriptionServices;

    private Subscription subscription;
    private Skier skier;

    @BeforeEach
    void setUp() {
        subscription = new Subscription(null, LocalDate.now(), null, 100.0f, TypeSubscription.MONTHLY);
        skier = new Skier(null, "John", "Doe", LocalDate.of(1995, 5, 21), "New York", null, null, null);
        skier = skierRepository.save(skier); // Save the skier for testing
    }

    @Test
    @Order(1)
    void testAddSubscription_Success() {
        Subscription savedSubscription = subscriptionServices.addSubscription(subscription);
        assertNotNull(savedSubscription);
        assertNotNull(savedSubscription.getNumSub());
        assertEquals(subscription.getPrice(), savedSubscription.getPrice());
    }

    @Test
    @Order(2)
    void testUpdateSubscription_Success() {
        subscription = subscriptionRepository.save(subscription); // Save first
        subscription.setPrice(200.0f); // Update the price

        Subscription updatedSubscription = subscriptionServices.updateSubscription(subscription);

        assertNotNull(updatedSubscription);
        assertEquals(200.0f, updatedSubscription.getPrice());
    }

    @Test
    @Order(3)
    void testRetrieveSubscriptionById_Success() {
        subscription = subscriptionRepository.save(subscription); // Save first

        Subscription retrievedSubscription = subscriptionServices.retrieveSubscriptionById(subscription.getNumSub());

        assertNotNull(retrievedSubscription);
        assertEquals(subscription.getNumSub(), retrievedSubscription.getNumSub());
    }

    @Test
    @Order(4)
    void testRetrieveSubscriptionById_NotFound() {
        Subscription result = subscriptionServices.retrieveSubscriptionById(999L); // Non-existent ID

        assertNull(result);
    }

    @Test
    @Order(5)
    void testGetSubscriptionByType() {
        subscription.setTypeSub(TypeSubscription.MONTHLY);
        subscriptionRepository.save(subscription); // Save a monthly subscription

        Set<Subscription> subscriptions = subscriptionServices.getSubscriptionByType(TypeSubscription.MONTHLY);

        assertNotNull(subscriptions);
        assertFalse(subscriptions.isEmpty());
        assertEquals(TypeSubscription.MONTHLY, subscriptions.iterator().next().getTypeSub());
    }

    @Test
    @Order(6)
    void testAssignSubscriptionToSkier_Success() {
        // Save subscription
        subscription = subscriptionRepository.save(subscription);

        // Assign subscription to skier
        String result = subscriptionServices.assignSubscriptionToSkier(subscription.getNumSub(), skier.getNumSkier());

        // Assert success message
        assertEquals("Subscription assigned to Skier successfully", result);

        // Retrieve the skier from the repository
        Optional<Skier> optionalSkier = skierRepository.findById(skier.getNumSkier());

        // Ensure the skier exists
        assertTrue(optionalSkier.isPresent(), "Skier not found in the repository!");

        // Get the skier object
        Skier updatedSkier = optionalSkier.get();

        // Compare individual fields to avoid object identity issues
        assertNotNull(updatedSkier.getSubscription());
        assertEquals(subscription.getNumSub(), updatedSkier.getSubscription().getNumSub());
        assertEquals(subscription.getPrice(), updatedSkier.getSubscription().getPrice());
        assertEquals(subscription.getStartDate(), updatedSkier.getSubscription().getStartDate());
        assertEquals(subscription.getEndDate(), updatedSkier.getSubscription().getEndDate());
        assertEquals(subscription.getTypeSub(), updatedSkier.getSubscription().getTypeSub());
    }

    @Test
    @Order(7)
    void testRetrieveSubscriptions_ScheduledTask() {
            subscriptionRepository.save(subscription); // Save subscription

            // Simulate the scheduled task
            subscriptionServices.retrieveSubscriptions();
            // No assertions needed since it's a scheduled task printing logs
            }

    @Test
     @Order(8)
            void testShowMonthlyRecurringRevenue() {
            // Save some subscriptions to simulate recurring revenue
            subscriptionRepository.save(new Subscription(null, LocalDate.now(), null, 50.0f, TypeSubscription.MONTHLY));
            subscriptionRepository.save(new Subscription(null, LocalDate.now(), null, 300.0f, TypeSubscription.ANNUAL));
            subscriptionRepository.save(new Subscription(null, LocalDate.now(), null, 150.0f, TypeSubscription.SEMESTRIEL));

            // Simulate the scheduled revenue calculation
            subscriptionServices.showMonthlyRecurringRevenue();
            // No assertions needed since it prints logs for verification
            }
    @Test
    @Order(9)
    void testDeleteSubscription() {
            subscription = subscriptionRepository.save(subscription); // Save subscription

            subscriptionRepository.deleteById(subscription.getNumSub());

            Optional<Subscription> deletedSubscription = subscriptionRepository.findById(subscription.getNumSub());
            assertFalse(deletedSubscription.isPresent());
    }
}

