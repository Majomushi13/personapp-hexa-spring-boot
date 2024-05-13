package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.EstudiosMapperRest;
import co.edu.javeriana.as.personapp.model.request.EstudiosRequest;
import co.edu.javeriana.as.personapp.model.response.EstudiosResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudioInputAdapterRest {

    @Autowired
    @Qualifier("studyOutputAdapterMaria")
    private StudyOutputPort studyOutputPortMaria;

    @Autowired
    @Qualifier("studyOutputAdapterMongo")
    private StudyOutputPort studyOutputPortMongo;

    @Autowired
    private PersonInputPort personInputPort;

    @Autowired
    private ProfessionInputPort professionInputPort;

    @Autowired
    private EstudiosMapperRest estudiosMapperRest;

    StudyInputPort studyInputPort;

    private String setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
        switch (dbOption.toUpperCase()) {
            case "MARIA":
            case "MARIADB":
                studyInputPort = new StudyUseCase(studyOutputPortMaria);
                return DatabaseOption.MARIA.toString();
            case "MONGO":
            case "MONGODB":
                studyInputPort = new StudyUseCase(studyOutputPortMongo);
                return DatabaseOption.MONGO.toString();
            default:
                throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<EstudiosResponse> historialEstudios(String database) {
        log.info("Fetching study history from Input Adapter");
        try {
            if (setStudyOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return studyInputPort.findAll().stream()
                        .map(estudiosMapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            } else {
                return studyInputPort.findAll().stream()
                        .map(estudiosMapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new ArrayList<>();
        }
    }

    
    public EstudiosResponse crearEstudio(EstudiosRequest request, String database) {
        log.info("Creating new study through Input Adapter for database {}", database);
        try {
            setStudyOutputPortInjection(database);
            Person person = personInputPort.findOne(request.getPersonId());
            Profession profession = professionInputPort.findOne(request.getProfessionId());
    
            if (person == null || profession == null) {
                log.warn("Person or Profession not found");
                return null; // Podrías considerar devolver una respuesta más específica o lanzar una excepción personalizada.
            }
    
            Study study = new Study(person, profession, request.getGraduationDate(), request.getUniversityName());
            Study savedStudy = studyInputPort.create(study);
            return estudiosMapperRest.fromDomainToAdapterRest(savedStudy, database);
        } catch (InvalidOptionException | NoExistException e) {
            log.error("Error in creating study: {}", e.getMessage());
            return null; // Considera manejar de forma diferente dependiendo del tipo de error.
        }
    }
    

}

