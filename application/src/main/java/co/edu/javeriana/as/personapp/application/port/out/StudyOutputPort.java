package co.edu.javeriana.as.personapp.application.port.out;

import java.util.List;
import co.edu.javeriana.as.personapp.domain.Study;

public interface StudyOutputPort {
    Study save(Study study);
    Boolean delete(Integer idProf, Integer ccPer);
    List<Study> findAll();
    Study findById(Integer idProf, Integer ccPer);
}
