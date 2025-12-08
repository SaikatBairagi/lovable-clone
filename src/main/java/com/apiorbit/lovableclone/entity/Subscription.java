package com.apiorbit.lovableclone.entity;

import com.apiorbit.lovableclone.enumaration.SubscriptionStatus;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subscription {

    Long id;
    User subscriber;
    Plan plan;
    String stripeSubscriptionId;
    SubscriptionStatus status;
    Instant currentPeriodStart;
    Instant currentPeriodEnd;
    Boolean cancelAtPeriodEnd;
    Instant createTs;
    Instant updateTs;

}
