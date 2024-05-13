package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class StudyUseCase implements StudyInputPort {

    private StudyOutputPort studyPersistence;

    public StudyUseCase(@Qualifier("studyOutputAdapterMaria") StudyOutputPort studyPersistence) {
        this.studyPersistence = studyPersistence;
    }

    @Override
    public void setPersistence(StudyOutputPort studyPersistence) {
        this.studyPersistence = studyPersistence;
    }

    @Override
    public Study create(Study study) {
        log.debug("Creating study in Application Domain");
        return studyPersistence.save(study);
    }

    @Override
    public Study edit(Integer id, Study study) throws NoExistException {
        Study oldStudy = studyPersistence.findById(id);
        if (oldStudy != null)
            return studyPersistence.save(study);
        throw new NoExistException("The study with id " + id + " does not exist in the database, cannot be edited");
    }

    @Override
    public Boolean drop(Integer id) throws NoExistException {
        Study oldStudy = studyPersistence.findById(id);
        if (oldStudy != null)
            return studyPersistence.delete(id);
        throw new NoExistException("The study with id " + id + " does not exist in the database, cannot be dropped");
    }

    @Override
    public List<Study> findAll() {
        log.info("Fetching all studies from the database");
        return studyPersistence.findAll();
    }

    @Override
    public Study findOne(Integer id) throws NoExistException {
        Study study = studyPersistence.findById(id);
        if (study != null)
            return study;
        throw new NoExistException("The study with id " + id + " does not exist in the database, cannot be found");
    }

    @Override
    public Integer count() {
        return findAll().size();
    }
}
