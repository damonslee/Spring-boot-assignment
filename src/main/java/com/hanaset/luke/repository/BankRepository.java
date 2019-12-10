package com.hanaset.luke.repository;

import com.hanaset.luke.entitiy.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<BankEntity, String> {

    BankEntity findByInstituteCode(String code);

    BankEntity findByInstituteName(String name);
}
