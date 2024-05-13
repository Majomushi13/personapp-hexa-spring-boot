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

import co.edu.javeriana.as.personapp.adapter.EstudioInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.request.EstudiosRequest;
import co.edu.javeriana.as.personapp.model.response.EstudiosResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/estudio")
public class EstudioControllerV1 {

    @Autowired
    private EstudioInputAdapterRest estudioInputAdapterRest;

    @ResponseBody
    @GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EstudiosResponse> estudios(@PathVariable String database) {
        log.info("Accessing estudios REST API with database: {}", database);
        return estudioInputAdapterRest.historialEstudios(database.toUpperCase());
    }

    @PostMapping(path = "/create/{database}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EstudiosResponse> createStudy(@RequestBody EstudiosRequest request, @PathVariable String database) {
        try {
            EstudiosResponse response = estudioInputAdapterRest.crearEstudio(request, database);
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(path = "/update/{idProf}/{ccPer}/{database}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EstudiosResponse> actualizarEstudio(@PathVariable Integer idProf, @PathVariable Integer ccPer, @RequestBody EstudiosRequest request, @PathVariable String database) {
        try {
            EstudiosResponse response = estudioInputAdapterRest.actualizarEstudio(idProf, ccPer, request, database);
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (InvalidOptionException e) {
            log.error("Invalid database option: {}", database, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (NoExistException e) {
            log.error("Study not found for profession ID {} and person ID {} in database: {}", idProf, ccPer, database, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping(path = "/delete/{idProf}/{ccPer}/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> eliminarEstudio(@PathVariable Integer idProf, @PathVariable Integer ccPer, @PathVariable String database) {
        log.info("Deleting study for profession ID {} and person ID {} from database: {}", idProf, ccPer, database);
        try {
            estudioInputAdapterRest.eliminarEstudio(idProf, ccPer, database);
            return ResponseEntity.noContent().build();
        } catch (InvalidOptionException e) {
            log.error("Invalid database option: {}", database, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (NoExistException e) {
            log.error("Study not found for profession ID {} and person ID {} in database: {}", idProf, ccPer, database, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
