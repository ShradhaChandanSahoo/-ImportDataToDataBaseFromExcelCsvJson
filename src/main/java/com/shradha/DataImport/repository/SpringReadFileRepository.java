package com.shradha.DataImport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shradha.DataImport.model.User;


public interface SpringReadFileRepository extends JpaRepository<User, Long> {

}
