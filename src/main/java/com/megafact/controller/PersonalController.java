package com.megafact.controller;

import com.megafact.dto.PersonalDTO;
import com.megafact.model.Persona;
import com.megafact.model.Personal;
import com.megafact.payload.CustomResponse;
import com.megafact.service.IPersonaService;
import com.megafact.service.IPersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/api/personales")
@CrossOrigin(origins = "*")
public class PersonalController {

    @Autowired
    private IPersonalService service;

    @Autowired
    private IPersonaService personaService;

    @GetMapping(value = "/healthcheck", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getHealthCheck() {
        return "{ \"todoOk\" : true }";
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listar() {
        List<Personal> list = new ArrayList<>();
        try {
            list = service.listar().stream()
            .sorted(Comparator.comparing(Personal::getIdPersonal).reversed())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/listar-personal-id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listId(@PathVariable("id") Long id) {
        if (id<=0) {
            String msg="el -> id "+id+" es invalido";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CustomResponse.builder().codigo(0).mensaje(msg).build());
        }
        Personal optional= service.listarPorId(id);
        if (optional!=null) {
            return  ResponseEntity.ok(service.listarPorId(id));
        }else {
            return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

    }

//    @GetMapping(value = "/listar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> listarId(@PathVariable("id") Long id) {
//
//        if (id <= 0) {
//            String mgs="id -> "+id+" es invalido";
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CustomResponse.builder().codigo(0).mensaje(mgs).build());
//        }
//        Optional<Personal> optionalPersonal = service.listarPorId(id);
//        //return  ResponseEntity.ok(service.listarPorId(id).get());
//
//        if (optionalPersonal.isPresent()) {
//            return ResponseEntity.ok(service.listarPorId(id).get());
//        } else {
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        }
//
//        //validacion de documentos
//        /*if (!Objects.isNull(optionalPersonal)) {
//            if (optionalPersonal.isPresent()) {
//                return  ResponseEntity.ok(optionalPersonal.get());
//            }
//        }
//        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        */
//    }


    /*@PostMapping(value = "/registrar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrar(@Valid @RequestBody PersonalDTO rep) {

        try {
            Persona persona = personaService.registrar(rep.getPersona());
            rep.getPersonal().setPersona(persona);
            Personal personal = service.registrar(rep.getPersonal());
            return new ResponseEntity<>(personal, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    @PostMapping(value = "/registrar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrar(@Valid @RequestBody PersonalDTO perosonales) {
        try {
            if (perosonales == null) {
                String msg = "No se encontr?? el id " + perosonales.getPersonal().getIdPersonal() + " que intenta modificar";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponse.builder().codigo(0).mensaje(msg).build());
            }
            Persona persona = personaService.registrar(perosonales.getPersona());
            perosonales.getPersonal().setPersona(persona);
            Personal personal = service.registrar(perosonales.getPersonal());

            if (personal != null) {
                return ResponseEntity.ok(personal);
            }else {
                String msg = "Se present?? una condici??n inesperada que impidi?? completar la operaci??n.";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponse.builder().codigo(0).mensaje(msg).build());
            }
        } catch (Exception e) {
            //return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            String msg = "Se present?? una condici??n inesperada que impidi?? completar la operaci??n.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponse.builder().codigo(0).mensaje(msg).build());
        }
    }

}
