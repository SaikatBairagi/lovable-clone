package com.apiorbit.lovableclone.impl;

import com.apiorbit.lovableclone.dto.billing.CheckoutResponse;
import com.apiorbit.lovableclone.dto.billing.PortalResponse;
import com.apiorbit.lovableclone.dto.billing.checkoutRequest;
import com.apiorbit.lovableclone.entity.Plan;
import com.apiorbit.lovableclone.error.NoResourceFoundException;
import com.apiorbit.lovableclone.repository.PlanRepository;
import com.apiorbit.lovableclone.security.AuthUtil;
import com.apiorbit.lovableclone.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StrpiePaymentServiceImpl implements PaymentService {
    private final AuthUtil  authUtil;
    private final PlanRepository planRepository;

    @Value("${domain.url}")
    private String domainUrl;

    @Override
    public CheckoutResponse createCheckoutSession(
            checkoutRequest request) {
        Long userId = authUtil.getUserId();

        Plan plan = planRepository.findById(request.planId())
                .orElseThrow(() -> new NoResourceFoundException("No Plan found", request.planId().toString()));
        String stripePriceId = plan.getStripePriceId();

        //Stripe Subscription starts
        SessionCreateParams params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setPrice(stripePriceId).setQuantity(1L).build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(domainUrl + "/success.html?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(domainUrl + "/failure.html")
                .build();
        try {
            Session session = Session.create(params);
            return new CheckoutResponse(session.getUrl());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
        //String Subscription Ends
    }

    @Override
    public PortalResponse openCustomerPortal(Long userId) {
        return null;
    }
}
