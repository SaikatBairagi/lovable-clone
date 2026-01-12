package com.apiorbit.lovableclone.mapper;

import com.apiorbit.lovableclone.dto.plan.SubscriptionResponse;
import com.apiorbit.lovableclone.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface SubscriptionResponseMapper {

    @Mapping(source = "subscription.subscriber", target = "subscriber")
    SubscriptionResponse toSubscriptionResponse(Subscription subscription);
}
