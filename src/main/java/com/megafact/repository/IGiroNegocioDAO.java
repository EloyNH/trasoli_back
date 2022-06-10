package com.megafact.repository;

import com.megafact.model.GiroNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGiroNegocioDAO extends JpaRepository<GiroNegocio,Long> {

}
