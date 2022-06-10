package com.megafact.controller;

import com.megafact.exception.ModeloNotFoundException;
import com.megafact.model.TipoDocumento;
import com.megafact.service.ITipoDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/tipo-documentos")
@CrossOrigin(origins = "*")
public class TipoDocumentoController {

    @Autowired
    private ITipoDocumentoService service;

    @GetMapping(value = "/healthcheck", produces = "application/json; charset=utf-8")
    public String getHealthCheck() {
        return "{ \"todoOk\" : true }";
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listar() {
        List<TipoDocumento> list = new ArrayList<>();
        try {
            list = service.listar();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


//    @GetMapping(value = "/listar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> listarId(@PathVariable("id") Long id) {
//        try {
//            TipoDocumento tipoDocumento = service.listarPorId(id);
//            return new ResponseEntity<>(tipoDocumento, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }

    @GetMapping(value = "/listar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listarId(@PathVariable("id") Long id) {
        TipoDocumento tipoDocumento = service.listarPorId(id);
        if (tipoDocumento == null) {
            throw new ModeloNotFoundException("Id no encontrado " + id);
        }
        return new ResponseEntity<>(tipoDocumento, HttpStatus.OK);
    }

    @PostMapping(value = "/registrar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrar(@Valid @RequestBody TipoDocumento tipoDocumentos) {
        try {
            //InetAddress numeroIp4= InetAddress.getLocalHost();
            tipoDocumentos.setIpMaquina("0.0.0.0");
            service.registrar(tipoDocumentos);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(tipoDocumentos, HttpStatus.OK);
    }

    @PutMapping(value = "/modificar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modificar(@Valid @RequestBody TipoDocumento tipoDocumentos) {
        try {
            TipoDocumento obj = service.listarPorId(tipoDocumentos.getIdTipoDocumento());
            if (obj == null) {
                throw new ModeloNotFoundException("Id no encontrado " + obj.getIdTipoDocumento());
            }
            TipoDocumento tipoDocumento = service.modificar(obj);
            return new ResponseEntity<>(tipoDocumento, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/eliminar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        TipoDocumento obj = service.listarPorId(id);

        if (obj == null) {
            throw new ModeloNotFoundException("Id no encontrado " + obj.getIdTipoDocumento());
        }

        service.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
