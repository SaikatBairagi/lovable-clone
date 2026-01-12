package com.apiorbit.lovableclone.impl;

import com.apiorbit.lovableclone.dto.billing.CheckoutResponse;
import com.apiorbit.lovableclone.dto.billing.PortalResponse;
import com.apiorbit.lovableclone.dto.billing.checkoutRequest;
import com.apiorbit.lovableclone.entity.Plan;
import com.apiorbit.lovableclone.entity.User;
import com.apiorbit.lovableclone.enumaration.SubscriptionStatus;
import com.apiorbit.lovableclone.error.NoResourceFoundException;
import com.apiorbit.lovableclone.repository.PlanRepository;
import com.apiorbit.lovableclone.repository.UserRepository;
import com.apiorbit.lovableclone.security.AuthUtil;
import com.apiorbit.lovableclone.service.PaymentService;
import com.apiorbit.lovableclone.service.SubscriptionService;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StrpiePaymentServiceImpl implements PaymentService {
    private final AuthUtil  authUtil;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;

    @Value("${domain.url}")
    private String domainUrl;

    @Override
    public CheckoutResponse createCheckoutSession(
            checkoutRequest request) {
        Long userId = authUtil.getUserId();

        Plan plan = planRepository.findById(request.planId())
                .orElseThrow(() -> new NoResourceFoundException("No Plan found", request.planId().toString()));
        String stripePriceId = plan.getStripePriceId();
        User user = userRepository.findById(userId).orElseThrow(() -> new NoResourceFoundException("User", userId.toString()));

        //Stripe Subscription starts
        var params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setPrice(stripePriceId).setQuantity(1L).build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(domainUrl + "/success.html?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(domainUrl + "/failure.html")
                .putMetadata("UserId", userId.toString())
                .putMetadata("PlanId", plan.getId().toString());

        try {
            if(user.getStripeCustomerId() == null || user.getStripeCustomerId().isEmpty()) {
                params.setCustomerEmail(user.getEmail());
            }else{
                params.setCustomer(user.getStripeCustomerId());
            }
            Session session = Session.create(params.build());
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

    @Override
    public void handleWebhookRequest(
            String type,
            StripeObject stripeObject,
            Map<String, String> metadataMap) {
        log.info("Received Webhook Request {}",type);
        log.info("Received Webhook Request metadata {}",metadataMap);

        switch (type) {
            case "checkout.session.completed"-> handleCheckout((Session) stripeObject, metadataMap);
            case "customer.subscription.updated"-> handleSubscriptionUpdate((Subscription) stripeObject);
            case "customer.subscription.deleted"-> handleSubscriptionDeleted((Subscription)stripeObject);
            case "invoice.paid"-> handleInvoicePaid((Invoice)stripeObject);
            case "invoice_payment.failed"-> handleInvoiceFailed((Invoice) stripeObject);
            default-> log.info("Received Webhook Request {}",type);
        }

    }

    private void handleInvoiceFailed(Invoice invoice) {
        //Getting the Subscription Object from Invoice
        Subscription subscription = getSubscriptionFromInvoice(invoice);
        subscriptionService.markSubscriptionPastDue(subscription.getId());

    }

    private void handleSubscriptionDeleted(Subscription delSubscription) {
        if(delSubscription == null){
            log.info("Delete Subscription is null inside handleSubscriptionDeleted");
            return;
        }
        subscriptionService.deleteSubscription(delSubscription.getId());
    }

    private void handleSubscriptionUpdate(Subscription subscription) {
        if(subscription == null) {
            log.info("Subscription is null inside handleSubscriptionUpdate");
            return;
        }

        SubscriptionStatus status = mapSubscriptionStatusToEnum(subscription.getStatus());
        if(status == null){
            log.info("Subscription status '{}' is for Subscription {}", subscription.getStatus(),subscription.getId());
        }
        SubscriptionItem item = subscription.getItems().getData().get(0);
        Instant startTime = Instant.ofEpochSecond(item.getCurrentPeriodStart());
        Instant endTime = Instant.ofEpochSecond(item.getCurrentPeriodEnd());

        // get the Stripe price id to get the plan Id from Plan table
        Long planId = getPlanIdFromPrice(item.getPrice());

        subscriptionService.handleSubscriptionUpdate(subscription.getId(), status, startTime, endTime, subscription.getCancelAtPeriodEnd(), planId);

    }



    private void handleInvoicePaid(Invoice invoice) {
        //Getting the Subscription Object from Invoice
        Subscription subscription = getSubscriptionFromInvoice(invoice);
        if(subscription == null){
            log.info("Subscription is null inside handleInvoicePaid");
        }
        SubscriptionItem item = subscription.getItems().getData().get(0);
        Instant startDate = Instant.ofEpochSecond(item.getCurrentPeriodStart());
        Instant endDate = Instant.ofEpochSecond(item.getCurrentPeriodEnd());
        subscriptionService.confirmSubscription(subscription.getId(),startDate,endDate);

    }


    private void handleCheckout(
            Session session,
            Map<String, String> metadataMap) {

        if(session == null) {
            log.info("Session is null");
            return;
        }
        log.info("Received Checkout Request {}",metadataMap);
        Long userId = Long.parseLong(metadataMap.get("UserId"));
        Long planId = Long.parseLong(metadataMap.get("PlanId"));

        String subscriptionId = session.getSubscription();
        String stripeUserId = session.getCustomer();

        User user = userRepository.findById(userId).orElseThrow(() -> new NoResourceFoundException("User", userId.toString()));

        if(user.getStripeCustomerId()== null){
            user.setStripeCustomerId(stripeUserId);
            userRepository.save(user);
        }

        subscriptionService.createSubscription(planId, subscriptionId, user);



    }

    private SubscriptionStatus mapSubscriptionStatusToEnum(String status) {
        return switch (status) {
            case "active" ->  SubscriptionStatus.ACTIVE;
            case "trialing" ->  SubscriptionStatus.TRIALING;
            case "past_due", "unpaid", "paused", "incomplete_expired" ->  SubscriptionStatus.PAST_DUE;
            case "canceled" ->  SubscriptionStatus.CANCELLED;
            case "incomplete" ->  SubscriptionStatus.INCOMPLETE;
            default -> {
                log.info("No subscription status match found for  '{}'",status);
                yield null;
            }
        };
    }

    private Long getPlanIdFromPrice(Price price) {
        if(price == null || price.getId() == null) return null;
        return planRepository.findByStripePriceId(price.getId())
                .map(plan -> plan.getId())
                .orElse(null);
    }

    private Subscription getSubscriptionFromInvoice(Invoice invoice) {
        if(invoice == null) return null;
        String subscriptionId= invoice.getParent().getSubscriptionDetails().getSubscription();
        try {
            Subscription subscription= Subscription.retrieve(subscriptionId);
            if(subscription == null) throw new NoResourceFoundException("Subscription", subscriptionId.toString());
            return subscription;
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

    }
}
