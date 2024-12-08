package com.sunny.sunnyfarm.repository;

import com.sunny.sunnyfarm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
