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
        TelefonoEntity telefonoEntity = new TelefonoEntity();
        telefonoEntity.setNum(phone.getNumber());
        telefonoEntity.setOper(phone.getCompany());
        if (owner != null && owner.getIdentification() != null) {
            PersonaEntity duenioEntity = new PersonaEntity();
            duenioEntity.setCc(owner.getIdentification());
            telefonoEntity.setDuenio(duenioEntity);
        } else {
            throw new IllegalStateException("El dueño es requerido y no puede ser nulo");
        }
        return telefonoEntity;
    }    

    public Phone fromAdapterToDomain(TelefonoEntity telefonoEntity) {
        Phone phone = new Phone();
        phone.setNumber(telefonoEntity.getNum());
        phone.setCompany(telefonoEntity.getOper());
        if (telefonoEntity.getDuenio() != null) {
            Person owner = new Person();
            owner.setIdentification(telefonoEntity.getDuenio().getCc());
            owner.setFirstName(telefonoEntity.getDuenio().getNombre());
            owner.setLastName(telefonoEntity.getDuenio().getApellido());
            phone.setOwner(owner);
        } else {
            phone.setOwner(new Person()); 
        }
        return phone;
    }
    
}
