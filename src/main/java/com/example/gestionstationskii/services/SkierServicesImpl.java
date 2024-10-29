package com.example.gestionstationskii.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.gestionstationskii.entities.*;
import com.example.gestionstationskii.repositories.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
@Service
public class SkierServicesImpl implements ISkierServices {

    private ISkierRepository skierRepository;

    private IPisteRepository pisteRepository;

    private ICourseRepository courseRepository;

    private IRegistrationRepository registrationRepository;

    private ISubscriptionRepository subscriptionRepository;
    private static final Logger log = LoggerFactory.getLogger(SkierServicesImpl.class);


    @Override
    public List<Skier> retrieveAllSkiers() {
        return skierRepository.findAll();
    }

    @Override
    public Skier addSkier(Skier skier) {
        switch (skier.getSubscription().getTypeSub()) {
            case ANNUAL:
                skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusYears(1));
                break;
            case SEMESTRIEL:
                skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusMonths(6));
                break;
            case MONTHLY:
                skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusMonths(1));
                break;
        }
        return skierRepository.save(skier);
    }

    @Override
    public Skier assignSkierToSubscription(Long numSkier, Long numSubscription) {
        // Retrieve skier and subscription
        Optional<Skier> optionalSkier = skierRepository.findById(numSkier);
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(numSubscription);

        // Check if either the skier or the subscription is not present
        if (!optionalSkier.isPresent() || !optionalSubscription.isPresent()) {
            log.warn("Skier or Subscription not found. Skier ID: {}, Subscription ID: {}", numSkier, numSubscription);
            return null;
        }

        // Get the entities from Optionals
        Skier skier = optionalSkier.get();
        Subscription subscription = optionalSubscription.get();

        // Assign subscription to skier
        skier.setSubscription(subscription);
        return skierRepository.save(skier);
    }


    @Override
    public Skier addSkierAndAssignToCourse(Skier skier, Long numCourse) {
        Skier savedSkier = skierRepository.save(skier);
        Course course = courseRepository.findById(numCourse).orElseThrow(()->new NullPointerException("course not found"));
        Set<Registration> registrations = savedSkier.getRegistrations();
        for (Registration r : registrations) {
            r.setSkier(savedSkier);
            r.setCourse(course);
            registrationRepository.save(r);
        }
        return savedSkier;
    }

    @Override
    public void removeSkier(Long numSkier) {
        skierRepository.deleteById(numSkier);
    }

    @Override
    public Skier retrieveSkier(Long numSkier) {
        return skierRepository.findById(numSkier).orElse(null);
    }

    @Override
    public Skier assignSkierToPiste(Long numSkieur, Long numPiste) {
        // Retrieve skier and piste from repositories
        Optional<Skier> optionalSkier = skierRepository.findById(numSkieur);
        Optional<Piste> optionalPiste = pisteRepository.findById(numPiste);

        // Check if both Skier and Piste exist
        if (!optionalSkier.isPresent() || !optionalPiste.isPresent()) {
            log.warn("Skier or Piste not found. Skier ID: {}, Piste ID: {}", numSkieur, numPiste);
            return null;
        }

        Skier skier = optionalSkier.get();
        Piste piste = optionalPiste.get();

        // Initialize or add to the skier's pistes
        if (skier.getPistes() == null) {
            skier.setPistes(new HashSet<>());
        }
        skier.getPistes().add(piste);

        log.info("Assigned Piste ID {} to Skier ID {}", numPiste, numSkieur);

        // Save and return the skier
        return skierRepository.save(skier);
    }


    @Override
    public List<Skier> retrieveSkiersBySubscriptionType(TypeSubscription typeSubscription) {
        return skierRepository.findBySubscription_TypeSub(typeSubscription);
    }
}