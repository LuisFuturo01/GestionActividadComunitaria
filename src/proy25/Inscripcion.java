package proy25;

import java.util.Date;

public class Inscripcion<T> {
    protected T idInscripcion;
    protected Participante participante;
    protected ActividadComunitaria actividad;
    protected Date fechaInscripcion;

    public Inscripcion(T iI, Participante p, ActividadComunitaria a, Date f){
        this.idInscripcion=iI;
        this.participante=p;
        this.actividad=a;
        this.fechaInscripcion=f;
    }

    public T getIdInscripcion(){
        return this.idInscripcion;
    }
    public Participante getParticipante(){
        return this.participante;
    }
    public ActividadComunitaria getActividad(){
        return this.actividad;
    }
    public Date getFechaInscripcion(){
        return this.fechaInscripcion;
    }
    public void setIdInscripcion(T idInscripcion){
        this.idInscripcion=idInscripcion;
    }
    public void setParticipante(Participante participante){
        this.participante=participante;
    }
    public void setActividad(ActividadComunitaria actividad){
        this.actividad=actividad;
    }
    public void setFechaInscripcion(Date fechaInscripcion){
        this.fechaInscripcion=fechaInscripcion;
    }

    public void mostrarInscripcion(){
        System.out.println("Detalle de Inscripcion (ID: " + getIdInscripcion() + "):");
        System.out.println("Participante:");
        getParticipante().mostrar();
        System.out.println("Actividad:");
        getActividad().mostrar();
        System.out.println("Fecha de inscripcion: " + this.fechaInscripcion);
    }
}
