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

import co.edu.javeriana.as.personapp.adapter.ProfesionInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/profesion")
public class ProfesionControllerV1 {

    @Autowired
    private ProfesionInputAdapterRest profesionInputAdapterRest;

    @ResponseBody
    @GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProfesionResponse> profesiones(@PathVariable String database) {
        log.info("Accessing profesiones REST API with database: {}", database);
        return profesionInputAdapterRest.historial(database.toUpperCase());
    }

    @ResponseBody
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProfesionResponse crearProfesion(@RequestBody ProfesionRequest request) {
        log.info("Entering method crearProfesion in the API controller");
        return profesionInputAdapterRest.crearProfesion(request);
    }

    @ResponseBody
    @PutMapping(path = "/update/{id}/{database}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfesionResponse> actualizarProfesion(@PathVariable Integer id, @RequestBody ProfesionRequest request, @PathVariable String database) {
        log.info("Updating profession with ID {} in database: {}", id, database);
        try {
            request.setDatabase(database);
            ProfesionResponse response = profesionInputAdapterRest.actualizarProfesion(id, request);
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (InvalidOptionException e) {
            log.error("Invalid database option: {}", database, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (NoExistException e) {
            log.error("Profession with ID {} not found in database: {}", id, database, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @ResponseBody
    @DeleteMapping(path = "/delete/{id}/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> eliminarProfesion(@PathVariable Integer id, @PathVariable String database) {
        log.info("Deleting profession with ID {} from database: {}", id, database);
        try {
            profesionInputAdapterRest.eliminarProfesion(id, database);
            return ResponseEntity.noContent().build();
        } catch (InvalidOptionException e) {
            log.error("Invalid database option: {}", database, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (NoExistException e) {
            log.error("Profession with ID {} not found in database: {}", id, database, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
