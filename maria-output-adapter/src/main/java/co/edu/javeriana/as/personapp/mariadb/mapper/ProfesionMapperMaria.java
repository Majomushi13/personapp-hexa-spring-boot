// ProfesionMapperMaria.java
package co.edu.javeriana.as.personapp.mariadb.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;

@Mapper
public class ProfesionMapperMaria {

    @Autowired
    private EstudiosMapperMaria estudiosMapperMaria;

    public ProfesionEntity fromDomainToAdapter(Profession profession) {
        ProfesionEntity profesionEntity = new ProfesionEntity();
        profesionEntity.setId(profession.getIdentification());
        profesionEntity.setNom(profession.getName());
        profesionEntity.setDes(validateDes(profession.getDescription()));
        // Remove the call to validateEstudios here
        return profesionEntity;
    }

    private String validateDes(String description) {
        return description != null ? description : "";
    }

    public Profession fromAdapterToDomain(ProfesionEntity profesionEntity) {
        if (profesionEntity == null) {
            return null; // O manejar de otra manera según el diseño de tu aplicación
        }
        Profession profession = new Profession();
        profession.setIdentification(profesionEntity.getId());
        profession.setName(profesionEntity.getNom());
        profession.setDescription(validateDescription(profesionEntity.getDes()));
        return profession;
    }
    

    private String validateDescription(String des) {
        return des != null ? des : "";
    }
}
