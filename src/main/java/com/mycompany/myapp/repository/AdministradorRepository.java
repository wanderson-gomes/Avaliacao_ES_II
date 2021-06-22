package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Administrador;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Administrador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {}
