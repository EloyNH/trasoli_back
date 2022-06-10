package com.megafact.controller;

import com.megafact.exception.ModeloNotFoundException;
import com.megafact.model.Empresa;
import com.megafact.model.TipoDocumento;
import com.megafact.service.IEmpresaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/empresas")
public class EmpresaController {

    @Autowired
    private IEmpresaService service;

    @GetMapping(value = "/healthcheck", produces = "application/json; charset=utf-8")
    public String getHealthCheck() {
        return "{ \"todoOk\" : true }";
    }

    @GetMapping(value = "/list-empresa", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listar() {
        List<Empresa> list = new ArrayList<>();
        try {
            list = service.listar();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/listar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listarId(@PathVariable("id") Long id) {
        Empresa empresa = service.listarPorId(id);
        if (empresa == null) {
            throw new ModeloNotFoundException("Id no encontrado " + id);
        }
        return new ResponseEntity<>(empresa, HttpStatus.OK);
    }


    @PostMapping(value = "/registrar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrar(@RequestBody Empresa empresa) {
        try {
            InetAddress ipManquina=InetAddress.getLocalHost();
            empresa.setIpMaquina(String.valueOf(ipManquina));
            Empresa empresa1 = service.registrar(empresa);
            return new ResponseEntity<>(empresa1, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/modificar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modificar(@Valid @RequestBody Empresa empresas) {
        try {
            Empresa obj = service.listarPorId(empresas.getIdEmpresa());
            if (obj == null) {
                throw new ModeloNotFoundException("Id no encontrado " + obj.getIdEmpresa());
            }
            Empresa empresa = service.modificar(obj);
            return new ResponseEntity<>(empresa, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/eliminar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        Empresa obj = service.listarPorId(id);

        if (obj == null) {
            throw new ModeloNotFoundException("Id no encontrado " + obj.getIdEmpresa());
        }

        service.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
