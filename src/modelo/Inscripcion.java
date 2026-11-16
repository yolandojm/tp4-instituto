package modelo;

import java.time.LocalDate;

public class Inscripcion {

    private Alumno alumno;
    private Curso curso;
    private LocalDate fecha;
    private boolean cuotaPaga;

    public Inscripcion(Alumno alumno, Curso curso, LocalDate fecha, boolean cuotaPaga) {
        this.alumno = alumno;
        this.curso = curso;
        this.fecha = fecha;
        this.cuotaPaga = cuotaPaga;
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
        this.alumno = alumno;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setCuotaPaga(boolean cuotaPaga) {
        this.cuotaPaga = cuotaPaga;
    }
}
