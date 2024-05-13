package co.edu.javeriana.as.personapp.mapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mariadb.adapter.ProfessionOutputAdapterMaria;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import co.edu.javeriana.as.personapp.mongo.adapter.ProfessionOutputAdapterMongo;

@Mapper
public class ProfesionMapperRest {

    public ProfesionResponse fromDomainToAdapterRestMaria( Profession profession){
        return fromDomainToAdapterRest(profession, "MariaDB");
    }

    public ProfesionResponse fromDomainToAdapterRestMongo(Profession profession) {
        return fromDomainToAdapterRest(profession, "MongoDB");
    }


    public Profession fromAdapterToDomain(ProfesionRequest request) {
        if (request == null) {
            return null;
        }
        return new Profession(
            request.getIdentification(),
            request.getName(),
            request.getDescription()
        );
    }

    // Método para convertir una entidad de dominio en una respuesta
    public ProfesionResponse fromDomainToAdapterRest(Profession profesion, String database) {
        if (profesion == null) {
            return null;
        }
        return new ProfesionResponse(
            profesion.getIdentification(),
            profesion.getName(),
            profesion.getDescription(),
            "OK"  // Supongamos que siempre retornamos "OK" como estado
        );
    }
}
