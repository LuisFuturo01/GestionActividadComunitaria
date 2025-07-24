package proy25;

public abstract class Persona<T> {
    protected T idPersona;
    protected String nombre;
    protected String email;

    public Persona(T idPersona, String nombre, String email) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.email = email;
    }

    public T getIdPersona() {
        return this.idPersona;
    }
    public String getNombre() {
        return this.nombre;
    }
    public String getEmail(){
        return this.email;
    }
    public void setIdPersona(T idPersona){
        this.idPersona=idPersona;
    }
    public void setNombre(String nombre){
        this.nombre=nombre;
    }
    public void setEmail(String email){
        this.email=email;
    }

    public void mostrar() {
        System.out.println("ID Persona: " + this.idPersona);
        System.out.println("Nombre: " + this.nombre);
        System.out.println("Email: " + this.email);
    }

    public void mostrar(String prefijo) {
        System.out.println(prefijo + "ID Persona: " + this.idPersona);
        System.out.println(prefijo + "Nombre: " + this.nombre);
        System.out.println(prefijo + "Email: " + this.email);
    }
}
