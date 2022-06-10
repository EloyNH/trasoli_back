package com.megafact.controller;

import com.megafact.exception.ModeloNotFoundException;
import com.megafact.model.GiroNegocio;
import com.megafact.payload.CustomResponse;
import com.megafact.service.IGiroNegocioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/giro-negocios")
public class GiroNegocioController {

    @Autowired
    private IGiroNegocioService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listar() {
        List<GiroNegocio> list = new ArrayList<>();
        try {
            list = service.listar()
                    .stream().sorted(Comparator.comparing(GiroNegocio::getIdGiroNegocio).reversed())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @GetMapping(value = "/listar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listarId(@PathVariable("id") Long id) {
        GiroNegocio giroNegocio = service.listarPorId(id);
        if (id <= 0) {
            String msg = "el -> id " + id + " es invalido";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CustomResponse.builder().codigo(0).mensaje(msg).build());
        }

        if (giroNegocio == null) {
            //throw new ModeloNotFoundException("Id " + id+" no encontrado");
            String msg = "Id " + id + " no encontrado";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponse.builder().codigo(0).mensaje(msg).build());
        }

        if (giroNegocio != null) {
            //return new ResponseEntity<>(giroNegocio, HttpStatus.OK);
            return ResponseEntity.ok(giroNegocio);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

    }

    @PostMapping(value = "/registrar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrar(@Valid @RequestBody GiroNegocio giroNegocios) {
        try {
            if (giroNegocios == null) {
                String msg = "No se encontró el id " + giroNegocios.getIdGiroNegocio() + " que intenta modificar";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponse.builder().codigo(0).mensaje(msg).build());
            }

            GiroNegocio giroNegocio=service.registrar(giroNegocios);
            if (giroNegocio != null) {
                return ResponseEntity.ok(giroNegocio);
            }else {
                String msg = "Se presentó una condición inesperada que impidió completar la operación.";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponse.builder().codigo(0).mensaje(msg).build());
            }
        } catch (Exception e) {
            //return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            String msg = "Se presentó una condición inesperada que impidió completar la operación.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponse.builder().codigo(0).mensaje(msg).build());
        }
    }

    @PutMapping(value = "/actualizar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modificar(@Valid @RequestBody GiroNegocio giroNegocios) {
        try {
            GiroNegocio obj = service.listarPorId(giroNegocios.getIdGiroNegocio());
            if (obj == null) {
                String msg = "No se encontró el id " + obj.getIdGiroNegocio() + " que intenta modificar";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponse.builder().codigo(0).mensaje(msg).build());
            }

            GiroNegocio giroNegocio = service.modificar(giroNegocios);
            return ResponseEntity.ok(giroNegocio);
            //return new ResponseEntity<>(giroNegocio, HttpStatus.OK);

        } catch (Exception e) {
            //e.getCause().getCause().getMessage()
            //return new ResponseEntity<>(e.getCause().getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            String msg = "Se presentó una condición inesperada que impidió completar la operación.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponse.builder().codigo(0).mensaje(msg).build());
        }
    }

    @DeleteMapping(value = "/eliminar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> eliminar(@PathVariable("id") Long id) {
        try {
            GiroNegocio obj = service.listarPorId(id);

            if (id <= 0) {
                String msg = "el -> id " + id + " es invalido";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CustomResponse.builder().codigo(0).mensaje(msg).build());
            }

            if (obj == null) {
                //throw new ModeloNotFoundException("Id " + id+" no encontrado");
                String msg = "Id " + obj.getIdGiroNegocio() + " no encontrado";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponse.builder().codigo(0).mensaje(msg).build());
            }

            if (obj != null) {
                service.eliminar(id);
                return ResponseEntity.ok(HttpStatus.NO_CONTENT);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

        } catch (Exception e) {
            //e.getCause().getCause().getMessage()
            //return new ResponseEntity<>(e.getCause().getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            String msg = "Se presentó una condición inesperada que impidió completar la operación.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponse.builder().codigo(0).mensaje(msg).build());
        }
    }


}
