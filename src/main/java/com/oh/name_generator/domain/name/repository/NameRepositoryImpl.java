package com.oh.name_generator.domain.name.repository;


import com.oh.name_generator.domain.name.dto.CreateNameRequestDto;
import com.oh.name_generator.domain.name.dto.NameRequestDto;
import com.oh.name_generator.domain.name.dto.QCreateNameRequestDto;
import com.oh.name_generator.domain.name.type.Gender;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.oh.name_generator.domain.name.entity.QFirstName.firstName;
import static com.oh.name_generator.domain.name.entity.QSecondName.secondName;


@Repository
public class NameRepositoryImpl implements NameRepository {

    private JPAQueryFactory query;

    public NameRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<CreateNameRequestDto> createFirstNames(NameRequestDto nameRequestDto) {
        return query
                .select(new QCreateNameRequestDto(firstName.firstWord))
                .from(firstName)
                .where(firstName.gender.in(genderCond(nameRequestDto)))
                .fetch();
    }

    @Override
    public List<CreateNameRequestDto> createSecondNames(NameRequestDto nameRequestDto) {
        return query
                .select(new QCreateNameRequestDto(secondName.secondWord))
                .from(secondName)
                .where(secondName.gender.in(genderCond(nameRequestDto)))
                .fetch();
    }

    private static List<String> genderCond(NameRequestDto nameRequestDto) {
        List<String> genderCond = new ArrayList<>();
        String gender = nameRequestDto.getGender();
        genderCond.add(Gender.C.name());

        if (StringUtils.hasText(gender)) {
            genderCond.add(nameRequestDto.getGender());
        } else {
            genderCond.add(Gender.M.name());
            genderCond.add(Gender.W.name());
        }

        return genderCond;
    }

}
