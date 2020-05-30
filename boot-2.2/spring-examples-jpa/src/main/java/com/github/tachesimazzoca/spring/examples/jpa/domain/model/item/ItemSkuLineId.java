package com.github.tachesimazzoca.spring.examples.jpa.domain.model.item;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ItemSkuLineId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "item_code")
    private String itemCode;

    @Column(name = "sku_code")
    private String skuCode;
}
