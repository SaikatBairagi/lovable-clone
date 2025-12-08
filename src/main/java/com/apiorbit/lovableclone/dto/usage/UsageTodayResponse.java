package com.apiorbit.lovableclone.dto.usage;

public record UsageTodayResponse(
        Integer tokensUser,
        Integer tokensLimit,
        Integer previewRunning,
        Integer previewLimit
) {
}
