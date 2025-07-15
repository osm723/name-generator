package com.oh.name_generator.domain.name.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Entity
@Getter
@Table(name = "CF_FIRST_NAME", schema = "GEN")
public class FirstName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @NotEmpty
    private String firstWord;

    @NotEmpty
    private String gender;

    
}