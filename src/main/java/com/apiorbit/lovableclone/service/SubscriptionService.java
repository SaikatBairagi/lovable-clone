package com.apiorbit.lovableclone.service;


import com.apiorbit.lovableclone.dto.plan.SubscriptionResponse;

public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription(Long userId);
}
