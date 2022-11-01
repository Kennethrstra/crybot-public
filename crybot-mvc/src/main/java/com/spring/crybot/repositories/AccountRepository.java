package com.spring.crybot.repositories;

import com.spring.crybot.models.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, String> {
}
