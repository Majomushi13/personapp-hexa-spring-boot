package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.ProfesionMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.ProfessionRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Qualifier("professionOutputPortMaria")
@Adapter("professionOutputAdapterMaria")
@Transactional
public class ProfessionOutputAdapterMaria implements ProfessionOutputPort {

    @Autowired
    private ProfessionRepositoryMaria professionRepositoryMaria;

    @Autowired
    private ProfesionMapperMaria professionMapperMaria;

    @Override
    public Profession save(Profession profession) {
        log.debug("Saving profession to MariaDB");
        ProfesionEntity professionEntity = professionMapperMaria.fromDomainToAdapter(profession);
        ProfesionEntity savedEntity = professionRepositoryMaria.save(professionEntity);
        return professionMapperMaria.fromAdapterToDomain(savedEntity);
    }

    @Override
    public Boolean delete(Integer id) {
        log.debug("Deleting profession from MariaDB");
        try {
            professionRepositoryMaria.deleteById(id);
            return !professionRepositoryMaria.findById(id).isPresent();
        } catch (Exception e) {
            log.error("Error deleting profession: ", e);
            return false;
        }
    }

    @Override
    public List<Profession> findAll() {
        log.debug("Finding all professions in MariaDB");
        return professionRepositoryMaria.findAll().stream()
            .map(professionMapperMaria::fromAdapterToDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Profession findById(Integer id) {
        log.debug("Finding profession by ID in MariaDB");
        return professionRepositoryMaria.findById(id)
            .map(professionMapperMaria::fromAdapterToDomain)
            .orElse(null);
    }
}
