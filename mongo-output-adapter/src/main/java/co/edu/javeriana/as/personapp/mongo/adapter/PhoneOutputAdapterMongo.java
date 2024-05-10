package co.edu.javeriana.as.personapp.mongo.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.TelefonoMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.TelefonoRepositoryMongo;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Adapter("phoneOutputAdapterMongo")
public class PhoneOutputAdapterMongo implements PhoneOutputPort {

    @Autowired
    private TelefonoRepositoryMongo telefonoRepositoryMongo;

    @Autowired
    private TelefonoMapperMongo telefonoMapperMongo;

    @Override
    public Phone save(Phone phone) {
        TelefonoDocument telefonoDocument = telefonoMapperMongo.fromDomainToAdapter(phone);
        telefonoDocument = telefonoRepositoryMongo.save(telefonoDocument);
        return telefonoMapperMongo.fromAdapterToDomain(telefonoDocument);
    }

    @Override
    public void delete(String number) {
        telefonoRepositoryMongo.deleteById(number);
    }

    @Override
    public List<Phone> findAll() {
        return telefonoRepositoryMongo.findAll().stream()
                .map(telefonoMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Phone findByNumber(String number) {
        return telefonoRepositoryMongo.findById(number)
                .map(telefonoMapperMongo::fromAdapterToDomain)
                .orElse(null);
    }

    @Override
    public Integer count() {
        return (int) telefonoRepositoryMongo.count();
    }
}
