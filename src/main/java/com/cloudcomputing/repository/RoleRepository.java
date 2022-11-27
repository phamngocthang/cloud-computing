package com.cloudcomputing.repository;

import com.cloudcomputing.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("Select u.roles from User u where u.id = ?1")
    List<Role> findAllByUserId(Integer userId);
}
