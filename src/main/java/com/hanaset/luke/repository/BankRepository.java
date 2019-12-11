package com.hanaset.luke.repository;

import com.hanaset.luke.entitiy.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<BankEntity, String> {

    Optional<BankEntity> findByInstituteCode(String code);

    Optional<BankEntity> findByInstituteName(String name);
}
