package modelo;

import java.time.LocalDate;
import java.util.Objects;

public class Inscripcion {

    private Alumno alumno;
    private Curso curso;
    private LocalDate fecha;
    private boolean cuotaPaga;

    public Inscripcion(Alumno alumno, Curso curso, LocalDate fecha, boolean cuotaPaga) {
        setAlumno(alumno);
        setCurso(curso);
        setFecha(fecha);
        setCuotaPaga(cuotaPaga);
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public Curso getCurso() {
        return curso;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public boolean getCuotaPaga() {
        return cuotaPaga;
    }

    public void setAlumno(Alumno alumno) {
        if (alumno == null) {
            throw new IllegalArgumentException("El alumno no puede ser nulo");
        }
        this.alumno = alumno;
    }

    public void setCurso(Curso curso) {
        if (curso == null) {
            throw new IllegalArgumentException("El curso no puede ser nulo");
        }
        this.curso = curso;
    }

    public void setFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        this.fecha = fecha;
    }

    public void setCuotaPaga(boolean cuotaPaga) {
        this.cuotaPaga = cuotaPaga;
    }

    @Override
    public String toString() {
        return alumno + " — " + curso + " (" + fecha + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Inscripcion otra = (Inscripcion) obj;
        return Objects.equals(alumno, otra.alumno) && Objects.equals(curso, otra.curso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alumno, curso);
    }
}
