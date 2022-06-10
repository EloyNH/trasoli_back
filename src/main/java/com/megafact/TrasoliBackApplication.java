package com.megafact;

import com.megafact.model.TipoDocumento;
import com.megafact.service.ITipoDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrasoliBackApplication implements CommandLineRunner {

    @Autowired
    private ITipoDocumentoService service;

    public static void main(String[] args) {
        SpringApplication.run(TrasoliBackApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        TipoDocumento tipoDocumento = new TipoDocumento();
        tipoDocumento.setCodTipoDocumento("1");
        tipoDocumento.setAbreviatura("DNI");
        tipoDocumento.setDenominacion("Documento Nacional de Identidad");

        service.registrar(tipoDocumento);

        TipoDocumento tipoDocumento2 = new TipoDocumento();
        tipoDocumento2.setCodTipoDocumento("2");
        tipoDocumento2.setAbreviatura("RUC");
        tipoDocumento2.setDenominacion("Registro Único de Contribuyentes");

        service.registrar(tipoDocumento2);

    }
}
