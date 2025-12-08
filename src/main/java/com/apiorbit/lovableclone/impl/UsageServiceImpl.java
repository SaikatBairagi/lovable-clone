package com.apiorbit.lovableclone.impl;

import com.apiorbit.lovableclone.dto.usage.PlanLimitsResponse;
import com.apiorbit.lovableclone.dto.usage.UsageTodayResponse;
import com.apiorbit.lovableclone.service.UsageService;
import org.springframework.stereotype.Service;

@Service
public class UsageServiceImpl implements UsageService {
    @Override
    public UsageTodayResponse getTodaysUsage(Long userId) {
        return null;
    }

    @Override
    public PlanLimitsResponse getCurrentSubscriptionOfUser(Long userId) {
        return null;
    }
}
