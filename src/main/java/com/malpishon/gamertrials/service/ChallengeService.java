package com.malpishon.gamertrials.service;

import org.springframework.stereotype.Service;
import com.malpishon.gamertrials.model.Challenge;
import com.malpishon.gamertrials.repository.ChallengeRepository;

import java.util.List;

@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    public ChallengeService(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }

    public Challenge createChallenge(Challenge challenge) {
        return challengeRepository.save(challenge);
    }

    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    public Challenge getById(Long id) {
        return challengeRepository.findById(id).orElse(null);
    }

    public Challenge updateChallenge(Challenge challenge) {
        // save behaves as update when id is present
        return challengeRepository.save(challenge);
    }

    public void deleteById(Long id) {
        challengeRepository.deleteById(id);
    }
}
