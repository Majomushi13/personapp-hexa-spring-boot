package co.edu.javeriana.as.personapp.application.port.out;

import java.util.List;

import co.edu.javeriana.as.personapp.domain.Phone;

public interface PhoneOutputPort {

    Phone save(Phone phone);

    void delete(String number);

    List<Phone> findAll();

    Phone findByNumber(String number);

    Integer count();
}

