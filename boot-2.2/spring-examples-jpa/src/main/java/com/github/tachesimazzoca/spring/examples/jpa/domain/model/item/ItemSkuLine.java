package com.github.tachesimazzoca.spring.examples.jpa.domain.model.item;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Data;

@Data
@Embeddable
public class ItemSkuLine {

    @OneToOne
    @JoinColumn(name = "sku_code")
    private Sku sku;

    private Integer quantity;
}
