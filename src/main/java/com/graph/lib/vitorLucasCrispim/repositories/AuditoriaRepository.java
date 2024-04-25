package com.graph.lib.vitorLucasCrispim.repositories;

import com.graph.lib.vitorLucasCrispim.entities.AuditoriaVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaRepository extends JpaRepository<AuditoriaVO,Long> {
}
