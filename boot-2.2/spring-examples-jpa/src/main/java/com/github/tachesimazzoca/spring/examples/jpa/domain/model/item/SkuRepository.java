package com.github.tachesimazzoca.spring.examples.jpa.domain.model.item;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SkuRepository extends JpaRepository<Sku, String> {
}
