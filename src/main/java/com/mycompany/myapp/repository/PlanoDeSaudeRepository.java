package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PlanoDeSaude;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PlanoDeSaude entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanoDeSaudeRepository extends JpaRepository<PlanoDeSaude, Long> {}
