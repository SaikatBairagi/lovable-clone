package com.apiorbit.lovableclone.service;

import com.apiorbit.lovableclone.dto.billing.CheckoutResponse;
import com.apiorbit.lovableclone.dto.billing.PortalResponse;
import com.apiorbit.lovableclone.dto.billing.checkoutRequest;

public interface PaymentService {
    CheckoutResponse createCheckoutSession(
            checkoutRequest request);

    PortalResponse openCustomerPortal(Long userId);
}
