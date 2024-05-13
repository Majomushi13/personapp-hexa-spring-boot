package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;

@Port
public interface StudyInputPort {

    void setPersistence(StudyOutputPort studyPersistence);

    Study create(Study study);

    Study edit(Integer idProf, Integer ccPer, Study study) throws NoExistException;

    Boolean drop(Integer idProf, Integer ccPer) throws NoExistException;

    List<Study> findAll();

    Study findOne(Integer idProf, Integer ccPer) throws NoExistException;

    Integer count();
}
