package modelo;

public class Curso {
    private String codigo;
    private String idioma;
    private String nivel;
    private String turno;
    private int cupo;
    private int vacantes;
    private double cuotaMensual;

    public Curso(String codigo, String idioma, String nivel, String turno, int cupo, double cuotaMensual){
        this(codigo, idioma, nivel, turno, cupo, cupo, cuotaMensual);
    }

    public Curso(String codigo, String idioma, String nivel, String turno, int cupo, int vacantes, double cuotaMensual){
        this.codigo = codigo;
        this.idioma = idioma;
        this.nivel = nivel;
        this.turno = turno;
        this.cupo = cupo;
        this.vacantes = vacantes;
        this.cuotaMensual = cuotaMensual;
    }
    public String getCodigo(){
        return codigo;
    }
    public String getIdioma(){
        return idioma;
    }
    public String getNivel(){
        return nivel;
    }
    public String getTurno(){
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
        this.codigo = codigo;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public void setVacantes(int vacantes) {
        this.vacantes = vacantes;
    }

    public void setCuotaMensual(double cuotaMensual) {
        this.cuotaMensual = cuotaMensual;
    }

}
