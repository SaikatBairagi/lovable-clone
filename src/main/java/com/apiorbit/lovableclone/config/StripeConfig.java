package com.apiorbit.lovableclone.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StripeConfig {
    @Value("${stripe.api}")
    String stripeSecretKey;

    @PostConstruct
    public void setStripeSecretKey() {
        Stripe.apiKey = stripeSecretKey;
    }

}
