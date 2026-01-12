package com.apiorbit.lovableclone.impl;

import com.apiorbit.lovableclone.dto.plan.SubscriptionResponse;
import com.apiorbit.lovableclone.entity.Plan;
import com.apiorbit.lovableclone.entity.Subscription;
import com.apiorbit.lovableclone.entity.User;
import com.apiorbit.lovableclone.enumaration.SubscriptionStatus;
import com.apiorbit.lovableclone.error.NoResourceFoundException;
import com.apiorbit.lovableclone.mapper.SubscriptionResponseMapper;
import com.apiorbit.lovableclone.repository.PlanRepository;
import com.apiorbit.lovableclone.repository.SubscriptionRepository;
import com.apiorbit.lovableclone.repository.UserRepository;
import com.apiorbit.lovableclone.security.AuthUtil;
import com.apiorbit.lovableclone.service.SubscriptionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    AuthUtil authUtil;
    PlanRepository planRepository;
    UserRepository userRepository;
    SubscriptionRepository  subscriptionRepository;
    SubscriptionResponseMapper subscriptionResponseMapper;


    @Override
    public SubscriptionResponse getCurrentSubscription() {
        Long userId = authUtil.getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new NoResourceFoundException("User", userId.toString()));

        Subscription subscription = subscriptionRepository.findBySubscriberAndStatusIn(user, Set.of(SubscriptionStatus.ACTIVE,
                        SubscriptionStatus.PAST_DUE, SubscriptionStatus.INCOMPLETE))
                .orElseThrow(() -> new NoResourceFoundException("Subscription", userId.toString()));
        return subscriptionResponseMapper.toSubscriptionResponse(subscription);
    }

    @Override
    @Transactional
    public void handleSubscriptionUpdate(
            String subscriptionId,
            SubscriptionStatus status,
            Instant startTime,
            Instant endTime,
            Boolean cancelAtPeriodEnd,
            Long planId) {
        log.info("Inside handleSubscriptionUpdate");
        log.info("Login data subscriptionId: {}, SubscriptionStatus: {}, cancelAtPeriodEnd: {}, planId: {}", subscriptionId, status, cancelAtPeriodEnd, planId);
        log.info("Login Dates startTime: {}, endTime : {}", startTime, endTime);
        Boolean subscriptionChanged = Boolean.FALSE;

        Subscription subscription = getSubscriptionByStripeId(subscriptionId);
        if (subscription == null) {
            throw new NoResourceFoundException("Subscription", subscriptionId.toString());
        }

        if(subscription.getCancelAtPeriodEnd() != cancelAtPeriodEnd) {
            subscription.setCancelAtPeriodEnd(cancelAtPeriodEnd);
            subscriptionChanged = Boolean.TRUE;
        }
        if(subscription.getStatus() != status) {
            subscription.setStatus(status);
            subscriptionChanged = Boolean.TRUE;
        }

        if(subscription.getCurrentPeriodStart() != startTime) {
            subscription.setCurrentPeriodStart(startTime);
            subscriptionChanged = Boolean.TRUE;
        }

        if(subscription.getCurrentPeriodEnd() != endTime) {
            subscription.setCurrentPeriodEnd(endTime);
            subscriptionChanged = Boolean.TRUE;
        }

        if(!subscription.getStripeSubscriptionId().equals(subscriptionId)) {
            subscription.setStripeSubscriptionId(subscriptionId);
            Plan plan = getPlanById(planId);
            subscription.setPlan(plan);
            subscriptionChanged = Boolean.TRUE;
        }

        if(subscriptionChanged){
            log.info("Data changed for the subscription");
            subscriptionRepository.save(subscription);
        }

    }

    @Override
    @Transactional
    public void deleteSubscription(String subscriptionId) {
        Subscription subscription = getSubscriptionByStripeId(subscriptionId);
        subscription.setCancelAtPeriodEnd(Boolean.TRUE);
        subscriptionRepository.save(subscription);

    }

    @Override
    @Transactional
    public void createSubscription(
            Long planId,
            String subscriptionId,
            User user) {
        log.info("-------Inside createSubscription--------");
        log.info("Login data subscriptionId: {}, planId: {}", subscriptionId, planId);
        //Long userId = authUtil.getUserId();
        //User user = userRepository.findById(userId).orElseThrow(() -> new NoResourceFoundException("User", userId.toString()));
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new NoResourceFoundException("Plan", planId.toString()));
        Boolean isSubscriptionExists = subscriptionRepository.existsSubscriptionByStripeSubscriptionId(subscriptionId);
        log.info("isSubscriptionExists: {}", isSubscriptionExists);
        if(isSubscriptionExists) return;

        Subscription subscription = Subscription.builder()
                .subscriber(user)
                .plan(plan)
                .stripeSubscriptionId(subscriptionId)
                .status(SubscriptionStatus.INCOMPLETE)
                .build();

        subscriptionRepository.save(subscription);
        log.info("-------End createSubscription--------");
    }

    @Override
    @Transactional
    public void confirmSubscription(
            String subscriptionId,
            Instant startDate,
            Instant endDate) {
        log.info("--------Inside confirmSubscription--------");
        log.info("login data subscriptionId: {}, startDate: {}, endDate: {}", subscriptionId, startDate,  endDate);
        Subscription subscription = getSubscriptionByStripeId(subscriptionId);
        //In case the start date came as blank we set the start date as End date of Subscription
        Instant newStartDate = startDate==null? subscription.getCurrentPeriodEnd(): startDate;
        subscription.setCurrentPeriodStart(newStartDate);
        subscription.setCurrentPeriodEnd(endDate);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscriptionRepository.save(subscription);

    }

    @Override
    @Transactional
    public void markSubscriptionPastDue(String subscriptionId) {
        log.info("Inside markSubscriptionPastDue");
        log.info("login data subscriptionId: {}", subscriptionId);
        Subscription subscription = getSubscriptionByStripeId(subscriptionId);
        subscription.setStatus(SubscriptionStatus.PAST_DUE);
        subscriptionRepository.save(subscription);

    }

    private Subscription getSubscriptionByStripeId(String subscriptionId) {
        Subscription subscription =  subscriptionRepository.findByStripeSubscriptionId(subscriptionId)
                .orElse(new Subscription());
        log.info("subscription Object: {}", subscription);
        return subscription;
    }

    private Plan getPlanById(Long id){
        return planRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("Plan", id.toString()));
    }

}
