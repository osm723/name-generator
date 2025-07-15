package com.oh.name_generator.domain.fortune.repository;

import com.oh.name_generator.domain.fortune.entity.Fortune;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FortuneRepository extends JpaRepository<Fortune, Long> {
}
