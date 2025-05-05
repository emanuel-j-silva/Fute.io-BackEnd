package com.io.fute.repository;

import com.io.fute.entity.Draw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrawRepository extends JpaRepository<Draw, Long> {

}
