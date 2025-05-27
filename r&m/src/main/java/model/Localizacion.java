package com.example.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "localizacion")
public class Localizacion {

    @Id
    private Long id;

    private String name;
    private String type;
    private String dimension;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private Set<Personaje> residents = new HashSet<>();


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public Set<Personaje> getResidents() {
        return residents;
    }

    public void setResidents(Set<Personaje> residents) {
        this.residents = residents;
    }
}
