package com.apiorbit.lovableclone.impl;

import com.apiorbit.lovableclone.dto.billing.CheckoutResponse;
import com.apiorbit.lovableclone.dto.billing.PortalResponse;
import com.apiorbit.lovableclone.dto.billing.checkoutRequest;
import com.apiorbit.lovableclone.dto.plan.SubscriptionResponse;
import com.apiorbit.lovableclone.enumaration.SubscriptionStatus;
import com.apiorbit.lovableclone.service.SubscriptionService;
import com.stripe.model.checkout.Session;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Override
    public SubscriptionResponse getCurrentSubscription(Long userId) {
        return null;
    }

    @Override
    public void createSubscription(
            Long planId,
            String stripeUserId,
            Session session) {

    }

    @Override
    public void handleSubscriptionUpdate(
            String subscriptionId,
            SubscriptionStatus status,
            Instant startTime,
            Instant endTime,
            Boolean cancelAtPeriodEnd,
            Long planId) {

    }

    @Override
    public void deleteSubscription(String subscriptionId) {

    }

}
