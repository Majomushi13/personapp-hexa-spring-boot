package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.PhoneInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PhoneMenu {

    private static final int OPCION_REGRESAR_MODULOS = 0;
    private static final int PERSISTENCIA_MARIADB = 1;
    private static final int PERSISTENCIA_MONGODB = 2;

    private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
    private static final int OPCION_VER_TODO = 1;
    private static final int OPCION_AGREGAR_TELEFONO = 2;
    private static final int OPCION_ELIMINAR_TELEFONO = 3;
    private static final int OPCION_ACTUALIZAR_TELEFONO = 4;
  
    public void iniciarMenu(PhoneInputAdapterCli phoneInputAdapterCli, Scanner keyboard) {
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
                    phoneInputAdapterCli.setPhoneOutputPortInjection("MARIA");
                    menuOpciones(phoneInputAdapterCli, keyboard);
                    break;
                case PERSISTENCIA_MONGODB:
                    phoneInputAdapterCli.setPhoneOutputPortInjection("MONGO");
                    menuOpciones(phoneInputAdapterCli, keyboard);
                    break;
                default:
                    log.warn("La opción elegida no es válida.");
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!isValid);
    }

    
    private void menuOpciones(PhoneInputAdapterCli phoneInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            mostrarMenuOpciones();
            int opcion = leerOpcion(keyboard);
            switch (opcion) {
                case OPCION_VER_TODO:
                    phoneInputAdapterCli.listPhones();
                    break;
                case OPCION_AGREGAR_TELEFONO:
                    phoneInputAdapterCli.addPhone(keyboard);
                    break;
                case OPCION_ELIMINAR_TELEFONO:
                    phoneInputAdapterCli.deletePhone(keyboard);
                    break;
                case OPCION_ACTUALIZAR_TELEFONO:
                    phoneInputAdapterCli.updatePhone(keyboard);
                    break;
                case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
                    isValid = true;
                    break;
                default:
                    log.warn("La opción elegida no es válida.");
            }
        } while (!isValid);
    }
    
    private void mostrarMenuOpciones() {
        System.out.println("----------------------");
        System.out.println(OPCION_VER_TODO + " - Ver todos los teléfonos");
        System.out.println(OPCION_AGREGAR_TELEFONO + " - Agregar un teléfono");
        System.out.println(OPCION_ELIMINAR_TELEFONO + " - Eliminar un teléfono");
        System.out.println(OPCION_ACTUALIZAR_TELEFONO + " - Actualizar un teléfono");
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
