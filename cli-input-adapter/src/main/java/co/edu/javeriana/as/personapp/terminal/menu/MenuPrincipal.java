package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.adapter.PhoneInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfessionInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MenuPrincipal {
    
    @Autowired
    private PersonaInputAdapterCli personaInputAdapterCli;

    @Autowired
    private PhoneInputAdapterCli phoneInputAdapterCli;

    @Autowired
    private ProfessionInputAdapterCli professionInputAdapterCli;  // Agregar la inyección del adaptador de profesiones

    private static final int SALIR = 0;
    private static final int MODULO_PERSONA = 1;
    private static final int MODULO_PROFESION = 2;
    private static final int MODULO_TELEFONO = 3;
    private static final int MODULO_ESTUDIO = 4;

    private final PersonaMenu personaMenu;
    private final PhoneMenu phoneMenu;
    private final ProfessionMenu professionMenu;  // Crear una instancia del menú de profesiones
    private final Scanner keyboard;

    public MenuPrincipal() {
        this.personaMenu = new PersonaMenu();
        this.phoneMenu = new PhoneMenu();
        this.professionMenu = new ProfessionMenu();  // Inicializar el menú de profesiones
        this.keyboard = new Scanner(System.in);
    }

    public void inicio() {
        boolean isValid = false;
        do {
            mostrarMenu();
            int opcion = leerOpcion();
            switch (opcion) {
            case SALIR:
                isValid = true;
                break;
            case MODULO_PERSONA:
                personaMenu.iniciarMenu(personaInputAdapterCli, keyboard);
                log.info("Returned to Main Menu from Person Module");
                break;
            case MODULO_TELEFONO:
                phoneMenu.iniciarMenu(phoneInputAdapterCli, keyboard);
                log.info("Returned to Main Menu from Phone Module");
                break;
            case MODULO_PROFESION:
                professionMenu.iniciarMenu(professionInputAdapterCli, keyboard);
                log.info("Returned to Main Menu from Profession Module");
                break;
            case MODULO_ESTUDIO:
                log.warn("Implementation pending for this module");
                break;
            default:
                log.warn("The selected option is not valid.");
            }
        } while (!isValid);
        keyboard.close();
    }

    private void mostrarMenu() {
        System.out.println("----------------------");
        System.out.println(MODULO_PERSONA + " to work with the Person Module");
        System.out.println(MODULO_PROFESION + " to work with the Profession Module");
        System.out.println(MODULO_TELEFONO + " to work with the Phone Module");
        System.out.println(MODULO_ESTUDIO + " to work with the Study Module");
        System.out.println(SALIR + " to Exit");
    }

    private int leerOpcion() {
        try {
            System.out.print("Enter an option: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Only numbers are allowed.");
            keyboard.next(); // clear scanner buffer
            return leerOpcion();
        }
    }
}
