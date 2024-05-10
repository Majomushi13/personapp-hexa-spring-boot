package co.edu.javeriana.as.personapp.mariadb.mapper;

import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;

@Component
public class TelefonoMapperMaria {

    public TelefonoEntity fromDomainToAdapter(Phone phone) {
        TelefonoEntity telefonoEntity = new TelefonoEntity();
        telefonoEntity.setNum(phone.getNumber());
        telefonoEntity.setOper(phone.getCompany());
        return telefonoEntity;
    }

    public TelefonoEntity fromDomainToAdapter(Phone phone, Person owner) {
        TelefonoEntity telefonoEntity = fromDomainToAdapter(phone); // Reuse the single argument method
        if (owner != null) {
            PersonaEntity duenioEntity = new PersonaEntity();
            duenioEntity.setCc(owner.getIdentification());
            telefonoEntity.setDuenio(duenioEntity);
        }
        return telefonoEntity;
    }

    public Phone fromAdapterToDomain(TelefonoEntity telefonoEntity) {
        Phone phone = new Phone();
        phone.setNumber(telefonoEntity.getNum());
        phone.setCompany(telefonoEntity.getOper());
        // Set owner if necessary
        return phone;
    }
}
