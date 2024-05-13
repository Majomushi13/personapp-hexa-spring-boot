package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.mapper.EstudiosMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.StudyRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("studyOutputAdapterMaria")
@Transactional
public class StudyOutputAdapterMaria implements StudyOutputPort {

    @Autowired
    private StudyRepositoryMaria studyRepositoryMaria;

    @Autowired
    private EstudiosMapperMaria studyMapperMaria;

    @Override
    public Study save(Study study) {
        log.debug("Creating or updating study in MariaDB");
        EstudiosEntity studyEntity = studyMapperMaria.fromDomainToAdapter(study);
        EstudiosEntity savedEntity = studyRepositoryMaria.save(studyEntity);
        return studyMapperMaria.fromAdapterToDomain(savedEntity);
    }

    @Override
    public Boolean delete(Integer idProf, Integer ccPer) {
        log.debug("Deleting study from MariaDB");
        try {
            EstudiosEntityPK primaryKey = new EstudiosEntityPK(idProf, ccPer);
            studyRepositoryMaria.deleteById(primaryKey);
            return !studyRepositoryMaria.findById(primaryKey).isPresent();
        } catch (Exception e) {
            log.error("Error deleting study: ", e);
            return false;
        }
    }

    @Override
    public Study findById(Integer idProf, Integer ccPer) {
        log.debug("Finding study by ID in MariaDB");
        EstudiosEntityPK primaryKey = new EstudiosEntityPK(idProf, ccPer);
        return studyRepositoryMaria.findById(primaryKey)
            .map(studyMapperMaria::fromAdapterToDomain)
            .orElse(null);
    }

    @Override
    public List<Study> findAll() {
        log.debug("Finding all studies in MariaDB");
        return studyRepositoryMaria.findAll().stream()
            .map(studyMapperMaria::fromAdapterToDomain)
            .collect(Collectors.toList());
    }
}
