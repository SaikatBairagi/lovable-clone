package com.apiorbit.lovableclone.entity;

import com.apiorbit.lovableclone.enumaration.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    User subscriber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,name = "plan_id")
    Plan plan;

    @Column(nullable = false)
    String stripeSubscriptionId;

    @Enumerated(EnumType.STRING)
    SubscriptionStatus status;

    Instant currentPeriodStart;
    Instant currentPeriodEnd;

    Boolean cancelAtPeriodEnd= Boolean.FALSE;

    @CreationTimestamp
    Instant createTs;

    @UpdateTimestamp
    Instant updateTs;

}
