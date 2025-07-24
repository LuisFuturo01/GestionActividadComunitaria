package proy25;

import java.util.Date;
import java.util.ArrayList;

public abstract class ActividadComunitaria {
    protected int idActividad;
    protected String titulo;
    protected String descripcion;
    protected Date fechaInicio;
    protected Date fechaFin;
    protected int cupoMaximo;
    protected String lugar;
    protected Organizador organizador;
    protected ArrayList<Participante> listaParticipantes;

    public ActividadComunitaria(int idActividad, String titulo, String descripcion,Date fechaInicio, Date fechaFin, String lugar, Organizador organizador, int cupoMaximo) {
        this.idActividad=idActividad;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio=fechaInicio;
        this.fechaFin=fechaFin;
        this.cupoMaximo = cupoMaximo;
        this.lugar = lugar;
        this.organizador = organizador;
        this.listaParticipantes=new ArrayList<>();
    }

    public int getIdActividad() {
        return idActividad;
    }

    public String getTitulo() {
        return titulo;
    }

    public void registrarParticipante(Participante p) {
        if (estaDisponible()) {
            listaParticipantes.add(p);
            System.out.println("Participante " + p.getNombre() + " inscrito en " + this.titulo + ".");
        } else {
            System.out.println("No hay cupos disponibles en " + this.titulo + ".");
        }
    }

    public boolean estaDisponible() {
        return this.listaParticipantes.size() < cupoMaximo;
    }

    public void listarParticipantes() {
        if (listaParticipantes.isEmpty()) {
            System.out.println("No hay participantes en " + this.titulo + ".");
            return;
        }
        System.out.println("Lista de participantes en " + this.titulo + ":");
        for(int i=0; i<this.listaParticipantes.size();i++){
           this.listaParticipantes.get(i).mostrar();
        }
    }

    public void mostrar(){
        System.out.println("ID actividad: " + this.idActividad);
        System.out.println("Titulo: " + this.titulo);
        System.out.println("Descripcion: " + this.descripcion);
        System.out.println("Fecha inicio: " + this.fechaInicio);
        System.out.println("Fecha fin: " + this.fechaFin);
        System.out.println("Lugar: " + this.lugar);
        System.out.print("Organizador: ");
        if (this.organizador != null) {
            this.organizador.mostrar();
        } else {
            System.out.println("Organizador no especificado.");
        }
        System.out.println("Cupo maximo: " + this.cupoMaximo);
    }
    public ArrayList<Participante> getListaParticipantes() {
        return listaParticipantes;
    }
    public abstract String obtenerTipo();
}
