package com.apiorbit.lovableclone.repository;

import com.apiorbit.lovableclone.entity.Subscription;
import com.apiorbit.lovableclone.entity.User;
import com.apiorbit.lovableclone.enumaration.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByStripeSubscriptionId(String subscriptionId);

    Optional<Subscription> findBySubscriberAndStatusIn(
            User user,
            Set<SubscriptionStatus> active);

    Boolean existsSubscriptionByStripeSubscriptionId(String subscriptionId);
}
