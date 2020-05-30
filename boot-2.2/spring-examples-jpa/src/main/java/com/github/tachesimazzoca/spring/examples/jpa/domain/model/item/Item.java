package com.github.tachesimazzoca.spring.examples.jpa.domain.model.item;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Item {
    @Id
    @Column(name = "item_code")
    private String itemCode;

    private String itemName;

    @ElementCollection
    @CollectionTable(name = "item_sku_line", joinColumns = @JoinColumn(name = "item_code"))
    private List<ItemSkuLine> itemSkuLines = new ArrayList<>();

    // Unidirectional OneToMany relationship
    @OneToMany
    @JoinColumn(name = "item_code", referencedColumnName = "item_code",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), insertable = false,
            updatable = false)
    private List<ItemImage> itemImages = new ArrayList<>();
}
