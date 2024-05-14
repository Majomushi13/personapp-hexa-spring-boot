package co.edu.javeriana.as.personapp.mariadb.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import lombok.NonNull;

@Component
public class PersonaMapperMaria {

    @Autowired
    private EstudiosMapperMaria estudiosMapperMaria;

    @Autowired
    private TelefonoMapperMaria telefonoMapperMaria;

    public PersonaEntity fromDomainToAdapter(Person person) {
        PersonaEntity personaEntity = new PersonaEntity();
        personaEntity.setCc(person.getIdentification());
        personaEntity.setNombre(person.getFirstName());
        personaEntity.setApellido(person.getLastName());
        personaEntity.setGenero(validateGenero(person.getGender()));
        personaEntity.setEdad(validateEdad(person.getAge()));
        personaEntity.setEstudios(validateEstudios(person.getStudies())); 
        personaEntity.setTelefonos(validateTelefonos(person.getPhoneNumbers(), person));
        return personaEntity;
    }

    private Character validateGenero(@NonNull Gender gender) {
        return gender == Gender.FEMALE ? 'F' : gender == Gender.MALE ? 'M' : ' ';
    }

    private Integer validateEdad(Integer age) {
        return age != null && age >= 0 ? age : null;
    }

    private List<EstudiosEntity> validateEstudios(List<Study> studies) {
        return studies != null && !studies.isEmpty()
                ? studies.stream().map(study -> estudiosMapperMaria.fromDomainToAdapter(study)).collect(Collectors.toList())
                : new ArrayList<>();
    }

    private List<TelefonoEntity> validateTelefonos(List<Phone> phoneNumbers, Person owner) {
        return phoneNumbers != null && !phoneNumbers.isEmpty() ? phoneNumbers.stream()
                .map(phone -> telefonoMapperMaria.fromDomainToAdapter(phone, owner)).collect(Collectors.toList())
                : new ArrayList<>();
    }

    public Person fromAdapterToDomain(PersonaEntity personaEntity) {
        Person person = new Person();
        person.setIdentification(personaEntity.getCc());
        person.setFirstName(personaEntity.getNombre());
        person.setLastName(personaEntity.getApellido());
        person.setGender(validateGender(personaEntity.getGenero()));
        person.setAge(validateAge(personaEntity.getEdad()));
        person.setStudies(validateStudies(personaEntity.getEstudios())); 
        person.setPhoneNumbers(validatePhones(personaEntity.getTelefonos(), person));
        return person;
    }

    private @NonNull Gender validateGender(Character genero) {
        return genero == 'F' ? Gender.FEMALE : genero == 'M' ? Gender.MALE : Gender.OTHER;
    }

    private Integer validateAge(Integer edad) {
        return edad != null && edad >= 0 ? edad : null;
    }

    private List<Study> validateStudies(List<EstudiosEntity> estudiosEntity) {
        List<Study> studies = new ArrayList<>();
        if (estudiosEntity != null && !estudiosEntity.isEmpty()) {
            for (EstudiosEntity estudio : estudiosEntity) {
                Study study = new Study();
                studies.add(study);
            }
        }
        return studies;
    }

    private List<Phone> validatePhones(List<TelefonoEntity> telefonoEntities, Person owner) {
        return telefonoEntities != null && !telefonoEntities.isEmpty() ? telefonoEntities.stream()
                .map(telefono -> new Phone(telefono.getNum(), telefono.getOper(), owner)).collect(Collectors.toList())
                : new ArrayList<>();
    }
}
