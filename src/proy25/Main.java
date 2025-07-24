package proy25;

import java.util.Scanner;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SistemaGestionActividades sistema = new SistemaGestionActividades();
        
        int opcion;
        do {
            System.out.println("\n--- Menu Sistema de Gestion de Actividades Comunitarias (Consola) ---");
            System.out.println("1. Añadir actividad");
            System.out.println("2. Añadir persona (organizador o participante)");
            System.out.println("3. Inscribir en actividad");
            System.out.println("4. Ver actividades");
            System.out.println("5. Ver personas");
            System.out.println("6. Ver inscripciones");
            System.out.println("7. Cargar datos desde la base de datos");
            System.out.println("8. Actualizar base de datos");
            System.out.println("0. Salir");
            System.out.print("Elige una opcion: ");

            opcion = -1;
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opcion invalida. Ingresa un numero.");
                continue;
            }

            switch (opcion) {
                case 1:
                    anadirActividadConsola(sistema, scanner);
                    break;
                case 2:
                    anadirPersonaConsola(sistema, scanner);
                    break;
                case 3:
                    inscribirEnActividadConsola(sistema, scanner);
                    break;
                case 4:
                    sistema.listarTodasLasActividades();
                    break;
                case 5:
                    listarPersonas(sistema);
                    break;
                case 6:
                    sistema.listarTodasLasInscripciones();
                    break;
                case 7:
                    sistema.cargarDatosDesdeDB();
                    break;
                case 8:
                    sistema.actualizarDatosDB();
                    break;
                case 0:
                    System.out.println("Saliendo del programa. ¡Hasta luego!");
                    sistema.cerrarConexionDB();
                    break;
                default:
                    System.out.println("Opcion no reconocida. Por favor, intenta de nuevo.");
            }
        } while (opcion != 0);

        scanner.close();
    }

    public static void anadirActividadConsola(SistemaGestionActividades sistema, Scanner scanner) {
        System.out.println("\n--- Añadir Nueva Actividad ---");
        System.out.print("Tipo de actividad (Curso, Evento, Taller): ");
        String tipo = scanner.nextLine();

        System.out.print("Titulo: ");
        String titulo = scanner.nextLine();

        System.out.print("Descripcion: ");
        String descripcion = scanner.nextLine();

        System.out.print("Fecha de inicio (YYYY-MM-DD): ");
        String fechaInicioStr = scanner.nextLine();

        System.out.print("Fecha de fin (YYYY-MM-DD): ");
        String fechaFinStr = scanner.nextLine();

        System.out.print("Lugar: ");
        String lugar = scanner.nextLine();

        System.out.print("ID del Organizador: ");
        String organizadorId = scanner.nextLine();

        int cupoMaximo = 0;
        try {
            System.out.print("Cupo maximo: ");
            cupoMaximo = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Cupo maximo invalido. Por favor, ingresa un numero.");
            return;
        }

        Integer numSesiones = null;
        Boolean esAbiertoAlPublico = null;
        Boolean requiereMateriales = null;

        switch (tipo.toLowerCase()) {
            case "curso":
                try {
                    System.out.print("Numero de sesiones: ");
                    numSesiones = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Numero de sesiones invalido. Por favor, ingresa un numero.");
                    return;
                }
                break;
            case "evento":
                System.out.print("Es abierto al publico? (si/no): ");
                esAbiertoAlPublico = scanner.nextLine().equalsIgnoreCase("si");
                break;
            case "taller":
                System.out.print("Requiere materiales? (si/no): ");
                requiereMateriales = scanner.nextLine().equalsIgnoreCase("si");
                break;
            default:
                System.out.println("Tipo de actividad no reconocido.");
                return;
        }

        if (sistema.agregarActividad(tipo, titulo, descripcion, fechaInicioStr, fechaFinStr, lugar, organizadorId, cupoMaximo, numSesiones, esAbiertoAlPublico, requiereMateriales)) {
            System.out.println("Actividad añadida exitosamente.");
        } else {
            System.out.println("No se pudo añadir la actividad. Verifique los datos o el ID del organizador.");
        }
    }

    public static void anadirPersonaConsola(SistemaGestionActividades sistema, Scanner scanner) {
        System.out.println("\n--- Añadir Nueva Persona ---");
        System.out.print("Tipo de persona (organizador/participante): ");
        String tipo = scanner.nextLine();

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        if (tipo.equalsIgnoreCase("organizador")) {
            System.out.print("Institucion: ");
            String institucion = scanner.nextLine();
            sistema.agregarOrganizador(nombre, email, institucion);
            System.out.println("Organizador añadido.");
        } else if (tipo.equalsIgnoreCase("participante")) {
            int edad = 0;
            try {
                System.out.print("Edad: ");
                edad = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Edad invalida. Por favor, ingresa un numero.");
                return;
            }
            System.out.print("Genero: ");
            String genero = scanner.nextLine();
            sistema.agregarParticipante(nombre, email, edad, genero);
            System.out.println("Participante añadido.");
        } else {
            System.out.println("Tipo de persona no reconocido.");
        }
    }

    public static void inscribirEnActividadConsola(SistemaGestionActividades sistema, Scanner scanner) {
        System.out.println("\n--- Inscribir Participante en Actividad ---");
        System.out.print("ID del participante: ");
        String participanteId = scanner.nextLine();

        int actividadId = 0;
        try {
            System.out.print("ID de la actividad: ");
            actividadId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID de actividad invalido. Por favor, ingresa un numero.");
            return;
        }

        if (sistema.inscribirParticipanteEnActividad(participanteId, actividadId)) {
            System.out.println("Inscripcion realizada exitosamente.");
        } else {
            System.out.println("No se pudo realizar la inscripcion. Verifique los IDs o el cupo.");
        }
    }

    public static void listarPersonas(SistemaGestionActividades sistema) {
        System.out.println("\n--- Lista de todas las personas ---");
        ArrayList<Persona> personas = sistema.getPersonasRegistradas();
        if (personas.isEmpty()) {
            System.out.println("No hay personas registradas.");
            return;
        }
        for (Persona persona : personas) {
            persona.mostrar();
            System.out.println("--------------------");
        }
    }
}