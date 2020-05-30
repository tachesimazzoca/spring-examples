package com.github.tachesimazzoca.spring.examples.jpa.domain.model.blog;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue
    private long id;

    private String code;

    private String name;
}