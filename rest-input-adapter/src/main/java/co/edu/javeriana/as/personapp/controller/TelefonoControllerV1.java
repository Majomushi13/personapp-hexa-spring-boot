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

import co.edu.javeriana.as.personapp.adapter.TelefonoInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/telefono")
public class TelefonoControllerV1 {

    @Autowired
    private TelefonoInputAdapterRest telefonoInputAdapterRest;

    @ResponseBody
    @GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TelefonoResponse> telefonos(@PathVariable String database) {
        log.info("Accessing telefonos REST API");
        return telefonoInputAdapterRest.historial(database.toUpperCase());
    }

    @ResponseBody
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public TelefonoResponse crearTelefono(@RequestBody TelefonoRequest request) {
        log.info("Entering method crearTelefono in the API controller");
        return telefonoInputAdapterRest.crearTelefono(request);
    }

    @ResponseBody
    @PutMapping(path = "/update/{number}/{database}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TelefonoResponse> actualizarTelefono(@PathVariable String number, @RequestBody TelefonoRequest request, @PathVariable String database) {
        log.info("Updating phone with number {} in database: {}", number, database);
        try {
            request.setDatabase(database);
            TelefonoResponse response = telefonoInputAdapterRest.actualizarTelefono(number, request);
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (InvalidOptionException e) {
            log.error("Invalid database option: {}", database, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (NoExistException e) {
            log.error("Phone with number {} not found in database: {}", number, database, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @ResponseBody
    @DeleteMapping(path = "/delete/{number}/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> eliminarTelefono(@PathVariable String number, @PathVariable String database) {
        log.info("Deleting phone with number {} from database: {}", number, database);
        try {
            telefonoInputAdapterRest.eliminarTelefono(number, database);
            return ResponseEntity.noContent().build();
        } catch (InvalidOptionException e) {
            log.error("Invalid database option: {}", database, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (NoExistException e) {
            log.error("Phone with number {} not found in database: {}", number, database, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
