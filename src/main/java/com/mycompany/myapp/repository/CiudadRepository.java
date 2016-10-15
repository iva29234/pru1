package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Ciudad;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Ciudad entity.
 */
@SuppressWarnings("unused")
public interface CiudadRepository extends JpaRepository<Ciudad,Long> {

}
