package co.edu.javeriana.as.personapp.mariadb.mapper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;

@Mapper
@Component
public class EstudiosMapperMaria {

    @Autowired
    private ProfesionMapperMaria profesionMapperMaria;

    @Autowired
    private PersonaMapperMaria personaMapperMaria;

    public EstudiosEntity fromDomainToAdapter(Study study) {
        EstudiosEntity estudio = new EstudiosEntity();
        EstudiosEntityPK estudioPK = new EstudiosEntityPK();
        
        if (study.getPerson() != null && study.getProfession() != null) {
            estudioPK.setCcPer(study.getPerson().getIdentification());
            estudioPK.setIdProf(study.getProfession().getIdentification());
            
            // Map the profession and person entities
            ProfesionEntity profesionEntity = profesionMapperMaria.fromDomainToAdapter(study.getProfession());
            PersonaEntity personaEntity = personaMapperMaria.fromDomainToAdapter(study.getPerson());

            estudio.setProfesion(profesionEntity);
            estudio.setPersona(personaEntity);
        } else {
            throw new RuntimeException("Person or Profession data is missing or incorrect");
        }

        estudio.setEstudiosPK(estudioPK);
        estudio.setFecha(validateFecha(study.getGraduationDate()));
        estudio.setUniver(validateUniver(study.getUniversityName()));

        return estudio;
    }

    private Date validateFecha(LocalDate graduationDate) {
        return graduationDate != null
                ? Date.from(graduationDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                : null;
    }

    private String validateUniver(String universityName) {
        return universityName != null ? universityName : "";
    }

    public Study fromAdapterToDomain(EstudiosEntity estudiosEntity) {
        if (estudiosEntity == null || estudiosEntity.getProfesion() == null || estudiosEntity.getPersona() == null) {
            throw new RuntimeException("Study, Profession or Person entity is missing from the database.");
        }

        Study study = new Study();
        study.setProfession(profesionMapperMaria.fromAdapterToDomain(estudiosEntity.getProfesion()));
        study.setPerson(personaMapperMaria.fromAdapterToDomain(estudiosEntity.getPersona()));
        study.setGraduationDate(validateGraduationDate(estudiosEntity.getFecha()));
        study.setUniversityName(validateUniversityName(estudiosEntity.getUniver()));
        return study;
    }

    private LocalDate validateGraduationDate(Date fecha) {
        return fecha != null ? Instant.ofEpochMilli(fecha.getTime()).atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }

    private String validateUniversityName(String univer) {
        return univer != null ? univer : "";
    }
}
