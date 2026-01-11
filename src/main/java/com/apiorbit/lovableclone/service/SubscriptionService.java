package com.apiorbit.lovableclone.service;


import com.apiorbit.lovableclone.dto.plan.SubscriptionResponse;
import com.apiorbit.lovableclone.enumaration.SubscriptionStatus;
import com.stripe.model.checkout.Session;

import java.time.Instant;

public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription(Long userId);

    void createSubscription(
            Long planId,
            String stripeUserId,
            Session session);

    void handleSubscriptionUpdate(
            String subscriptionId,
            SubscriptionStatus status,
            Instant startTime,
            Instant endTime,
            Boolean cancelAtPeriodEnd,
            Long planId);

    void deleteSubscription(String subscriptionId);
}
