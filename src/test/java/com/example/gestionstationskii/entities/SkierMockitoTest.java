package com.example.gestionstationskii.entities;

import com.example.gestionstationskii.repositories.IPisteRepository;
import com.example.gestionstationskii.repositories.ISkierRepository;
import com.example.gestionstationskii.repositories.ISubscriptionRepository;
import com.example.gestionstationskii.services.SkierServicesImpl;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;


import static org.mockito.Mockito.when;

import java.util.*;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
class SkierMockitoTest {

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private IPisteRepository pisteRepository;

    @InjectMocks
    private SkierServicesImpl skierServices;

    private Skier skier;
    private Subscription subscription;
    private Piste piste;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialiser un Skier
        skier = new Skier();
        skier.setNumSkier(1L);
        skier.setFirstName("John");
        skier.setLastName("Doe");
        skier.setDateOfBirth(LocalDate.of(1995, 1, 1));
        skier.setCity("Mountain City");

        // Initialiser une Subscription
        subscription = new Subscription();
        subscription.setNumSub(1L);
        subscription.setTypeSub(TypeSubscription.ANNUAL);

        // Initialiser une Piste
        piste = new Piste();
        piste.setNumPiste(1L);
        piste.setNamePiste("Blue Slope");
    }


   /* void testAddSkier_Success() {
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        Skier savedSkier = skierServices.addSkier(skier);

        assertNotNull(savedSkier);
        assertEquals(skier.getNumSkier(), savedSkier.getNumSkier());

        verify(skierRepository, times(1)).save(skier);
    }*/

  /*  public Skier addSkier(Skier skier) {
        if (skier.getSubscription() == null) {
            throw new IllegalArgumentException("Subscription cannot be null");
        }

        // Autres logiques de traitement...
        return skierRepository.save(skier);
    }*/

    @Test
    void testRetrieveSkier_Success() {
        when(skierRepository.findById(skier.getNumSkier())).thenReturn(Optional.of(skier));

        Skier retrievedSkier = skierServices.retrieveSkier(skier.getNumSkier());

        assertNotNull(retrievedSkier);
        assertEquals(skier.getFirstName(), retrievedSkier.getFirstName());

        verify(skierRepository, times(1)).findById(skier.getNumSkier());
    }

    @Test
    void testAssignSkierToSubscription_Success() {
        when(skierRepository.findById(skier.getNumSkier())).thenReturn(Optional.of(skier));
        when(subscriptionRepository.findById(subscription.getNumSub())).thenReturn(Optional.of(subscription));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        Skier updatedSkier = skierServices.assignSkierToSubscription(skier.getNumSkier(), subscription.getNumSub());

        assertNotNull(updatedSkier.getSubscription());
        assertEquals(subscription.getTypeSub(), updatedSkier.getSubscription().getTypeSub());

        verify(skierRepository, times(1)).save(skier);
    }

    @Test
    void testAssignSkierToPiste_Success() {
        when(skierRepository.findById(skier.getNumSkier())).thenReturn(Optional.of(skier));
        when(pisteRepository.findById(piste.getNumPiste())).thenReturn(Optional.of(piste));

        Set<Piste> pistes = new HashSet<>();
        pistes.add(piste);
        skier.setPistes(pistes);

        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        Skier updatedSkier = skierServices.assignSkierToPiste(skier.getNumSkier(), piste.getNumPiste());

        assertNotNull(updatedSkier.getPistes());
        assertTrue(updatedSkier.getPistes().contains(piste));

        verify(skierRepository, times(1)).save(skier);
    }

    @Test
    /*void testRetrieveSkiersBySubscriptionType_Success() {
        when(skierRepository.findBySubscription_TypeSub(TypeSubscription.ANNUAL)).thenReturn(Collections.singletonList(skier));

        List<Skier> skiers = skierServices.retrieveSkiersBySubscriptionType(TypeSubscription.ANNUAL);

        assertNotNull(skiers);
        assertFalse(skiers.isEmpty());
        assertEquals(TypeSubscription.ANNUAL, skiers.get(0).getSubscription().getTypeSub());

        verify(skierRepository, times(1)).findBySubscription_TypeSub(TypeSubscription.ANNUAL);
    }*/
    void testRetrieveSkiersBySubscriptionType_Success() {

        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusYears(1));
        subscription.setPrice(500f);
        subscription.setTypeSub(TypeSubscription.ANNUAL);


        Skier skier = new Skier();
        skier.setNumSkier(1L);
        skier.setFirstName("John");
        skier.setLastName("Doe");
        skier.setDateOfBirth(LocalDate.of(1990, 1, 1));
        skier.setCity("Alpes");
        skier.setSubscription(subscription);


        when(skierRepository.findBySubscription_TypeSub(TypeSubscription.ANNUAL))
                .thenReturn(Collections.singletonList(skier));


        List<Skier> skiers = skierServices.retrieveSkiersBySubscriptionType(TypeSubscription.ANNUAL);


        assertNotNull(skiers);
        assertFalse(skiers.isEmpty());
        assertEquals(TypeSubscription.ANNUAL, skiers.get(0).getSubscription().getTypeSub());


        verify(skierRepository, times(1)).findBySubscription_TypeSub(TypeSubscription.ANNUAL);
    }


    @Test
    void testRemoveSkier_Success() {
        doNothing().when(skierRepository).deleteById(skier.getNumSkier());

        skierServices.removeSkier(skier.getNumSkier());

        verify(skierRepository, times(1)).deleteById(skier.getNumSkier());
    }
}
