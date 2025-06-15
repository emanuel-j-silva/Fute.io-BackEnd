package com.io.fute.repository;

import com.io.fute.entity.draw.Draw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DrawRepository extends JpaRepository<Draw, Long> {

    Optional<Draw> findFirstByGroupUserIdOrderByDateDesc(UUID userId);

    @Query("""
    SELECT COUNT(d) 
    FROM Draw d 
    JOIN d.group g 
    WHERE g.user.id = :userId
    """)
    Long countByUserId(@Param("userId") UUID userId);

}
