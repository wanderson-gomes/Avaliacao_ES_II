package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Retorno;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Retorno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RetornoRepository extends JpaRepository<Retorno, Long> {}
