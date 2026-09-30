package org.example.subscriptionservice.controller;
import lombok.RequiredArgsConstructor;
import org.example.subscriptionservice.dto.SaveSubscriptionRequest;
import org.example.subscriptionservice.dto.SubscriptionDto;
import org.example.subscriptionservice.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService service;


    @GetMapping("/{login}")
    public ResponseEntity<SubscriptionDto> getByLogin(@PathVariable String login) {
        return ResponseEntity.ok(service.getByLogin(login));
    }


    @PostMapping
    public ResponseEntity<SubscriptionDto> save(@RequestBody SaveSubscriptionRequest request) {
        return ResponseEntity.ok(service.save(request));
    }
}