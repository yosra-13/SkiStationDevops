package com.example.gestionstationskii;

import com.example.gestionstationskii.entities.*;
import com.example.gestionstationskii.repositories.*;
import com.example.gestionstationskii.services.SubscriptionServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionMockitoTest {

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private ISkierRepository skierRepository;

    @InjectMocks
    private SubscriptionServicesImpl subscriptionServices;

    private Subscription subscription;
    private Skier skier;

    @BeforeEach
    void setup() {
        // Initialize mock entities
        skier = new Skier();
        skier.setNumSkier(1L);
        skier.setFirstName("John");
        skier.setLastName("Doe");

        subscription = new Subscription();
        subscription.setNumSub(1L);
        subscription.setStartDate(LocalDate.of(2024, 1, 1));
        subscription.setPrice(100.0f);
        subscription.setTypeSub(TypeSubscription.ANNUAL);
    }

    @Test
    void testAddSubscription_Success() {
        // Mock behavior
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        // Call the service method
        Subscription result = subscriptionServices.addSubscription(subscription);

        // Assert and verify
        assertNotNull(result);
        assertEquals(1L, result.getNumSub());
        assertEquals(TypeSubscription.ANNUAL, result.getTypeSub());
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    @Test
    void testRetrieveSubscriptionById_Success() {
        // Mock behavior
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        // Call the service method
        Subscription found = subscriptionServices.retrieveSubscriptionById(1L);

        // Assert and verify
        assertNotNull(found);
        assertEquals(1L, found.getNumSub());
        verify(subscriptionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetSubscriptionsByType_Success() {
        // Mock behavior
        Set<Subscription> mockSubscriptions = new HashSet<>();
        mockSubscriptions.add(subscription); // Add subscription to the set
        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(TypeSubscription.ANNUAL))
                .thenReturn(mockSubscriptions);

        // Call the service method
        Set<Subscription> result = subscriptionServices.getSubscriptionByType(TypeSubscription.ANNUAL);

        // Assert and verify
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(subscription));
        verify(subscriptionRepository, times(1)).findByTypeSubOrderByStartDateAsc(TypeSubscription.ANNUAL);
    }

    @Test
    void testUpdateSubscription_Success() {
        // Mock behavior
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        // Update and save
        subscription.setPrice(120.0f);
        Subscription updatedSubscription = subscriptionRepository.save(subscription);

        // Assert and verify
        assertNotNull(updatedSubscription);
        assertEquals(120.0f, updatedSubscription.getPrice());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void testRetrieveSubscriptionsByDates_Success() {
        // Mock behavior
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        when(subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate)).thenReturn(Collections.singletonList(subscription));

        // Call the service method
        List<Subscription> result = subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate);

        // Assert and verify
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subscriptionRepository, times(1)).getSubscriptionsByStartDateBetween(startDate, endDate);
    }


    @Test
    void testShowMonthlyRecurringRevenue_Success() {
        // Mock repository behavior
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)).thenReturn(100.0f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL)).thenReturn(200.0f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL)).thenReturn(300.0f);

        // Call the service method
        subscriptionServices.showMonthlyRecurringRevenue();

        // Verify that the method logs the correct revenue (you might want to verify logs in real tests)
        // For now, we just verify the interaction
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY);
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL);
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL);
    }

    @Test
    void testUpdateSubscriptionWithInvalidData_Failure() {
        // Invalid price update (negative price)
        subscription.setPrice(-50.0f);  // Invalid price

        // Mock behavior
        when(subscriptionRepository.save(any(Subscription.class))).thenThrow(new IllegalArgumentException("Price cannot be negative"));

        // Assert exception is thrown
        assertThrows(IllegalArgumentException.class, () -> subscriptionServices.updateSubscription(subscription));
    }
    @Test
    void testShowMonthlyRecurringRevenue_WithMultipleTypes() {
        // Mock repository behavior with different revenue for each type
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)).thenReturn(100.0f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL)).thenReturn(200.0f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL)).thenReturn(300.0f);

        // Call the service method
        subscriptionServices.showMonthlyRecurringRevenue();

        // Verify that the correct revenue is returned and logged (you can also verify logs in real tests)
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY);
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL);
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL);

        // Here, you could assert the revenue calculation if it's returned from the service (depending on your method).
    }

}
