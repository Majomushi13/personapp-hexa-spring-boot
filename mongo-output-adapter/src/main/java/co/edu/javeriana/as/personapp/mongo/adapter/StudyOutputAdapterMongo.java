package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.StudyRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("studyOutputAdapterMongo")
public class StudyOutputAdapterMongo implements StudyOutputPort {

    @Autowired
    private StudyRepositoryMongo studyRepositoryMongo;

    @Autowired
    private EstudiosMapperMongo estudiosMapperMongo;

    @Override
    public Study save(Study study) {
        log.debug("Into save on Adapter MongoDB");
        EstudiosDocument persistedEstudios = studyRepositoryMongo.save(estudiosMapperMongo.fromDomainToAdapter(study));
        return estudiosMapperMongo.fromAdapterToDomain(persistedEstudios);
    }

    @Override
    public Boolean delete(Integer idProf, Integer ccPer) {
        String id = idProf + "-" + ccPer;  // Asumiendo que el ID es una concatenación
        studyRepositoryMongo.deleteById(id);
        return studyRepositoryMongo.findById(id).isEmpty();
    }

    @Override
    public Study findById(Integer idProf, Integer ccPer) {
        String id = idProf + "-" + ccPer;  // Asumiendo que el ID es una concatenación
        return studyRepositoryMongo.findById(id)
            .map(estudiosMapperMongo::fromAdapterToDomain)
            .orElse(null);
    }

    @Override
    public List<Study> findAll() {
        return studyRepositoryMongo.findAll().stream()
            .map(estudiosMapperMongo::fromAdapterToDomain)
            .collect(Collectors.toList());
    }
}
