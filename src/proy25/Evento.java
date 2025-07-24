package proy25;

import java.util.Date;

public class Evento extends ActividadComunitaria {
    private Boolean esAbiertoAlPublico; 

    public Evento(int idActividad, String titulo, String descripcion,Date fechaIn, Date fechaFin, String lugar, Organizador organizador, int cupoMaximo, Boolean abierto) {
        super(idActividad, titulo, descripcion,fechaIn, fechaFin, lugar, organizador,cupoMaximo);
        this.esAbiertoAlPublico = abierto;
    }

    @Override
    public String obtenerTipo() {
        return "Evento";
    }

    public Boolean getEsAbiertoAlPublico() {
        return esAbiertoAlPublico;
    }
}
