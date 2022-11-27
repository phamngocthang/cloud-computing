package com.cloudcomputing.repository;

import com.cloudcomputing.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    @Query("SELECT s FROM Subject s WHERE s.name LIKE %?1% ")
    List<Subject> findAllByContainKeyword(String keyword);

    @Query("SELECT s FROM Subject s WHERE s.id = ?1 ")
    Subject findOneById(Integer id);

}

