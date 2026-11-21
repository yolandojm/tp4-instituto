package modelo;

import java.util.Objects;

public class Alumno {

    private String nombre;
    private String apellido;
    private String dni;
    private String correo;

    public Alumno(String nombre, String apellido, String dni, String correo) {
        setNombre(nombre);
        setApellido(apellido);
        setDni(dni);
        setCorreo(correo);
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDni() {
        return dni;
    }

    public String getCorreo() {
        return correo;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede quedar vacío");
        }
        this.nombre = nombre.trim();
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El apellido no puede quedar vacío");
        }
        this.apellido = apellido.trim();
    }

    public void setDni(String dni) {
        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("El DNI no puede quedar vacío");
        }
        this.dni = dni.trim();
    }

    public void setCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            this.correo = null;
            return;
        }
        this.correo = correo.trim();
    }

    @Override
    public String toString() {
        return apellido + ", " + nombre + " (DNI: " + dni + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Alumno otro = (Alumno) obj;
        return Objects.equals(dni, otro.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni);
    }
}
