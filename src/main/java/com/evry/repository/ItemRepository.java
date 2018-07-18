package com.evry.repository;

import com.evry.model.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends CrudRepository<Item, Long> {

    @Query("SELECT i.quantity from Item i WHERE i.id =:id")
    public Long findQuantityById(@Param("id") Long id);
}
