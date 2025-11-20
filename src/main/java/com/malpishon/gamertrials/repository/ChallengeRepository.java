package com.malpishon.gamertrials.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.malpishon.gamertrials.model.Challenge;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
