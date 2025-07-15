package com.oh.name_generator.domain.name.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Entity
@Getter
@Table(name = "CF_SECOND_NAME", schema = "GEN")
public class SecondName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @NotEmpty
    private String secondWord;

    @NotEmpty
    private String gender;


}
