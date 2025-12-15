package com.apiorbit.lovableclone.repository;

import com.apiorbit.lovableclone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<User, Long> {
}
