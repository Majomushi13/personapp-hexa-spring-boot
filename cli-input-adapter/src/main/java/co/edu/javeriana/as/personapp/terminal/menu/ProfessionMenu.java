package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfessionInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProfessionMenu {

    private static final int OPCION_REGRESAR_MODULOS = 0;
    private static final int PERSISTENCIA_MARIADB = 1;
    private static final int PERSISTENCIA_MONGODB = 2;

    private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
    private static final int OPCION_VER_TODO = 1;
    private static final int OPCION_AGREGAR_PROFESION = 2;
    private static final int OPCION_ACTUALIZAR_PROFESION = 3;
    private static final int OPCION_ELIMINAR_PROFESION = 4;

    public void iniciarMenu(ProfessionInputAdapterCli professionInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            try {
                mostrarMenuMotorPersistencia();
                int opcion = leerOpcion(keyboard);
                switch (opcion) {
                case OPCION_REGRESAR_MODULOS:
                    isValid = true;
                    break;
                case PERSISTENCIA_MARIADB:
                    professionInputAdapterCli.setProfessionOutputPortInjection("MARIA");
                    menuOpciones(professionInputAdapterCli, keyboard);
                    break;
                case PERSISTENCIA_MONGODB:
                    professionInputAdapterCli.setProfessionOutputPortInjection("MONGO");
                    menuOpciones(professionInputAdapterCli, keyboard);
                    break;
                default:
                    log.warn("La opción elegida no es válida.");
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!isValid);
    }

    private void menuOpciones(ProfessionInputAdapterCli professionInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            mostrarMenuOpciones();
            int opcion = leerOpcion(keyboard);
            switch (opcion) {
            case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
                isValid = true;
                break;
            case OPCION_VER_TODO:
                professionInputAdapterCli.listProfessions();
                break;
            case OPCION_AGREGAR_PROFESION:
                professionInputAdapterCli.addProfession(keyboard);
                break;
            case OPCION_ACTUALIZAR_PROFESION:
                professionInputAdapterCli.updateProfession(keyboard);
                break;
            case OPCION_ELIMINAR_PROFESION:
                professionInputAdapterCli.deleteProfession(keyboard);
                break;
            default:
                log.warn("La opción elegida no es válida.");
            }
        } while (!isValid);
    }

    private void mostrarMenuOpciones() {
        System.out.println("----------------------");
        System.out.println(OPCION_VER_TODO + " - Ver todas las profesiones");
        System.out.println(OPCION_AGREGAR_PROFESION + " - Agregar una profesión");
        System.out.println(OPCION_ACTUALIZAR_PROFESION + " - Actualizar una profesión");
        System.out.println(OPCION_ELIMINAR_PROFESION + " - Eliminar una profesión");
        System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " - Regresar");
    }

    private void mostrarMenuMotorPersistencia() {
        System.out.println("----------------------");
        System.out.println(PERSISTENCIA_MARIADB + " - para MariaDB");
        System.out.println(PERSISTENCIA_MONGODB + " - para MongoDB");
        System.out.println(OPCION_REGRESAR_MODULOS + " - para regresar");
    }

    private int leerOpcion(Scanner keyboard) {
        try {
            System.out.print("Ingrese una opción: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Solo se permiten números.");
            keyboard.next(); 
            return leerOpcion(keyboard);
        }
    }
}
