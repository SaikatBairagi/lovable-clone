package com.apiorbit.lovableclone.impl;

import com.apiorbit.lovableclone.dto.billing.CheckoutResponse;
import com.apiorbit.lovableclone.dto.billing.PortalResponse;
import com.apiorbit.lovableclone.dto.billing.checkoutRequest;
import com.apiorbit.lovableclone.dto.plan.SubscriptionResponse;
import com.apiorbit.lovableclone.service.SubscriptionService;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Override
    public SubscriptionResponse getCurrentSubscription(Long userId) {
        return null;
    }

    @Override
    public CheckoutResponse createCheckoutSession(
            checkoutRequest request,
            Long userId) {
        return null;
    }

    @Override
    public PortalResponse openCustomerPortal(Long userId) {
        return null;
    }
}
