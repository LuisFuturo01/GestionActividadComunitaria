package proy25;

public class Participante extends Persona<String> {
    protected int edad;
    protected String genero;

    public Participante(String idPersona, String nombre, String email, int edad, String genero) {
        super(idPersona, nombre, email);
        this.edad = edad;
        this.genero = genero;
    }
    
    @Override
    public void mostrar() {
        super.mostrar();
        System.out.println("Edad: " + edad + " años");
        System.out.println("Genero: " + genero);
    }
}
