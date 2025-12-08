package com.apiorbit.lovableclone.service;

import com.apiorbit.lovableclone.dto.plan.PlanResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface PlanService {
    List<PlanResponse> getAllActivePlan();
}
