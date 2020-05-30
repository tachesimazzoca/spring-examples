package com.github.tachesimazzoca.spring.examples.jpa.domain.model.blog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
