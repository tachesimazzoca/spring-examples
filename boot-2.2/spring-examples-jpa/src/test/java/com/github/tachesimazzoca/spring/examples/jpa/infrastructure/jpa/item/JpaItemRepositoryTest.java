package com.github.tachesimazzoca.spring.examples.jpa.infrastructure.jpa.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.tachesimazzoca.spring.examples.jpa.domain.model.item.ImageFile;
import com.github.tachesimazzoca.spring.examples.jpa.domain.model.item.ImageFileRepository;
import com.github.tachesimazzoca.spring.examples.jpa.domain.model.item.Item;
import com.github.tachesimazzoca.spring.examples.jpa.domain.model.item.ItemImage;
import com.github.tachesimazzoca.spring.examples.jpa.domain.model.item.ItemImageRepository;
import com.github.tachesimazzoca.spring.examples.jpa.domain.model.item.ItemRepository;
import com.github.tachesimazzoca.spring.examples.jpa.domain.model.item.ItemSkuLine;
import com.github.tachesimazzoca.spring.examples.jpa.domain.model.item.Sku;
import com.github.tachesimazzoca.spring.examples.jpa.domain.model.item.SkuRepository;
import com.github.tachesimazzoca.spring.examples.jpa.infrastructure.jpa.AbstractDataJpaTest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JpaItemRepositoryTest extends AbstractDataJpaTest {

    @Autowired
    private ImageFileRepository imageFileRepository;

    @Autowired
    private ItemImageRepository itemImageRepository;

    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private ItemRepository itemRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testUnidirectionalOneToMany() {

        ImageFile imageFile = new ImageFile();
        imageFile.setPath("a.jpg");
        imageFile = imageFileRepository.save(imageFile);
        imageFileRepository.flush();

        ItemImage itemImage = new ItemImage();
        itemImage.setItemCode("A00001");
        itemImage.setImageNo(1);
        itemImage.setImageFile(imageFile);
        itemImage = itemImageRepository.save(itemImage);
        itemImageRepository.flush();

        Sku sku = new Sku();
        sku.setSkuCode("S00001");
        sku.setSkuName("SKU - S00001");
        sku = skuRepository.save(sku);
        skuRepository.flush();

        ItemSkuLine itemSkuLine = new ItemSkuLine();
        itemSkuLine.setSku(sku);
        itemSkuLine.setQuantity(1);

        Item item = new Item();
        item.setItemCode("A00001");
        item.setItemName("Item-10001");
        item.getItemSkuLines().add(itemSkuLine);
        // Clearing elements doesn't affect because insertable and updatable are false.
        item.getItemImages().clear();
        // itemRepository.save(item);
        // itemRepository.flush();
        entityManager.persist(item);
        entityManager.flush();
        entityManager.detach(item);

        // Item persistedItem = entityManager.find(Item.class, "A00001");
        Item persistedItem = itemRepository.findById("A00001").get();
        log.info(persistedItem.toString());
        assertEquals(1, persistedItem.getItemImages().size());

        persistedItem.getItemSkuLines().clear();
        itemRepository.flush();
        itemSkuLine.getSku().setSkuName("Updated SKU - S00001");
        persistedItem.getItemSkuLines().add(itemSkuLine);
        itemRepository.flush();
    }
}
