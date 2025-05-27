package com.example.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "episodio")
public class Episodio {

    @Id
    private Long id;

    private String name;

    @Column(name = "air_date")
    private String airDate;

    private String episode;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "episode_characters",
        joinColumns = @JoinColumn(name = "episode_id"),
        inverseJoinColumns = @JoinColumn(name = "character_id")
    )
    private Set<Personaje> characters = new HashSet<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public Set<Personaje> getCharacters() {
        return characters;
    }

    public void setCharacters(Set<Personaje> characters) {
        this.characters = characters;
    }
}
