package com.switchfully.patekes.parksharkpatekes.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
public class Division {
    @Id
    @SequenceGenerator(name="division_seq", sequenceName = "division_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "division_seq")
    private Long id;
    private String name;
    private String originalName;
    private String director;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_id")
    private List<Division> subdivisions;

    public Division(String name, String originalName, String director) {
        this.name = name;
        this.originalName = originalName;
        this.director = director;
        this.subdivisions = new ArrayList<>();
    }

    public Division() {
    }
    public void addSubdivision(Division subdivision) {
        subdivisions.add(subdivision);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Division division = (Division) o;
        return Objects.equals(name, division.name) && Objects.equals(originalName, division.originalName) && Objects.equals(director, division.director) && Objects.equals(subdivisions, division.subdivisions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, originalName, director, subdivisions);
    }
}
