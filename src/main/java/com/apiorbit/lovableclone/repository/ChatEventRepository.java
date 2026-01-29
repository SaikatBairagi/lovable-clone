package com.apiorbit.lovableclone.repository;

import com.apiorbit.lovableclone.entity.ChatEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatEventRepository extends JpaRepository<ChatEvent, Long> {
}
