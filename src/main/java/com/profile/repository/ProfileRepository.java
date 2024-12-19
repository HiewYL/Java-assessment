package com.profile.repository;

import com.profile.model.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    Page<Profile> findByStatusCode(String statusCode, Pageable pageable);

    @Query("SELECT p FROM Profile p WHERE p.id = :userId and p.statusCode = 'A'")
    Optional<Profile> findActiveProfileById(Long userId);

    @Query(value = "SELECT MAX(id) FROM {h-schema}t_user_profile", nativeQuery = true)
    Long findMaxId();
}
