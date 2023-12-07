package com.full_stack.trendsetter.repository;

import com.full_stack.trendsetter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByEmail(String email);
}
