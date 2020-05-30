package com.github.tachesimazzoca.spring.examples.jpa.domain.model.item;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ImageFile {
    @Id
    @GeneratedValue
    private Long id;

    private String path;
}
