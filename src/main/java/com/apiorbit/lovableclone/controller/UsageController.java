package com.apiorbit.lovableclone.controller;

import com.apiorbit.lovableclone.dto.usage.PlanLimitsResponse;
import com.apiorbit.lovableclone.dto.usage.UsageTodayResponse;
import com.apiorbit.lovableclone.service.UsageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("NullableProblems")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usage")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UsageController {

    UsageService usageService;

    @GetMapping("/today")
    public ResponseEntity<UsageTodayResponse> userForToday(){
        Long userId = 1L;
        return ResponseEntity.ok().body(usageService.getTodaysUsage(userId));
    }

    @GetMapping("/limits")
    public ResponseEntity<PlanLimitsResponse> getPlanLimits(){
        Long userId = 1L;
        return ResponseEntity.ok().body(usageService.getCurrentSubscriptionOfUser(userId));
    }

}
