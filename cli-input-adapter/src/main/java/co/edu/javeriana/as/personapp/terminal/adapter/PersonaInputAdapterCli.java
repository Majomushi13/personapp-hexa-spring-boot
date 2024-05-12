package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Scanner;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.mapper.PersonaMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterCli {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperCli personaMapperCli;

	PersonInputPort personInputPort;

	public void setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial1() {
		log.info("Into historial PersonaEntity in Input Adapter");
		List<PersonaModelCli> persona = personInputPort.findAll().stream().map(personaMapperCli::fromDomainToAdapterCli)
					.collect(Collectors.toList());
		persona.forEach(p -> System.out.println(p.toString()));
	}
	public void historial() {
	    log.info("Into historial PersonaEntity in Input Adapter");
	    personInputPort.findAll().stream()
	        .map(personaMapperCli::fromDomainToAdapterCli)
	        .forEach(System.out::println);
	}

	
    public void addPerson(Scanner keyboard) {
        System.out.print("Ingrese la identificación: ");
        Integer identification = keyboard.nextInt();
        System.out.print("Ingrese el primer nombre: ");
        String firstName = keyboard.next();
        System.out.print("Ingrese el apellido: ");
        String lastName = keyboard.next();
        System.out.print("Ingrese el género (MALE, FEMALE, OTHER): ");
        String genderInput = keyboard.next();
        Gender gender;
        try {
            gender = Gender.valueOf(genderInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Género inválido. Elija entre MALE, FEMALE, OTHER.");
            return; 
        }
        System.out.print("Ingrese la edad: ");
        Integer age = keyboard.nextInt();
    
        Person person = new Person(identification, firstName, lastName, gender, age);
        Person createdPerson = personInputPort.create(person);
        System.out.println("Persona creada con éxito: " + personaMapperCli.fromDomainToAdapterCli(createdPerson));
    }
    

    public void updatePerson(Scanner keyboard) {
        try {
            System.out.print("Ingrese la identificación de la persona que desea actualizar: ");
            Integer identification = keyboard.nextInt();

            Person existingPerson = personInputPort.findOne(identification);
            System.out.print("Ingrese el nuevo primer nombre: ");
            existingPerson.setFirstName(keyboard.next());
            System.out.print("Ingrese el nuevo apellido: ");
            existingPerson.setLastName(keyboard.next());
            System.out.print("Ingrese el nuevo género (MALE, FEMALE, OTHER): ");
            existingPerson.setGender(Gender.valueOf(keyboard.next().toUpperCase()));
            System.out.print("Ingrese la nueva edad: ");
            existingPerson.setAge(keyboard.nextInt());

            Person updatedPerson = personInputPort.edit(identification, existingPerson);
            System.out.println("Persona actualizada con éxito: " + personaMapperCli.fromDomainToAdapterCli(updatedPerson));
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deletePerson(Scanner keyboard) {
        try {
            System.out.print("Ingrese la identificación de la persona que desea eliminar: ");
            Integer identification = keyboard.nextInt();

            Boolean deleted = personInputPort.drop(identification);
            if (deleted) {
                System.out.println("Persona eliminada con éxito!");
            } else {
                System.out.println("No se pudo eliminar la persona.");
            }
        } catch (NoExistException e) {
            System.out.println("Error al eliminar la persona: " + e.getMessage());
        }
    }

}
