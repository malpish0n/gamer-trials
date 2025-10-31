package com.malpishon.gamertrials.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "badges")
public class Badge {
    @Id
    @GeneratedValue
    private Long id;

    private String codeName;
    private String name;
    private String iconUrl;
    private String description;
    private boolean isExclusive;
}
