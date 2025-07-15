package com.oh.name_generator.domain.fortune.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "CF_FORTUNE", schema = "GEN")
public class Fortune {

    @Id
    @GeneratedValue
    @Column(name = "FORTUNE_ID")
    private Long id;

    private String sentence;

}
