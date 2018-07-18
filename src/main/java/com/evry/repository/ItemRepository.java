package com.evry.repository;

import com.evry.model.Item;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface ItemRepository extends CrudRepository<Item, Long> {

    @Modifying
    @Query("UPDATE Item i SET i.name = :name, i.price = :price, i.quantity = :quantity, i.category = :category WHERE i.id = :id")
    public void updateItem(@Param("id") Long id, @Param("name") String name, @Param("price") BigDecimal price, @Param("quantity") Long quantity, @Param("category") String category);
}
