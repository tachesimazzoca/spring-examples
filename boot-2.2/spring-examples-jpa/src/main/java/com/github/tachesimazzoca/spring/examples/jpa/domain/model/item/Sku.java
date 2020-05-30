package com.github.tachesimazzoca.spring.examples.jpa.domain.model.item;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Sku {
    @Id
    private String skuCode;

    private String skuName;
}
