package com.jsp.springboot.film.repositroy;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsp.springboot.film.entity.ResetToken;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {
    Optional<ResetToken> findByEmail(String email);
    Optional<ResetToken> findByToken(String token);
    void deleteByEmail(String email);
    
}

