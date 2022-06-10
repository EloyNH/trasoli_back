package com.megafact.service.impl;

import com.megafact.model.GiroNegocio;
import com.megafact.repository.IGiroNegocioDAO;
import com.megafact.service.IGiroNegocioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class GiroNegocioServiceImpl extends CRUDImpl<GiroNegocio,Long> implements IGiroNegocioService {

    @Autowired
    private IGiroNegocioDAO dao;

    @Override
    protected JpaRepository<GiroNegocio, Long> getDao() {
        return dao;
    }
}
