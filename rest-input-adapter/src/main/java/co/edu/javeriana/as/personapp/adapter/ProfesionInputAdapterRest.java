package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mapper.ProfesionMapperRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterRest {

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort profesionOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort profesionOutputPortMongo;

    @Autowired
    private ProfesionMapperRest profesionMapperRest;

    ProfessionInputPort profesionInputPort;

    private String setProfesionOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            profesionInputPort = new ProfessionUseCase(profesionOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            profesionInputPort = new ProfessionUseCase(profesionOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<ProfesionResponse> historial(String database) {
        log.info("Fetching professional history from Input Adapter");
        try {
            if(setProfesionOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())){
                return profesionInputPort.findAll().stream()
                        .map(profesionMapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            }else {
                return profesionInputPort.findAll().stream()
                        .map(profesionMapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }
            
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new ArrayList<>();
        }
    }

    public ProfesionResponse crearProfesion(ProfesionRequest request) {
        try {
            setProfesionOutputPortInjection(request.getDatabase());
            Profession profession = profesionInputPort.create(profesionMapperRest.fromAdapterToDomain(request));
            return profesionMapperRest.fromDomainToAdapterRestMaria(profession);
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
