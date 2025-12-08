package com.apiorbit.lovableclone.controller;

import com.apiorbit.lovableclone.dto.billing.CheckoutResponse;
import com.apiorbit.lovableclone.dto.billing.PortalResponse;
import com.apiorbit.lovableclone.dto.billing.checkoutRequest;
import com.apiorbit.lovableclone.dto.plan.PlanResponse;
import com.apiorbit.lovableclone.dto.plan.SubscriptionResponse;
import com.apiorbit.lovableclone.service.PlanService;
import com.apiorbit.lovableclone.service.SubscriptionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("NullableProblems")
@RestController
@RequestMapping("/api")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BillingController {

    SubscriptionService subscriptionService;
    PlanService planService;

    @GetMapping("/plans")
    public ResponseEntity<List<PlanResponse>> getAllPlans(){
        return ResponseEntity.ok().body(planService.getAllActivePlan());
    }

    @GetMapping("/me/subscription")
    public ResponseEntity<SubscriptionResponse> getMySubscription(){
        Long userId = 1L;
        return ResponseEntity.ok().body(subscriptionService.getCurrentSubscription(userId));
    }

    @PostMapping("/api/stripe/checkout")
    public ResponseEntity<CheckoutResponse> createCheckoutResponse(@RequestBody checkoutRequest request){

        Long userId = 1L;
        return ResponseEntity.ok().body(subscriptionService.createCheckoutSession(request, userId));
    }

    @PostMapping("/api/stripe/portal")
    public ResponseEntity<PortalResponse> openCustomerPortal(){
        Long userId = 1L;
        return ResponseEntity.ok().body(subscriptionService.openCustomerPortal(userId));
    }
}
