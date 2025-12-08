package com.apiorbit.lovableclone.impl;

import com.apiorbit.lovableclone.dto.plan.PlanResponse;
import com.apiorbit.lovableclone.service.PlanService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {
    @Override
    public List<PlanResponse> getAllActivePlan() {
        return List.of();
    }
}
