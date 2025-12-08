package com.apiorbit.lovableclone.dto.plan;

public record PlanResponse(
        Long id,
        String name,
        String stripePriceId,
        Integer maxProjects,
        Integer maxTokensPerDay,
        Integer maxPreview,
        Boolean unlimitedAi,
        String price
) {
}
