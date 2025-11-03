package com.malpishon.gamertrials.service;

import com.malpishon.gamertrials.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class UserService {

    public int xpPerLevelForLevel(int level) {
        if (level < 1) level = 1;
        int block = (level - 1) / 10;
        return (block + 1) * 1000;
    }

    public int cumulativeXpToReachLevel(int level) {
        if (level <= 1) return 0;
        long total = 0;
        for (int i = 1; i < level; i++) {
            total += xpPerLevelForLevel(i);
        }
        return (int) Math.min(Integer.MAX_VALUE, total);
    }

    public int getXpToNextLevel(User user) {
        if (user == null) return 0;
        int level = user.getLevel() != null ? user.getLevel() : 1;
        int totalXp = user.getXp() != null ? user.getXp() : 0;

        int currentLevelStartXp = cumulativeXpToReachLevel(level);
        int xpIntoLevel = Math.max(0, totalXp - currentLevelStartXp);
        int requiredThisLevel = xpPerLevelForLevel(level);
        int remaining = requiredThisLevel - xpIntoLevel;
        return Math.max(0, remaining);
    }

    public int getXpProgressPercent(User user) {
        if (user == null) return 0;
        int level = user.getLevel() != null ? user.getLevel() : 1;
        int totalXp = user.getXp() != null ? user.getXp() : 0;
        int currentLevelStartXp = cumulativeXpToReachLevel(level);
        int xpIntoLevel = Math.max(0, totalXp - currentLevelStartXp);
        int requiredThisLevel = xpPerLevelForLevel(level);
        if (requiredThisLevel <= 0) return 0;
        int pct = (int) Math.floor(100.0 * Math.min(requiredThisLevel, Math.max(0, xpIntoLevel)) / requiredThisLevel);
        if (pct < 0) return 0;
        if (pct > 100) return 100;
        return pct;
    }

    public Integer getPublicAge(User user) {
        if (user == null) return null;
        if (!user.isShowBirthDate()) return null;
        LocalDate birth = user.getBirthDate();
        if (birth == null) return null;
        LocalDate now = LocalDate.now();
        if (birth.isAfter(now)) return null;
        return Period.between(birth, now).getYears();
    }
}
