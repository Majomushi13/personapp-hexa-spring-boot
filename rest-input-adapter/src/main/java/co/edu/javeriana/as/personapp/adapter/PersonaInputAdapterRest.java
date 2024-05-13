package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mapper.PersonaMapperRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PersonaInputAdapterRest {

    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort personOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort personOutputPortMongo;

    @Autowired
    private PersonaMapperRest personaMapperRest;

    private PersonInputPort personInputPort;

    private String setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
        switch (dbOption.toUpperCase()) {
            case "MARIA":
            case "MARIADB":
                personInputPort = new PersonUseCase(personOutputPortMaria);
                return DatabaseOption.MARIA.toString();
            case "MONGO":
            case "MONGODB":
                personInputPort = new PersonUseCase(personOutputPortMongo);
                return DatabaseOption.MONGO.toString();
            default:
                throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<PersonaResponse> historial(String database) throws InvalidOptionException {
        log.info("Fetching person history from Input Adapter for database {}", database);
        setPersonOutputPortInjection(database);
        return personInputPort.findAll().stream()
                .map(personaMapperRest::fromDomainToAdapterRest)
                .collect(Collectors.toList());
    }

    public PersonaResponse crearPersona(PersonaRequest request) throws InvalidOptionException {
        log.info("Creating new person in database: {}", request.getDatabase());
        setPersonOutputPortInjection(request.getDatabase());
        Person person = personaMapperRest.fromAdapterToDomain(request);
        log.info("Person mapped: {}", person);
        Person savedPerson = personInputPort.create(person);
        log.info("Person saved: {}", savedPerson);
        return personaMapperRest.fromDomainToAdapterRest(savedPerson, request.getDatabase());
    }

    public PersonaResponse actualizarPersona(Integer id, PersonaRequest request) throws InvalidOptionException, NoExistException {
        log.info("Updating person with ID {} in database: {}", id, request.getDatabase());
        setPersonOutputPortInjection(request.getDatabase());
        Person person = personaMapperRest.fromAdapterToDomain(request);
        Person updatedPerson = personInputPort.edit(id, person);
        return personaMapperRest.fromDomainToAdapterRest(updatedPerson, request.getDatabase());
    }

    public void eliminarPersona(Integer id, String database) throws InvalidOptionException, NoExistException {
        log.info("Deleting person with ID {} from database: {}", id, database);
        setPersonOutputPortInjection(database);
        personInputPort.drop(id);
    }
}
