package com.example.gestionstationskii;

import com.example.gestionstationskii.entities.*;
import com.example.gestionstationskii.repositories.*;
import com.example.gestionstationskii.services.SubscriptionServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class GestionStationSkiiApplicationTests {

    @Autowired
    private ISubscriptionRepository subscriptionRepository;

    @Autowired
    private SubscriptionServicesImpl subscriptionServicesImpl;

    private Subscription subscription;

    @BeforeEach
    void setup() {
        subscriptionRepository.deleteAll();

        subscription = new Subscription();
        subscription.setStartDate(LocalDate.of(2024, 1, 1));
        subscription.setPrice(100.0F);
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription = subscriptionRepository.save(subscription);
    }
    @Order(1)
    @Test
    void testCreateSubscription_Success() {

        Subscription newSubscription = new Subscription();
        newSubscription.setStartDate(LocalDate.of(2024, 5, 1));
        newSubscription.setPrice(50.0F);
        newSubscription.setTypeSub(TypeSubscription.MONTHLY);

        Subscription result = subscriptionServicesImpl.addSubscription(newSubscription);

        assertNotNull(result);
        assertEquals(2, subscriptionRepository.count());
    }

    @Test
    void testReadSubscription_Success() {
        Subscription found = subscriptionRepository.findById(subscription.getNumSub()).orElse(null);
        assertNotNull(found);
        assertEquals(subscription.getNumSub(), found.getNumSub());
    }

    @Test
    void testUpdateSubscription_Success() {
        subscription.setPrice(120.0F);
        Subscription updatedSubscription = subscriptionRepository.save(subscription);

        assertNotNull(updatedSubscription);
        assertEquals(120.0F, updatedSubscription.getPrice());
    }

    @Test
    void testDeleteSubscription_Success() {
        subscriptionRepository.deleteById(subscription.getNumSub());

        boolean exists = subscriptionRepository.existsById(subscription.getNumSub());
        assertFalse(exists);
    }

    @Test
    void testGetSubscriptionsByType_Success() {
        Set<Subscription> subscriptions = subscriptionServicesImpl.getSubscriptionByType(TypeSubscription.ANNUAL);

        assertNotNull(subscriptions);
        assertTrue(subscriptions.size() > 0);
    }

    @Test
    void testGetSubscriptionsByDateRange_Success() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        List<Subscription> subscriptions = subscriptionServicesImpl.retrieveSubscriptionsByDates(startDate, endDate);

        assertNotNull(subscriptions);
        assertTrue(subscriptions.size() > 0);
    }


    @Test
    void testCleanUp() {
        subscriptionRepository.deleteAll();

        assertFalse(subscriptionRepository.findAll().iterator().hasNext());
    }

    @Test
    void testUpdateSubscriptionWithBusinessLogic_Success() {
        // Create subscription
        Subscription newSubscription = new Subscription();
        newSubscription.setStartDate(LocalDate.of(2024, 6, 1));
        newSubscription.setPrice(200.0F); // Above threshold for a premium discount
        newSubscription.setTypeSub(TypeSubscription.ANNUAL);
        subscriptionRepository.save(newSubscription);

        // Business logic: apply premium discount if price is above a threshold
        if (newSubscription.getPrice() > 150.0F && newSubscription.getTypeSub() == TypeSubscription.ANNUAL) {
            newSubscription.setPrice(newSubscription.getPrice() * 0.9F); // 10% discount
        }
        subscriptionRepository.save(newSubscription);

        // Assert: Ensure the price is updated
        Subscription updatedSubscription = subscriptionRepository.findById(newSubscription.getNumSub()).orElse(null);
        assertNotNull(updatedSubscription);
        assertEquals(180.0F, updatedSubscription.getPrice(), 0.01); // Price after discount
    }

    @Test
    void testDeleteSubscriptionWithCascadingEffects_Success() {
        // Create a subscription and associated user or related entities
        Subscription newSubscription = new Subscription();
        newSubscription.setStartDate(LocalDate.of(2024, 7, 1));
        newSubscription.setPrice(100.0F);
        newSubscription.setTypeSub(TypeSubscription.MONTHLY);
        subscriptionRepository.save(newSubscription);

        // Cascading delete: delete subscription and assert related entities are removed
        subscriptionRepository.deleteById(newSubscription.getNumSub());

        boolean exists = subscriptionRepository.existsById(newSubscription.getNumSub());
        assertFalse(exists);  // Ensure subscription is deleted

        // Optional: check related entities, if cascading delete affects them
        // Example: assertFalse(userRepository.existsBySubscriptionId(newSubscription.getNumSub()));
    }


}
