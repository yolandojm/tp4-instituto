package modelo;

import java.util.Objects;

public class Curso {

    private String codigo;
    private String idioma;
    private String nivel;
    private String turno;
    private int cupo;
    private int vacantes;
    private double cuotaMensual;

    public Curso(String codigo, String idioma, String nivel, String turno, int cupo, double cuotaMensual) {
        this(codigo, idioma, nivel, turno, cupo, cupo, cuotaMensual);
    }

    public Curso(String codigo, String idioma, String nivel, String turno, int cupo, int vacantes, double cuotaMensual) {
        setCodigo(codigo);
        setIdioma(idioma);
        setNivel(nivel);
        setTurno(turno);
        setCupo(cupo);
        setVacantes(vacantes);
        setCuotaMensual(cuotaMensual);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getIdioma() {
        return idioma;
    }

    public String getNivel() {
        return nivel;
    }

    public String getTurno() {
        return turno;
    }

    public int getCupo() {
        return cupo;
    }

    public int getVacantes() {
        return vacantes;
    }

    public int getInscriptos() {
        return cupo - vacantes;
    }

    public double getCuotaMensual() {
        return cuotaMensual;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del curso no puede quedar vacío");
        }
        this.codigo = codigo.trim();
    }

    public void setIdioma(String idioma) {
        if (idioma == null || idioma.isBlank()) {
            throw new IllegalArgumentException("El idioma no puede quedar vacío");
        }
        this.idioma = idioma.trim();
    }

    public void setNivel(String nivel) {
        if (nivel == null || nivel.isBlank()) {
            throw new IllegalArgumentException("El nivel no puede quedar vacío");
        }
        this.nivel = nivel.trim();
    }

    public void setTurno(String turno) {
        if (turno == null || turno.isBlank()) {
            throw new IllegalArgumentException("El turno no puede quedar vacío");
        }
        this.turno = turno.trim();
    }

    public void setCupo(int cupo) {
        if (cupo <= 0) {
            throw new IllegalArgumentException("El cupo tiene que ser mayor a cero: " + cupo);
        }
        if (this.vacantes > cupo) {
            throw new IllegalArgumentException("El cupo no puede ser menor que las vacantes actuales: " + cupo);
        }
        this.cupo = cupo;
    }

    public void setVacantes(int vacantes) {
        if (vacantes < 0 || vacantes > this.cupo) {
            throw new IllegalArgumentException("Las vacantes tienen que estar entre 0 y " + this.cupo + ": " + vacantes);
        }
        this.vacantes = vacantes;
    }

    public void setCuotaMensual(double cuotaMensual) {
        if (cuotaMensual < 0) {
            throw new IllegalArgumentException("La cuota mensual no puede ser negativa: " + cuotaMensual);
        }
        this.cuotaMensual = cuotaMensual;
    }

    @Override
    public String toString() {
        return codigo + " — " + idioma + " " + nivel + " (" + turno + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Curso otro = (Curso) obj;
        return Objects.equals(codigo, otro.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
