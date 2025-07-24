package proy25;
import java.util.Date;
public class Curso extends ActividadComunitaria {
    protected int numSesiones;

    public Curso(int idActividad, String titulo, String descripcion,Date fechaIn, Date fechaFin, String lugar, Organizador organizador, int cupoMaximo, int numSesiones) {
        super(idActividad, titulo, descripcion,fechaIn, fechaFin, lugar, organizador,cupoMaximo);
        this.numSesiones = numSesiones;
    }
    @Override
    public String obtenerTipo() {
        return "Curso";
    }
}
