package ru.bshein.aston.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bshein.aston.entities.Account;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

}
