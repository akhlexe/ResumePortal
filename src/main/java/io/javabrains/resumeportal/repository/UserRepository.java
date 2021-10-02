package io.javabrains.resumeportal.repository;

import io.javabrains.resumeportal.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String userName);
}
