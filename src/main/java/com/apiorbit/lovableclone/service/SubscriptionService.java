package com.apiorbit.lovableclone.service;


import com.apiorbit.lovableclone.dto.plan.SubscriptionResponse;
import com.apiorbit.lovableclone.entity.User;
import com.apiorbit.lovableclone.enumaration.SubscriptionStatus;
import com.stripe.model.checkout.Session;

import java.time.Instant;

public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription();

    void handleSubscriptionUpdate(
            String subscriptionId,
            SubscriptionStatus status,
            Instant startTime,
            Instant endTime,
            Boolean cancelAtPeriodEnd,
            Long planId);

    void deleteSubscription(String subscriptionId);

    void createSubscription(
            Long planId,
            String subscriptionId,
            User user);

    void confirmSubscription(
            String id,
            Instant startDate,
            Instant endDate);

    void markSubscriptionPastDue(String id);
}
