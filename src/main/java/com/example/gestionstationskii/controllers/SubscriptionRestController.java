package com.example.gestionstationskii.controllers;

import com.example.gestionstationskii.entities.Subscription;
import com.example.gestionstationskii.entities.TypeSubscription;
import com.example.gestionstationskii.services.ISubscriptionServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "\uD83D\uDC65 Subscription Management")
@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionRestController {

    private final ISubscriptionServices subscriptionServices;

    // Inner DTO class to encapsulate Subscription data
    public static class SubscriptionDTO {
         LocalDate startDate;
         LocalDate endDate;
         Float price;
         String typeSub;
    }

    // Helper method to convert Subscription entity to DTO
    private SubscriptionDTO toDTO(Subscription subscription) {
        SubscriptionDTO dto = new SubscriptionDTO();
        dto.startDate = subscription.getStartDate();
        dto.endDate = subscription.getEndDate();
        dto.price = subscription.getPrice();
        dto.typeSub = subscription.getTypeSub().name();
        return dto;
    }

    // Helper method to convert DTO to Subscription entity
    private Subscription toEntity(SubscriptionDTO dto) {
        Subscription subscription = new Subscription();
        subscription.setStartDate(dto.startDate);
        subscription.setEndDate(dto.endDate);
        subscription.setPrice(dto.price);
        subscription.setTypeSub(TypeSubscription.valueOf(dto.typeSub));
        return subscription;
    }

    @Operation(description = "Add Subscription")
    @PostMapping("/add")
    public SubscriptionDTO addSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        Subscription subscription = toEntity(subscriptionDTO);
        Subscription savedSubscription = subscriptionServices.addSubscription(subscription);
        return toDTO(savedSubscription);
    }

    @Operation(description = "Retrieve Subscription by Id")
    @GetMapping("/get/{id-subscription}")
    public SubscriptionDTO getById(@PathVariable("id-subscription") Long numSubscription) {
        Subscription subscription = subscriptionServices.retrieveSubscriptionById(numSubscription);
        return toDTO(subscription);
    }

    @Operation(description = "Retrieve Subscriptions by Type")
    @GetMapping("/all/{typeSub}")
    public Set<SubscriptionDTO> getSubscriptionsByType(@PathVariable("typeSub") TypeSubscription typeSubscription) {
        return subscriptionServices.getSubscriptionByType(typeSubscription)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toSet());
    }

    @Operation(description = "Update Subscription")
    @PutMapping("/update")
    public SubscriptionDTO updateSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        Subscription subscription = toEntity(subscriptionDTO);
        Subscription updatedSubscription = subscriptionServices.updateSubscription(subscription);
        return toDTO(updatedSubscription);
    }

    @Operation(description = "Retrieve Subscriptions created between two dates")
    @GetMapping("/all/{date1}/{date2}")
    public List<SubscriptionDTO> getSubscriptionsByDates(
            @PathVariable("date1") LocalDate startDate,
            @PathVariable("date2") LocalDate endDate) {
        return subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
