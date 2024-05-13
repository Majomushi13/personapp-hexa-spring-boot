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
    public Boolean delete(Integer id) {
        log.debug("Into delete on Adapter MongoDB");
        studyRepositoryMongo.deleteById(String.valueOf(id));
        return studyRepositoryMongo.findById(String.valueOf(id)).isEmpty();
    }

    @Override
    public List<Study> findAll() {
        log.debug("Into find on Adapter MongoDB");
        return studyRepositoryMongo.findAll().stream()
                .map(estudiosMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Study findById(Integer id) {
        log.debug("Into findById on Adapter MongoDB");
        if (studyRepositoryMongo.findById(String.valueOf(id)).isEmpty()) {
            return null;
        } else {
            return estudiosMapperMongo.fromAdapterToDomain(studyRepositoryMongo.findById(String.valueOf(id)).get());
        }
    }
}
