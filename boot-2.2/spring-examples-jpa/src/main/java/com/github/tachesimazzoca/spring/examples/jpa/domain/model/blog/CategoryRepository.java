package com.github.tachesimazzoca.spring.examples.jpa.domain.model.blog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
