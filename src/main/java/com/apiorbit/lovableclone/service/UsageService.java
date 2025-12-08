package com.apiorbit.lovableclone.service;

import com.apiorbit.lovableclone.dto.usage.PlanLimitsResponse;
import com.apiorbit.lovableclone.dto.usage.UsageTodayResponse;
import org.jspecify.annotations.Nullable;

public interface UsageService {
    UsageTodayResponse getTodaysUsage(Long userId);

    PlanLimitsResponse getCurrentSubscriptionOfUser(Long userId);
}
