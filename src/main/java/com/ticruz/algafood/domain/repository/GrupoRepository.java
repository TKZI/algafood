package com.ticruz.algafood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ticruz.algafood.domain.model.Grupo;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long>{

}
