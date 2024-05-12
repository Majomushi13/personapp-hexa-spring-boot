package co.edu.javeriana.as.personapp.mongo.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.ProfesionMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.ProfessionRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Adapter("professionOutputAdapterMongo")
@Qualifier("professionOutputPortMongo")
public class ProfessionOutputAdapterMongo implements ProfessionOutputPort {

    @Autowired
    private ProfessionRepositoryMongo profesionRepositoryMongo;

    @Autowired
    private ProfesionMapperMongo profesionMapperMongo;

    @Override
    public Profession save(Profession profession) {
        ProfesionDocument document = profesionMapperMongo.fromDomainToAdapter(profession);
        ProfesionDocument savedDocument = profesionRepositoryMongo.save(document);
        return profesionMapperMongo.fromAdapterToDomain(savedDocument);
    }

    @Override
    public Boolean delete(Integer id) {
        if (profesionRepositoryMongo.existsById(id)) {
            profesionRepositoryMongo.deleteById(id);
            return !profesionRepositoryMongo.existsById(id);
        }
        return false;
    }

    @Override
    public List<Profession> findAll() {
        return profesionRepositoryMongo.findAll().stream()
                .map(profesionMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Profession findById(Integer id) {
        return profesionRepositoryMongo.findById(id)
                .map(profesionMapperMongo::fromAdapterToDomain)
                .orElse(null);
    }
}
