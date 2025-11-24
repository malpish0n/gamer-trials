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
    private boolean isPublic;
    private String visibility;
    private String game;
    private String platform;
    private String difficulty;
    private String category;
    private Integer rewardXp;
    private String type;

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


    public Challenge() { }

        public Long getId () {
            return id;
        }

        public User getCreator () {
            return creator;
        }
        public void setCreator (User creator){
            this.creator = creator;
        }

        public String getName () {
            return name;
        }
        public void setName (String name){
            this.name = name;
        }

        public String getDescription () {
            return description;
        }
        public void setDescription (String description){
            this.description = description;
        }

        public LocalDate getDateOfCreation () {
            return dateOfCreation;
        }
        public void setDateOfCreation (LocalDate dateOfCreation){
            this.dateOfCreation = dateOfCreation;
        }

        public LocalDate getDateOfClosing () {
            return dateOfClosing;
        }
        public void setDateOfClosing (LocalDate dateOfClosing){
            this.dateOfClosing = dateOfClosing;
        }

        public String getStatus () {
            return status;
        }
        public void setStatus (String status){
            this.status = status;
        }

        public String getWinner () {
            return winner;
        }
        public void setWinner (String winner){
            this.winner = winner;
        }

        public boolean isPublic () {
            return isPublic;
        }
        public void setPublic ( boolean aPublic){
            isPublic = aPublic;
        }

        public String getVisibility () {
            return visibility;
        }
        public void setVisibility (String visibility){
            this.visibility = visibility;
        }

        public String getGame () {
            return game;
        }
        public void setGame (String game){
            this.game = game;
        }

        public String getPlatform () {
            return platform;
        }
        public void setPlatform (String platform){
            this.platform = platform;
        }

        public String getDifficulty () {
            return difficulty;
        }
        public void setDifficulty (String difficulty){
            this.difficulty = difficulty;
        }

        public String getCategory () {
            return category;
        }
        public void setCategory (String category){
            this.category = category;
        }

        public Integer getRewardXp () {
            return rewardXp;
        }
        public void setRewardXp (Integer rewardXp){
            this.rewardXp = rewardXp;
        }

        public String getType () {
            return type;
        }
        public void setType (String type){
            this.type = type;
        }
    }

