package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.PersonaInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/persona")
public class PersonaControllerV1 {

    @Autowired
    private PersonaInputAdapterRest personaInputAdapterRest;

    @ResponseBody
    @GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PersonaResponse>> personas(@PathVariable String database) {
        log.info("Fetching person list for database: {}", database);
        try {
            return ResponseEntity.ok(personaInputAdapterRest.historial(database.toUpperCase()));
        } catch (InvalidOptionException e) {
            log.error("Invalid database option: {}", database, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @ResponseBody
    @PostMapping(path = "/create/{database}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonaResponse> crearPersona(@RequestBody PersonaRequest request, @PathVariable String database) {
        log.info("Creating new person in database: {}", database);
        try {
            request.setDatabase(database);
            PersonaResponse response = personaInputAdapterRest.crearPersona(request);
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (InvalidOptionException e) {
            log.error("Invalid database option: {}", database, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @ResponseBody
    @PutMapping(path = "/update/{id}/{database}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonaResponse> actualizarPersona(@PathVariable Integer id, @RequestBody PersonaRequest request, @PathVariable String database) {
        log.info("Updating person with ID {} in database: {}", id, database);
        try {
            request.setDatabase(database);
            PersonaResponse response = personaInputAdapterRest.actualizarPersona(id, request);
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (InvalidOptionException e) {
            log.error("Invalid database option: {}", database, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (NoExistException e) {
            log.error("Person with ID {} not found in database: {}", id, database, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @ResponseBody
    @DeleteMapping(path = "/delete/{id}/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> eliminarPersona(@PathVariable Integer id, @PathVariable String database) {
        log.info("Deleting person with ID {} from database: {}", id, database);
        try {
            personaInputAdapterRest.eliminarPersona(id, database);
            return ResponseEntity.noContent().build();
        } catch (InvalidOptionException e) {
            log.error("Invalid database option: {}", database, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (NoExistException e) {
            log.error("Person with ID {} not found in database: {}", id, database, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
