package com.spring.crybot.repositories;

import com.spring.crybot.models.Price;
import org.springframework.data.repository.CrudRepository;

public interface PriceRepository extends CrudRepository<Price, String> {
}
