package com.college.transit_assist.repository;

import com.college.transit_assist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}