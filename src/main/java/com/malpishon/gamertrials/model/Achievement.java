package com.malpishon.gamertrials.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "achievements")
public class Achievement {
    @Id
    @GeneratedValue
    private Long id;

    private String codeName;
    private String name;
    private String description;
    private String iconUrl;
}
