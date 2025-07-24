package proy25;

public class Organizador extends Persona<String> {
    protected String institucion;

    public Organizador(String idPersona, String nombre, String email, String institucion) {
        super(idPersona, nombre, email);
        this.institucion = institucion;
    }

    @Override
    public void mostrar() {
        super.mostrar();
        System.out.println("Institucion: " + institucion);
    }
}
