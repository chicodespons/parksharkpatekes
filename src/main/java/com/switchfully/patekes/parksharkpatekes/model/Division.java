package com.switchfully.patekes.parksharkpatekes.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Division {
    @Id
    @SequenceGenerator(name="division_seq", sequenceName = "division_seq", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "division_seq")
    private long id;
    private String name;
    private String originalName;
    private String director;
    @OneToMany
    @JoinColumn(name = "fk_id")
    private List<Division> subdivisions;
}
