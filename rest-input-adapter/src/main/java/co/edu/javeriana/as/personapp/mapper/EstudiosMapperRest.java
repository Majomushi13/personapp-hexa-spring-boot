package co.edu.javeriana.as.personapp.mapper;

import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.model.request.EstudiosRequest;
import co.edu.javeriana.as.personapp.model.response.EstudiosResponse;
import co.edu.javeriana.as.personapp.common.annotations.Mapper;

@Mapper
@Component
public class EstudiosMapperRest {

    public EstudiosResponse fromDomainToAdapterRestMaria(Study study) {
        return fromDomainToAdapterRest(study, "MariaDB");
    }

    public EstudiosResponse fromDomainToAdapterRestMongo(Study study) {
        return fromDomainToAdapterRest(study, "MongoDB");
    }

    private EstudiosResponse fromDomainToAdapterRest(Study study, String database) {
        return new EstudiosResponse(
                study.getPerson().getIdentification(), 
                study.getProfession().getIdentification(), 
                study.getGraduationDate(),
                study.getUniversityName(),
                "OK", 
                database);
    }

    public Study fromAdapterToDomain(EstudiosRequest request, Person person, Profession profession) {
        return new Study(
                person,
                profession,
                request.getGraduationDate(),
                request.getUniversityName()
        );
    }
}
