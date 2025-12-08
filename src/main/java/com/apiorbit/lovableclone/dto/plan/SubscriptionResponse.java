package com.apiorbit.lovableclone.dto.plan;

import com.apiorbit.lovableclone.entity.User;
import com.apiorbit.lovableclone.enumaration.SubscriptionStatus;

import java.time.Instant;

public record SubscriptionResponse(
        Long id,
        User subscriber,
        PlanResponse plan,
        String stripeSubscriptionId,
        SubscriptionStatus status,
        Instant currentPeriodStart,
        Instant currentPeriodEnd,
        Boolean cancelAtPeriodEnd
) {
}
