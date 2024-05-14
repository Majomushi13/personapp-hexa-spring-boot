package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.mapper.ProfessionMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfessionModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfessionInputAdapterCli {

    @Autowired
    @Qualifier("professionOutputPortMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputPortMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private ProfessionMapperCli professionMapperCli;

    private ProfessionInputPort professionInputPort;

    public void setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void listProfessions() {
        log.info("Listing all professions in Input Adapter");
        List<ProfessionModelCli> professions = professionInputPort.findAll().stream()
                .map(professionMapperCli::fromDomainToAdapterCli)
                .collect(Collectors.toList());
        professions.forEach(p -> System.out.println(p.toString()));
    }

    public void addProfession(Scanner keyboard) {
        System.out.print("Ingrese el ID de la profesión: ");
        Integer id = keyboard.nextInt();
        System.out.print("Ingrese el nombre de la profesión: ");
        String name = keyboard.next();
        keyboard.nextLine();
        System.out.print("Ingrese la descripción de la profesión: ");
        String description = keyboard.nextLine();

        Profession profession = new Profession(id, name, description);
        Profession createdProfession = professionInputPort.create(profession);
        System.out.println("Profesión creada con éxito: " + professionMapperCli.fromDomainToAdapterCli(createdProfession));
    }

    public void updateProfession(Scanner keyboard) {
        try {
            System.out.print("Ingrese el ID de la profesión que desea actualizar: ");
            Integer id = keyboard.nextInt();
            keyboard.nextLine(); 
            System.out.print("Ingrese el nuevo nombre de la profesión: ");
            String name = keyboard.nextLine();
            System.out.print("Ingrese la nueva descripción de la profesión: ");
            String description = keyboard.nextLine();

            Profession profession = professionInputPort.findOne(id);
            profession.setName(name);
            profession.setDescription(description);

            Profession updatedProfession = professionInputPort.edit(id, profession);
            System.out.println("Profesión actualizada con éxito: " + professionMapperCli.fromDomainToAdapterCli(updatedProfession));
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteProfession(Scanner keyboard) {
        try {
            System.out.print("Ingrese el ID de la profesión que desea eliminar: ");
            Integer id = keyboard.nextInt();

            Boolean deleted = professionInputPort.drop(id);
            if (deleted) {
                System.out.println("Profesión eliminada con éxito!");
            } else {
                System.out.println("No se pudo eliminar la profesión.");
            }
        } catch (NoExistException e) {
            System.out.println("Error al eliminar la profesión: " + e.getMessage());
        }
    }
}
