package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Relatorio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Relatorio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {}
