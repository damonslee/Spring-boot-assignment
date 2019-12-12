package com.hanaset.luke.repository;

import com.hanaset.luke.entitiy.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUserId(String userId);

    Optional<UserEntity> findByUserIdAndPassword(String userId, String password);

}
