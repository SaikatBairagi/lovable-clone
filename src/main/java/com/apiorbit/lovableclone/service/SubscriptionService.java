package com.apiorbit.lovableclone.service;

import com.apiorbit.lovableclone.dto.billing.CheckoutResponse;
import com.apiorbit.lovableclone.dto.billing.PortalResponse;
import com.apiorbit.lovableclone.dto.billing.checkoutRequest;
import com.apiorbit.lovableclone.dto.plan.SubscriptionResponse;
import org.jspecify.annotations.Nullable;

public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription(Long userId);

    CheckoutResponse createCheckoutSession(
            checkoutRequest request,
            Long userId);

    PortalResponse openCustomerPortal(Long userId);
}
