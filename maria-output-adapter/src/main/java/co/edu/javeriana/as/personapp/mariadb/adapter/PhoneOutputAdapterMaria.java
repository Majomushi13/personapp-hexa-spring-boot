package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.TelefonoRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("phoneOutputAdapterMaria")
@Transactional
public class PhoneOutputAdapterMaria implements PhoneOutputPort {

    @Autowired
    private TelefonoRepositoryMaria telefonoRepositoryMaria;

    @Autowired
    private TelefonoMapperMaria telefonoMapperMaria;

    @Override
    public Phone save(Phone phone) {
        log.debug("Into save on Adapter MariaDB");
        TelefonoEntity persistedTelefono = telefonoRepositoryMaria.save(telefonoMapperMaria.fromDomainToAdapter(phone));
        return telefonoMapperMaria.fromAdapterToDomain(persistedTelefono);
    }

    @Override
    public void delete(String number) {
        log.debug("Into delete on Adapter MariaDB");
        telefonoRepositoryMaria.deleteById(number);
    }

    @Override
    public List<Phone> findAll() {
        log.debug("Into find all on Adapter MariaDB");
        return telefonoRepositoryMaria.findAll().stream()
            .map(telefonoMapperMaria::fromAdapterToDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Phone findByNumber(String number) {
        log.debug("Into findByNumber on Adapter MariaDB");
        return telefonoRepositoryMaria.findById(number)
            .map(telefonoMapperMaria::fromAdapterToDomain)
            .orElse(null);
    }

    @Override
    public Integer count() {
        log.debug("Counting phones in Adapter MariaDB");
        return (int) telefonoRepositoryMaria.count();
    }
    
}
