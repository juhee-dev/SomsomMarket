package com.jh.SomsomMarket.repository;

import com.jh.SomsomMarket.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByIdAndPassword(String id, String password);
    Optional<Account> findById(String id);
    List<Account> findByEmailAndPhone(String email, String phone);
}
