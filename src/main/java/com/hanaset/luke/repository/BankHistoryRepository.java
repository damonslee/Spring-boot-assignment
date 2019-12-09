package com.hanaset.luke.repository;

import com.hanaset.luke.entitiy.BankHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankHistoryRepository extends JpaRepository<BankHistoryEntity, Long> {

    @Query(value = "SELECT distinct year FROM taco.TB_BANK_HISTORY ORDER BY year", nativeQuery = true)
    List<Long> getYearList();

    List<BankHistoryEntity> findByYearOrderByMonth(Long year);
}
