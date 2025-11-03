package com.malpishon.gamertrials.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "challenges")
public class Challenge {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    private String name;
    private String description;
    private LocalDate dateOfCreation;
    private LocalDate dateOfClosing;
    private String status;
    private String winner;
    private boolean isPublic; // public or private
    private String game;
    private String difficulty;
    private String category; // (moba, fps etc.)
    private Integer rewardXp;
    private String type; // monthly, yearly etc.

    @ManyToMany
    @JoinTable(
            name = "challenge_badges",
            joinColumns = @JoinColumn(name = "challenge_id"),
            inverseJoinColumns = @JoinColumn(name = "badge_id")
    )
    private List<Badge> rewardBadges;

    @ManyToMany
    @JoinTable(
            name = "challenge_trophies",
            joinColumns = @JoinColumn(name = "challenge_id"),
            inverseJoinColumns = @JoinColumn(name = "trophy_id")
    )
    private List<Trophy> rewardTrophies;
}
