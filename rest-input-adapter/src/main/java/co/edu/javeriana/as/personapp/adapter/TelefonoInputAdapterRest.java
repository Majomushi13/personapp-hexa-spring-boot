package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mapper.TelefonoMapperRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterRest {

    @Autowired
    @Qualifier("phoneOutputAdapterMaria")
    private PhoneOutputPort phoneOutputPortMaria;

    @Autowired
    @Qualifier("phoneOutputAdapterMongo")
    private PhoneOutputPort phoneOutputPortMongo;

    @Autowired
    private TelefonoMapperRest telefonoMapperRest;

    PhoneInputPort phoneInputPort;

    private String setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
        switch (dbOption.toUpperCase()) {
            case "MARIA":
            case "MARIADB":
                phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
                return DatabaseOption.MARIA.toString();
            case "MONGO":
            case "MONGODB":
                phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
                return DatabaseOption.MONGO.toString();
            default:
                throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }
    

    public List<TelefonoResponse> historial(String database) {
        log.info("Fetching phone history in Input Adapter");
        try {
            if (setPhoneOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return phoneInputPort.findAll().stream()
                        .map(telefonoMapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            } else {
                return phoneInputPort.findAll().stream()
                        .map(telefonoMapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }

        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new ArrayList<>();
        }
    }

    public TelefonoResponse crearTelefono(TelefonoRequest request) {
        try {
            setPhoneOutputPortInjection(request.getDatabase());
            Phone phone = phoneInputPort.create(telefonoMapperRest.fromAdapterToDomain(request));
            return telefonoMapperRest.fromDomainToAdapterRestMaria(phone);
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    public TelefonoResponse actualizarTelefono(String number, TelefonoRequest request) throws InvalidOptionException, NoExistException {
        log.info("Updating phone with number {} in database: {}", number, request.getDatabase());
        setPhoneOutputPortInjection(request.getDatabase());
        Phone phone = telefonoMapperRest.fromAdapterToDomain(request);
        Phone updatedPhone = phoneInputPort.edit(number, phone);
        return telefonoMapperRest.fromDomainToAdapterRestMaria(updatedPhone);
    }

    public void eliminarTelefono(String number, String database) throws InvalidOptionException, NoExistException {
        log.info("Deleting phone with number {} from database: {}", number, database);
        setPhoneOutputPortInjection(database);
        phoneInputPort.delete(number);
    }
}
