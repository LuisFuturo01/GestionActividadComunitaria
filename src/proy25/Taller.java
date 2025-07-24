package proy25;

import java.util.Date;

public class Taller extends ActividadComunitaria {
    protected Boolean requiereMateriales;

    public Taller(int idActividad, String titulo, String descripcion,Date fechaIn, Date fechaFin, String lugar, Organizador organizador, int cupoMaximo, Boolean requiereMateriales) {
        super(idActividad, titulo, descripcion,fechaIn, fechaFin, lugar, organizador,cupoMaximo);
        this.requiereMateriales = requiereMateriales;
    }

    @Override
    public String obtenerTipo() {
        return "Taller";
    }

    public Boolean getRequiereMateriales() {
        return requiereMateriales;
    }
}
