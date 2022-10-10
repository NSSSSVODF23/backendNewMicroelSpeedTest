package com.microel.speedtest.repositories.interfaces;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.microel.speedtest.repositories.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    public User findByUsername(String username);

    public Boolean existsByUsername(String login);
}
