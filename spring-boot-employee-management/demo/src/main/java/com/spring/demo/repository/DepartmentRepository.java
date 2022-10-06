package com.spring.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.demo.entity.Department;

@Transactional
@Repository
public interface DepartmentRepository  extends JpaRepository<Department, Long>{
    Optional<Department> getDepartmentByEmployeeEmployeeId(long id);

    void deleteByEmployeeEmployeeId(long id);

}
