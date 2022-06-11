package com.megafact.controller;

import com.megafact.dto.PersonalDTO;
import com.megafact.dto.RepresentanteLegalDTO;
import com.megafact.exception.ModeloNotFoundException;
import com.megafact.model.GiroNegocio;
import com.megafact.model.Persona;
import com.megafact.model.RepresentanteLegal;
import com.megafact.model.TipoDocumento;
import com.megafact.payload.CustomResponse;
import com.megafact.service.IPersonaService;
import com.megafact.service.IPersonalService;
import com.megafact.service.IRepresentanteLegalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/representantes")
@CrossOrigin(origins = "*")
public class RepresentanteLegalController {
    @Autowired
    private IRepresentanteLegalService service;

    @Autowired
    private IPersonaService personaService;

    @GetMapping(value = "/apicheck", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getHealthCheck() {
        return "{ \"Ok\" : true }";
    }

    @GetMapping(value = "/list-representante",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listar() {
        List<RepresentanteLegal> list = new ArrayList<>();
        try {
            list = service.listar();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @PostMapping(value = "/registrar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrar(@RequestBody RepresentanteLegalDTO rep) {

        try {
            Persona persona = personaService.registrar(rep.getPersona());
            rep.getRepresentanteLegal().setPersona(persona);
            RepresentanteLegal representanteLegal = service.registrar(rep.getRepresentanteLegal());
            return new ResponseEntity<>(representanteLegal, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/listar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listarId(@PathVariable("id") Long id) {
        RepresentanteLegal representanteLegal = service.listarPorId(id);
        if (id <= 0) {
            String msg = "el -> id " + id + " es invalido";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CustomResponse.builder().codigo(0).mensaje(msg).build());
        }

        if (representanteLegal == null) {
            //throw new ModeloNotFoundException("Id " + id+" no encontrado");
            String msg = "Id " + id + " no encontrado";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponse.builder().codigo(0).mensaje(msg).build());
        }

        if (representanteLegal != null) {
            //return new ResponseEntity<>(giroNegocio, HttpStatus.OK);
            return ResponseEntity.ok(representanteLegal);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

    }

//    @GetMapping("/query")
//    public ArrayList<RepresentanteLegal> obtenerUsuarioPorPrioridad(@RequestParam("num_partida") String num_partida){
//        return this.service.obtenerPorNumPartida(num_partida);
//    }

//    @DeleteMapping( path = "/{id}")
//    public String eliminarPorId(@PathVariable("id") Long id){
//        boolean ok = this.service.eliminarRepresentanteLegal(id);
//        if (ok){
//            return "Se eliminó el representante con id " + id;
//        }else{
//            return "No pudo eliminar el representante con id" + id;
//        }
//    }


    @PutMapping(value = "/modificar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modificar(@Valid @RequestBody RepresentanteLegalDTO rep) {
        try {
            Persona persona = personaService.listarPorId(rep.getPersona().getIdPersona());
            RepresentanteLegal repLegal = service.listarPorId(rep.getRepresentanteLegal().getIdRepresentanteLegal());
            if (repLegal == null) {
                throw new ModeloNotFoundException("Id de representante legal no encontrado " + rep.getRepresentanteLegal().getIdRepresentanteLegal());
            }

            if (persona == null) {
                throw new ModeloNotFoundException("Id de persona no encontrado " + rep.getPersona().getIdPersona());
            }

            persona.setCorreo(rep.getPersona().getCorreo());
            persona.setDireccion(rep.getPersona().getDireccion());
            persona.setTelefoMovil(rep.getPersona().getTelefoMovil());
            persona.setRazonSocial(rep.getPersona().getRazonSocial());
            persona.setNumeroDocumento(rep.getPersona().getNumeroDocumento());

            Persona personaDB = personaService.registrar(persona);
            repLegal.setNumeroPartida(rep.getRepresentanteLegal().getNumeroPartida());
            repLegal.setPersona(personaDB);
            RepresentanteLegal representanteLegalDB = service.registrar(repLegal);
            return new ResponseEntity<>(representanteLegalDB, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/eliminar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        RepresentanteLegal repLegal = service.listarPorId(id);

        if (repLegal == null) {
            throw new ModeloNotFoundException("Id no encontrado " + id);
        }

        service.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
