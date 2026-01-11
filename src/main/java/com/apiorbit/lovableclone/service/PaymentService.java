package com.apiorbit.lovableclone.service;

import com.apiorbit.lovableclone.dto.billing.CheckoutResponse;
import com.apiorbit.lovableclone.dto.billing.PortalResponse;
import com.apiorbit.lovableclone.dto.billing.checkoutRequest;
import com.stripe.model.StripeObject;

import java.util.Map;

public interface PaymentService {
    CheckoutResponse createCheckoutSession(
            checkoutRequest request);

    PortalResponse openCustomerPortal(Long userId);

    void handleWebhookRequest(
            String type,
            StripeObject stripeObject,
            Map<String, String> metadataMap);
}
