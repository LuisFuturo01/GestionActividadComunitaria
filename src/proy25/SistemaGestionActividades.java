package proy25;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SistemaGestionActividades {
    Scanner lectorEntrada = new Scanner(System.in);
    protected ArrayList<ActividadComunitaria> actividadesRegistradas;
    protected ArrayList<Persona> personasRegistradas;
    protected ArrayList<Inscripcion> inscripcionesRealizadas;

    private int contadorIdActividad;
    private int contadorIdOrganizador;
    private int contadorIdParticipante;
    private int contadorIdInscripcion;

    private Connection conexionDB;

    public SistemaGestionActividades() {
        this.actividadesRegistradas = new ArrayList<>();
        this.personasRegistradas = new ArrayList<>();
        this.inscripcionesRealizadas = new ArrayList<>();
        this.contadorIdActividad = 1;
        this.contadorIdOrganizador = 1;
        this.contadorIdParticipante = 1;
        this.contadorIdInscripcion = 1;
        this.conexionDB = ConectarBD();
    }

    public Connection ConectarBD(){
        Connection conexion = null;
        String host ="jdbc:mysql://localhost/";
        String user = "root";
        String pass = "";
        String db ="actividadescomunitarias_25";
        System.out.println("Conectando a la base de datos...");
        try{
            conexion = DriverManager.getConnection(host + db, user, pass);
            System.out.println("Conexion exitosa a la base de datos.");
        }catch(SQLException e){
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return conexion;
    }

    public void cerrarConexionDB() {
        if (conexionDB != null) {
            try {
                conexionDB.close();
                System.out.println("Conexion a la base de datos cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexion a la base de datos: " + e.getMessage());
            }
        }
    }

    public void cargarDatosDesdeDB() {
        System.out.println("\n--- Cargando datos desde la base de datos ---");
        cargarPersonasDesdeDB();
        System.out.println("DEBUG: Personas cargadas antes de actividades: " + personasRegistradas.size());
        cargarActividadesDesdeDB();
        System.out.println("DEBUG: Actividades cargadas antes de inscripciones: " + actividadesRegistradas.size());
        cargarInscripcionesDesdeDB();
        System.out.println("Carga de datos completada.");
    }

    private void cargarPersonasDesdeDB() {
        personasRegistradas.clear();
        String sql = "SELECT id_persona, nombre, email, tipo_persona, edad, genero, institucion FROM Personas";
        int maxOrgId = 0;
        int maxPartId = 0;

        try (Statement stmt = conexionDB.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String idPersona = rs.getString("id_persona");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String tipoPersona = rs.getString("tipo_persona");

                if ("organizador".equalsIgnoreCase(tipoPersona)) {
                    String institucion = rs.getString("institucion");
                    Organizador organizador = new Organizador(idPersona, nombre, email, institucion);
                    personasRegistradas.add(organizador);
                    try {
                        int num = Integer.parseInt(idPersona.substring(4));
                        if (num > maxOrgId) {
                            maxOrgId = num;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing organizer ID for counter update: " + idPersona + " - " + e.getMessage());
                    }
                } else if ("participante".equalsIgnoreCase(tipoPersona)) {
                    int edad = rs.getInt("edad");
                    String genero = rs.getString("genero");
                    Participante participante = new Participante(idPersona, nombre, email, edad, genero);
                    personasRegistradas.add(participante);
                    try {
                        int num = Integer.parseInt(idPersona.substring(5));
                        if (num > maxPartId) {
                            maxPartId = num;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing participant ID for counter update: " + idPersona + " - " + e.getMessage());
                    }
                }
            }
            contadorIdOrganizador = maxOrgId + 1;
            contadorIdParticipante = maxPartId + 1;

            System.out.println("Personas cargadas: " + personasRegistradas.size());
        } catch (SQLException e) {
            System.err.println("Error al cargar personas desde la DB: " + e.getMessage());
        }
    }

    private void cargarActividadesDesdeDB() {
        actividadesRegistradas.clear();
        String sql = "SELECT id_actividad, titulo, descripcion, fecha_inicio, fecha_fin, cupo_maximo, lugar, id_organizador, tipo_actividad, num_sesiones, es_abierto_al_publico, requiere_materiales FROM ActividadesComunitarias";
        int maxActividadId = 0;

        try (Statement stmt = conexionDB.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int idActividad = rs.getInt("id_actividad");
                String titulo = rs.getString("titulo");
                String descripcion = rs.getString("descripcion");
                Date fechaInicio = convertSqlDateToUtilDate(rs.getDate("fecha_inicio"));
                Date fechaFin = convertSqlDateToUtilDate(rs.getDate("fecha_fin"));
                int cupoMaximo = rs.getInt("cupo_maximo");
                String lugar = rs.getString("lugar");
                String idOrganizador = rs.getString("id_organizador");
                String tipoActividad = rs.getString("tipo_actividad");

                Organizador organizador = null;
                for (Persona p : personasRegistradas) {
                    if (p instanceof Organizador && p.getIdPersona().equals(idOrganizador)) {
                        organizador = (Organizador) p;
                        break;
                    }
                }

                if (organizador == null) {
                    System.err.println("Organizador con ID " + idOrganizador + " no encontrado para la actividad " + titulo);
                    continue;
                }

                ActividadComunitaria actividad = null;
                switch (tipoActividad.toLowerCase()) {
                    case "curso":
                        int numSesiones = rs.getInt("num_sesiones");
                        actividad = new Curso(idActividad, titulo, descripcion, fechaInicio, fechaFin, lugar, organizador, cupoMaximo, numSesiones);
                        break;
                    case "evento":
                        Boolean esAbiertoAlPublico = rs.getObject("es_abierto_al_publico", Boolean.class);
                        actividad = new Evento(idActividad, titulo, descripcion, fechaInicio, fechaFin, lugar, organizador, cupoMaximo, esAbiertoAlPublico);
                        break;
                    case "taller":
                        Boolean requiereMaterialesDB = rs.getObject("requiere_materiales", Boolean.class);
                        actividad = new Taller(idActividad, titulo, descripcion, fechaInicio, fechaFin, lugar, organizador, cupoMaximo, requiereMaterialesDB);
                        break;
                }
                if (actividad != null) {
                    actividadesRegistradas.add(actividad);
                    if (idActividad > maxActividadId) {
                        maxActividadId = idActividad;
                    }
                }
            }
            contadorIdActividad = maxActividadId + 1;

            System.out.println("Actividades cargadas: " + actividadesRegistradas.size());
        } catch (SQLException e) {
            System.err.println("Error al cargar actividades desde la DB: " + e.getMessage());
        }
    }

    private void cargarInscripcionesDesdeDB() {
        inscripcionesRealizadas.clear();
        String sql = "SELECT id_inscripcion, id_participante, id_actividad, fecha_inscripcion FROM Inscripciones";
        int maxInscripcionNumId = 0;

        try (Statement stmt = conexionDB.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String idInscripcion = rs.getString("id_inscripcion");
                String idParticipante = rs.getString("id_participante");
                int idActividad = rs.getInt("id_actividad");
                Date fechaInscripcion = convertSqlTimestampToUtilDate(rs.getTimestamp("fecha_inscripcion")); 

                System.out.println("DEBUG: Procesando inscripcion ID: " + idInscripcion + 
                                   ", Participante ID: " + idParticipante + 
                                   ", Actividad ID: " + idActividad);

                Participante participante = null;
                for (Persona p : personasRegistradas) {
                    if (p instanceof Participante && p.getIdPersona().equals(idParticipante)) {
                        participante = (Participante) p;
                        break;
                    }
                }

                ActividadComunitaria actividad = null;
                for (ActividadComunitaria ac : actividadesRegistradas) {
                    if (ac.getIdActividad() == idActividad) {
                        actividad = ac;
                        break;
                    }
                }

                if (participante != null && actividad != null) {
                    Inscripcion<String> inscripcion = new Inscripcion<>(idInscripcion, participante, actividad, fechaInscripcion);
                    inscripcionesRealizadas.add(inscripcion);
                    
                    if (!actividad.getListaParticipantes().contains(participante)) {
                        actividad.getListaParticipantes().add(participante);
                    }
                    
                    try {
                        if (idInscripcion != null && idInscripcion.startsWith("INS-")) {
                            int num = Integer.parseInt(idInscripcion.substring(4));
                            if (num > maxInscripcionNumId) {
                                maxInscripcionNumId = num;
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing inscription ID for counter update: " + idInscripcion + " - " + e.getMessage());
                    }

                } else {
                    System.err.println("Participante o actividad no encontrada para la inscripcion ID: " + idInscripcion + 
                                       " (Participante encontrado: " + (participante != null) + 
                                       ", Actividad encontrada: " + (actividad != null) + ")");
                }
            }
            contadorIdInscripcion = maxInscripcionNumId + 1;

            System.out.println("Inscripciones cargadas: " + inscripcionesRealizadas.size());
        }
        catch (SQLException e) {
            System.err.println("Error al cargar inscripciones desde la DB: " + e.getMessage());
        }
    }

    public void actualizarDatosDB() {
        System.out.println("\n--- Actualizando base de datos ---");
        actualizarPersonasDB();
        actualizarActividadesDB();
        actualizarInscripcionesDB();
        System.out.println("Actualizacion de datos completada.");
    }

    private void actualizarPersonasDB() {
        String insertSql = "INSERT INTO Personas (id_persona, nombre, email, tipo_persona, edad, genero, institucion) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE nombre=?, email=?, tipo_persona=?, edad=?, genero=?, institucion=?";
        try (PreparedStatement pstmt = conexionDB.prepareStatement(insertSql)) {
            for (Persona persona : personasRegistradas) {
                pstmt.setString(1, persona.getIdPersona().toString());
                pstmt.setString(2, persona.getNombre());
                pstmt.setString(3, persona.getEmail());

                if (persona instanceof Organizador) {
                    Organizador org = (Organizador) persona;
                    pstmt.setString(4, "organizador");
                    pstmt.setNull(5, Types.INTEGER);
                    pstmt.setNull(6, Types.VARCHAR);
                    pstmt.setString(7, org.institucion);

                    pstmt.setString(8, org.getNombre());
                    pstmt.setString(9, org.getEmail());
                    pstmt.setString(10, "organizador");
                    pstmt.setNull(11, Types.INTEGER);
                    pstmt.setNull(12, Types.VARCHAR);
                    pstmt.setString(13, org.institucion);

                } else if (persona instanceof Participante) {
                    Participante part = (Participante) persona;
                    pstmt.setString(4, "participante");
                    pstmt.setInt(5, part.edad);
                    pstmt.setString(6, part.genero);
                    pstmt.setNull(7, Types.VARCHAR);

                    pstmt.setString(8, part.getNombre());
                    pstmt.setString(9, part.getEmail());
                    pstmt.setString(10, "participante");
                    pstmt.setInt(11, part.edad);
                    pstmt.setString(12, part.genero);
                    pstmt.setNull(13, Types.VARCHAR);
                }
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Personas actualizadas en la DB.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar personas en la DB: " + e.getMessage());
        }
    }

    private void actualizarActividadesDB() {
        String insertSql = "INSERT INTO actividadesComunitarias (id_actividad, titulo, descripcion, fecha_inicio, fecha_fin, cupo_maximo, lugar, id_organizador, tipo_actividad, num_sesiones, es_abierto_al_publico, requiere_materiales) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE titulo=?, descripcion=?, fecha_inicio=?, fecha_fin=?, cupo_maximo=?, lugar=?, id_organizador=?, tipo_actividad=?, num_sesiones=?, es_abierto_al_publico=?, requiere_materiales=?";
        try (PreparedStatement pstmt = conexionDB.prepareStatement(insertSql)) {
            for (ActividadComunitaria actividad : actividadesRegistradas) {
                pstmt.setInt(1, actividad.idActividad);
                pstmt.setString(2, actividad.titulo);
                pstmt.setString(3, actividad.descripcion);
                pstmt.setDate(4, convertUtilDateToSqlDate(actividad.fechaInicio));
                pstmt.setDate(5, convertUtilDateToSqlDate(actividad.fechaFin));
                pstmt.setInt(6, actividad.cupoMaximo);
                pstmt.setString(7, actividad.lugar);
                pstmt.setString(8, actividad.organizador != null ? actividad.organizador.getIdPersona().toString() : null);
                pstmt.setString(9, actividad.obtenerTipo().toLowerCase());

                if (actividad instanceof Curso) {
                    Curso curso = (Curso) actividad;
                    pstmt.setInt(10, curso.numSesiones);
                    pstmt.setNull(11, Types.BOOLEAN);
                    pstmt.setNull(12, Types.BOOLEAN);

                    pstmt.setString(13, curso.titulo);
                    pstmt.setString(14, curso.descripcion);
                    pstmt.setDate(15, convertUtilDateToSqlDate(curso.fechaInicio));
                    pstmt.setDate(16, convertUtilDateToSqlDate(curso.fechaFin));
                    pstmt.setInt(17, curso.cupoMaximo);
                    pstmt.setString(18, curso.lugar);
                    pstmt.setString(19, curso.organizador != null ? curso.organizador.getIdPersona().toString() : null);
                    pstmt.setString(20, "curso");
                    pstmt.setInt(21, curso.numSesiones);
                    pstmt.setNull(22, Types.BOOLEAN);
                    pstmt.setNull(23, Types.BOOLEAN);

                } else if (actividad instanceof Evento) {
                    Evento evento = (Evento) actividad;
                    pstmt.setNull(10, Types.INTEGER);
                    pstmt.setObject(11, evento.getEsAbiertoAlPublico(), Types.BOOLEAN);
                    pstmt.setNull(12, Types.BOOLEAN);

                    pstmt.setString(13, evento.titulo);
                    pstmt.setString(14, evento.descripcion);
                    pstmt.setDate(15, convertUtilDateToSqlDate(evento.fechaInicio));
                    pstmt.setDate(16, convertUtilDateToSqlDate(evento.fechaFin));
                    pstmt.setInt(17, evento.cupoMaximo);
                    pstmt.setString(18, evento.lugar);
                    pstmt.setString(19, evento.organizador != null ? evento.organizador.getIdPersona().toString() : null);
                    pstmt.setString(20, "evento");
                    pstmt.setNull(21, Types.INTEGER);
                    pstmt.setObject(22, evento.getEsAbiertoAlPublico(), Types.BOOLEAN);
                    pstmt.setNull(23, Types.BOOLEAN);

                } else if (actividad instanceof Taller) {
                    Taller taller = (Taller) actividad;
                    pstmt.setNull(10, Types.INTEGER);
                    pstmt.setNull(11, Types.BOOLEAN);
                    pstmt.setObject(12, taller.getRequiereMateriales(), Types.BOOLEAN);

                    pstmt.setString(13, taller.titulo);
                    pstmt.setString(14, taller.descripcion);
                    pstmt.setDate(15, convertUtilDateToSqlDate(taller.fechaInicio));
                    pstmt.setDate(16, convertUtilDateToSqlDate(taller.fechaFin));
                    pstmt.setInt(17, taller.cupoMaximo);
                    pstmt.setString(18, taller.lugar);
                    pstmt.setString(19, taller.organizador != null ? taller.organizador.getIdPersona().toString() : null);
                    pstmt.setString(20, "taller");
                    pstmt.setNull(21, Types.INTEGER);
                    pstmt.setNull(22, Types.BOOLEAN);
                    pstmt.setObject(23, taller.getRequiereMateriales(), Types.BOOLEAN);
                }
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Actividades actualizadas en la DB.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar actividades en la DB: " + e.getMessage());
        }
    }

    private void actualizarInscripcionesDB() {
        String insertSql = "INSERT INTO Inscripciones (id_inscripcion, id_participante, id_actividad, fecha_inscripcion) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE id_participante=?, id_actividad=?, fecha_inscripcion=?";
        try (PreparedStatement pstmt = conexionDB.prepareStatement(insertSql)) {
            for (Inscripcion inscripcion : inscripcionesRealizadas) {
                pstmt.setString(1, inscripcion.getIdInscripcion().toString());
                pstmt.setString(2, inscripcion.getParticipante().getIdPersona().toString());
                pstmt.setInt(3, inscripcion.getActividad().getIdActividad());
                pstmt.setTimestamp(4, convertUtilDateToSqlTimestamp(inscripcion.getFechaInscripcion()));

                pstmt.setString(5, inscripcion.getParticipante().getIdPersona().toString());
                pstmt.setInt(6, inscripcion.getActividad().getIdActividad());
                pstmt.setTimestamp(7, convertUtilDateToSqlTimestamp(inscripcion.getFechaInscripcion()));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Inscripciones actualizadas en la DB.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar inscripciones en la DB: " + e.getMessage());
        }
    }

    private java.sql.Date convertUtilDateToSqlDate(java.util.Date utilDate) {
        if (utilDate == null) {
            return null;
        }
        return new java.sql.Date(utilDate.getTime());
    }

    private java.util.Date convertSqlDateToUtilDate(java.sql.Date sqlDate) {
        if (sqlDate == null) {
            return null;
        }
        return new java.util.Date(sqlDate.getTime());
    }

    private java.sql.Timestamp convertUtilDateToSqlTimestamp(java.util.Date utilDate) {
        if (utilDate == null) {
            return null;
        }
        return new java.sql.Timestamp(utilDate.getTime());
    }

    private java.util.Date convertSqlTimestampToUtilDate(java.sql.Timestamp sqlTimestamp) {
        if (sqlTimestamp == null) {
            return null;
        }
        return new java.util.Date(sqlTimestamp.getTime());
    }

    public String agregarOrganizador(String nombre, String email, String institucion) {
        String idOrganizador = String.format("ORG-%03d", contadorIdOrganizador++);
        Organizador nuevoOrganizador = new Organizador(idOrganizador, nombre, email, institucion);
        this.personasRegistradas.add(nuevoOrganizador);
        System.out.println("Organizador " + nombre + " agregado con ID: " + idOrganizador);
        return idOrganizador;
    }

    public String agregarParticipante(String nombre, String email, int edad, String genero) {
        String idParticipante = String.format("PART-%03d", contadorIdParticipante++);
        Participante nuevoParticipante = new Participante(idParticipante, nombre, email, edad, genero);
        this.personasRegistradas.add(nuevoParticipante);
        System.out.println("Participante " + nombre + " agregado con ID: " + idParticipante);
        return idParticipante;
    }

    public boolean agregarActividad(String tipo, String titulo, String descripcion, String fechaInicioStr, String fechaFinStr, String lugar, String organizadorId, int cupoMaximo, Integer numSesiones, Boolean esAbiertoAlPublico, Boolean requiereMateriales) {
        Organizador organizador = null;
        for (Persona p : personasRegistradas) {
            if (p instanceof Organizador && p.getIdPersona().equals(organizadorId)) {
                organizador = (Organizador) p;
                break;
            }
        }

        if (organizador == null) {
            System.out.println("Organizador con ID " + organizadorId + " no encontrado.");
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaInicio = null;
        Date fechaFin = null;
        try {
            fechaInicio = sdf.parse(fechaInicioStr);
            fechaFin = sdf.parse(fechaFinStr);
        } catch (ParseException e) {
            System.out.println("Formato de fecha invalido. Use YYYY-MM-DD.");
            return false;
        }

        int idActividad = contadorIdActividad++;
        ActividadComunitaria nuevaActividad = null;

        switch (tipo.toLowerCase()) {
            case "curso":
                if (numSesiones == null) {
                    System.out.println("Numero de sesiones es requerido para un Curso.");
                    return false;
                }
                nuevaActividad = new Curso(idActividad, titulo, descripcion, fechaInicio, fechaFin, lugar, organizador, cupoMaximo, numSesiones);
                break;
            case "evento":
                if (esAbiertoAlPublico == null) {
                    System.out.println("El estado 'abierto al publico' es requerido para un Evento.");
                    return false;
                }
                nuevaActividad = new Evento(idActividad, titulo, descripcion, fechaInicio, fechaFin, lugar, organizador, cupoMaximo, esAbiertoAlPublico);
                break;
            case "taller":
                if (requiereMateriales == null) {
                    System.out.println("El estado 'requiere materiales' es requerido para un Taller.");
                    return false;
                }
                nuevaActividad = new Taller(idActividad, titulo, descripcion, fechaInicio, fechaFin, lugar, organizador, cupoMaximo, requiereMateriales);
                break;
            default:
                System.out.println("Tipo de actividad no reconocido.");
                return false;
        }

        this.actividadesRegistradas.add(nuevaActividad);
        System.out.println("Actividad '" + titulo + "' agregada con ID: " + idActividad);
        return true;
    }
    
    public boolean inscribirParticipanteEnActividad(String participanteId, int actividadId) {
        Participante participanteParaInscribir = null;
        for (Persona p : personasRegistradas) {
            if (p instanceof Participante && p.getIdPersona().equals(participanteId)) {
                participanteParaInscribir = (Participante) p;
                break;
            }
        }

        if (participanteParaInscribir == null) {
            System.out.println("El participante con ID " + participanteId + " no esta registrado.");
            return false;
        }

        ActividadComunitaria actividadParaInscribir = null;
        for (ActividadComunitaria ac : actividadesRegistradas) {
            if (ac.getIdActividad() == actividadId) {
                actividadParaInscribir = ac;
                break;
            }
        }
        
        if (actividadParaInscribir.getListaParticipantes().contains(participanteParaInscribir)) {
            System.out.println("El participante " + participanteParaInscribir.getNombre() + " ya esta inscrito en la actividad " + actividadParaInscribir.getTitulo() + ".");
            return false;
        }

        String identificadorInscripcion = String.format("INS-%03d", contadorIdInscripcion++);
        Date fechaActualInscripcion = new Date();
        Inscripcion<String> nuevaInscripcion = new Inscripcion<>(identificadorInscripcion, participanteParaInscribir, actividadParaInscribir, fechaActualInscripcion);
        this.inscripcionesRealizadas.add(nuevaInscripcion);
        actividadParaInscribir.registrarParticipante(participanteParaInscribir); 
        System.out.println("Inscripcion (ID: " + identificadorInscripcion + ") de " + participanteParaInscribir.getNombre() + " en " + actividadParaInscribir.getTitulo() + " realizada con exito.");
        return true;
    }

    public ArrayList<ActividadComunitaria> getActividadesRegistradas() {
        return actividadesRegistradas;
    }

    public ArrayList<Persona> getPersonasRegistradas() {
        return personasRegistradas;
    }

    public ArrayList<Inscripcion> getInscripcionesRealizadas() {
        return inscripcionesRealizadas;
    }

    public void listarTodasLasActividades() {
        System.out.println("\n--- Lista de todas las actividades ---");
        if (actividadesRegistradas.isEmpty()) {
            System.out.println("No hay actividades registradas.");
            return;
        }
        for (ActividadComunitaria actividad : actividadesRegistradas) {
            actividad.mostrar();
            System.out.println("Tipo: " + actividad.obtenerTipo());
            System.out.println("");
        }
    }

    public void listarTodasLasInscripciones() {
        System.out.println("\n--- Lista de todas las inscripciones ---");
        if (inscripcionesRealizadas.isEmpty()) {
            System.out.println("No hay inscripciones realizadas.");
            return;
        }
        for (Inscripcion inscripcion : inscripcionesRealizadas) {
            inscripcion.mostrarInscripcion();
            System.out.println("");
        }
    }
}