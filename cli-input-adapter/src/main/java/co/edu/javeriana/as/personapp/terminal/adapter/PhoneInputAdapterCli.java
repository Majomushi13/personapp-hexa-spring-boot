package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Scanner;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.mapper.PhoneMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.PhoneModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PhoneInputAdapterCli {

    @Autowired
    @Qualifier("phoneOutputAdapterMaria")
    private PhoneOutputPort phoneOutputPortMaria;

    @Autowired
    @Qualifier("phoneOutputAdapterMongo")
    private PhoneOutputPort phoneOutputPortMongo;

    @Autowired
    private PhoneMapperCli phoneMapperCli;

    @Autowired
    private PersonInputPort personInputPort;


    PhoneInputPort phoneInputPort;

    public void setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void listPhones() {
        log.info("Listing all phones in Input Adapter");
        List<PhoneModelCli> phones = phoneInputPort.findAll().stream()
                .map(phoneMapperCli::fromDomainToAdapterCli)
                .collect(Collectors.toList());
        phones.forEach(p -> System.out.println(p.toString()));
    }
    public void addPhone(Scanner keyboard) {
        try {
            System.out.print("Ingrese el número del teléfono: ");
            String number = keyboard.next();
            System.out.print("Ingrese la compañía: ");
            String company = keyboard.next();
            System.out.print("Ingrese la cédula del dueño del teléfono: ");
            Integer ownerId = keyboard.nextInt();
    
            Person owner = personInputPort.findOne(ownerId);  
            if (owner == null) {
                System.out.println("El dueño con la cédula " + ownerId + " no existe.");
                return;
            }
            Phone phone = new Phone(number, company, owner);
            phoneInputPort.create(phone);
            System.out.println("Teléfono agregado con éxito!");
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }    

    public void updatePhone(Scanner keyboard) {
        try {
            System.out.print("Ingrese el número del teléfono que desea actualizar: ");
            String number = keyboard.next();
            System.out.print("Ingrese la nueva compañía: ");
            String newCompany = keyboard.next();
            System.out.print("Ingrese la cédula del dueño del teléfono: ");
            Integer ownerId = keyboard.nextInt();
    
            Person owner = personInputPort.findOne(ownerId); // Recuperar el dueño
            if (owner == null) {
                System.out.println("No se encontró el dueño con ID: " + ownerId);
                return;
            }
    
            Phone phone = phoneInputPort.findByNumber(number);
            if (phone == null) {
                System.out.println("No se encontró el teléfono con número: " + number);
                return;
            }
    
            phone.setCompany(newCompany);
            phone.setOwner(owner); // Establecer el dueño
            phoneInputPort.edit(number, phone);
            System.out.println("Teléfono actualizado con éxito!");
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
        
    public void deletePhone(Scanner keyboard) {
        try {
            System.out.print("Ingrese el número del teléfono que desea eliminar: ");
            String number = keyboard.next();
            phoneInputPort.delete(number);  
            System.out.println("Teléfono eliminado con éxito!");
        } catch (NoExistException e) {
            System.out.println("Error al eliminar el teléfono: " + e.getMessage());
        }
    }
    
}
