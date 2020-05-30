package com.github.tachesimazzoca.spring.examples.jpa.domain.model.item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
public class ItemImage {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "item_code")
    private String itemCode;

    private Integer imageNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_file_id")
    @ToString.Exclude
    private ImageFile imageFile;
}
