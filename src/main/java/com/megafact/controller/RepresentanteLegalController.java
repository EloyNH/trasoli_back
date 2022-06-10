package com.megafact.controller;

import com.megafact.dto.PersonalDTO;
import com.megafact.dto.RepresentanteLegalDTO;
import com.megafact.exception.ModeloNotFoundException;
import com.megafact.model.Persona;
import com.megafact.model.RepresentanteLegal;
import com.megafact.model.TipoDocumento;
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

    /*@GetMapping( path = "/{id}")
    public Optional<RepresentanteLegal> obtenerRepresentantePorId(@PathVariable("id") Long id) {
        return this.service.listarPorId(id);
    }

    @GetMapping("/query")
    public ArrayList<RepresentanteLegal> obtenerUsuarioPorPrioridad(@RequestParam("num_partida") String num_partida){
        return this.service.obtenerPorNumPartida(num_partida);
    }

    @DeleteMapping( path = "/{id}")
    public String eliminarPorId(@PathVariable("id") Long id){
        boolean ok = this.service.eliminarRepresentanteLegal(id);
        if (ok){
            return "Se eliminó el representante con id " + id;
        }else{
            return "No pudo eliminar el representante con id" + id;
        }
    }*/


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

            Persona personaDB = personaService.registrar(rep.getPersona());
            rep.getRepresentanteLegal().setPersona(personaDB);
            RepresentanteLegal representanteLegalDB = service.registrar(rep.getRepresentanteLegal());
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
