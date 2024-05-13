package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDate;
import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.mapper.StudyMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.StudyModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class StudyInputAdapterCli {

    @Autowired
    @Qualifier("studyOutputAdapterMaria") 
    private StudyOutputPort studyOutputPortMaria;

    @Autowired
    @Qualifier("studyOutputAdapterMongo") 
    private StudyOutputPort studyOutputPortMongo;

    @Autowired
    private StudyMapperCli studyMapperCli;

    @Autowired
    private PersonInputPort personInputPort;

    @Autowired
    private ProfessionInputPort professionInputPort;

    private StudyInputPort studyInputPort;

    public void setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortMaria);
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortMongo);
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void listStudies() {
        log.info("Listing all studies in Input Adapter");
        List<StudyModelCli> studies = studyInputPort.findAll().stream()
                .map(studyMapperCli::fromDomainToAdapterCli)
                .collect(Collectors.toList());
        studies.forEach(s -> System.out.println(s.toString()));
    }

    public void addStudy(Scanner keyboard) {
        try {
            System.out.print("Ingrese el ID de la persona: ");
            Integer personId = keyboard.nextInt();
            System.out.print("Ingrese el ID de la profesión: ");
            Integer professionId = keyboard.nextInt();
            keyboard.nextLine();  // Consume the newline left-over
    
            // Attempt to fetch the Person and Profession
            Person person = personInputPort.findOne(personId);
            Profession profession = professionInputPort.findOne(professionId);
    
            if (person == null) {
                System.out.println("No se encontró la persona con el ID dado.");
                return;
            }
            if (profession == null) {
                System.out.println("No se encontró la profesión con el ID dado.");
                return;
            }
    
            System.out.print("Ingrese la fecha de graduación (YYYY-MM-DD): ");
            String dateString = keyboard.next();
            keyboard.nextLine();  // Consume any newline left over after the date input
            System.out.print("Ingrese el nombre de la universidad: ");
            String universityName = keyboard.nextLine();
    
            Study study = new Study(person, profession, LocalDate.parse(dateString), universityName);
            Study createdStudy = studyInputPort.create(study);
            System.out.println("Estudio creado con éxito: " + studyMapperCli.fromDomainToAdapterCli(createdStudy));
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error general: " + e.getMessage());
        }
    }
    
    

    public void updateStudy(Scanner keyboard) {
        try {
            System.out.print("Ingrese el ID de la profesión del estudio que desea actualizar: ");
            Integer professionId = keyboard.nextInt();
            System.out.print("Ingrese el ID de la persona del estudio que desea actualizar: ");
            Integer personId = keyboard.nextInt();
            keyboard.nextLine();  // Consumir el salto de línea restante

            Study study = studyInputPort.findOne(professionId, personId);
            System.out.print("Ingrese el nuevo nombre de la universidad: ");
            String universityName = keyboard.nextLine();
            study.setUniversityName(universityName);

            Study updatedStudy = studyInputPort.edit(professionId, personId, study);
            System.out.println("Estudio actualizado con éxito: " + studyMapperCli.fromDomainToAdapterCli(updatedStudy));
        } catch (NoExistException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteStudy(Scanner keyboard) {
        try {
            System.out.print("Ingrese el ID de la profesión del estudio que desea eliminar: ");
            Integer professionId = keyboard.nextInt();
            System.out.print("Ingrese el ID de la persona del estudio que desea eliminar: ");
            Integer personId = keyboard.nextInt();

            Boolean deleted = studyInputPort.drop(professionId, personId);
            if (deleted) {
                System.out.println("Estudio eliminado con éxito!");
            } else {
                System.out.println("No se pudo eliminar el estudio.");
            }
        } catch (NoExistException e) {
            System.out.println("Error al eliminar el estudio: " + e.getMessage());
        }
    }
}
