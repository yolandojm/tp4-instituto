
package DAO;

import Conexion.Conexion;
import modelo.Inscripcion;
import modelo.Alumno;
import modelo.Curso;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDAO {

    public List<Inscripcion> listar() throws SQLException, IOException {
        List<Inscripcion> inscripciones = new ArrayList<>();

        String sql = """
                SELECT a.nombre, a.apellido, a.dni, a.correo,
                       c.codigo, c.idioma, c.nivel, c.turno,
                       c.cupo, c.vacantes, c.cuota_mensual,
                       i.fecha, i.cuota_paga
                FROM inscripcion i
                JOIN alumno a ON i.id_alumno = a.id_alumno
                JOIN curso c ON i.id_curso = c.id_curso
                """;

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                inscripciones.add(mapearInscripcion(rs));
            }
        }

        return inscripciones;
    }

    public List<Inscripcion> listarDeudores() throws SQLException, IOException {
        List<Inscripcion> deudores = new ArrayList<>();

        String sql = """
                SELECT a.nombre, a.apellido, a.dni, a.correo,
                       c.codigo, c.idioma, c.nivel, c.turno,
                       c.cupo, c.vacantes, c.cuota_mensual,
                       i.fecha, i.cuota_paga
                FROM inscripcion i
                JOIN alumno a ON i.id_alumno = a.id_alumno
                JOIN curso c ON i.id_curso = c.id_curso
                WHERE i.cuota_paga = FALSE
                """;

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                deudores.add(mapearInscripcion(rs));
            }
        }

        return deudores;
    }

    public Inscripcion buscarPorDniYCodigo(String dni, String codigo)
            throws SQLException, IOException {

        String sql = """
                SELECT a.nombre, a.apellido, a.dni, a.correo,
                       c.codigo, c.idioma, c.nivel, c.turno,
                       c.cupo, c.vacantes, c.cuota_mensual,
                       i.fecha, i.cuota_paga
                FROM inscripcion i
                JOIN alumno a ON i.id_alumno = a.id_alumno
                JOIN curso c ON i.id_curso = c.id_curso
                WHERE a.dni = ? AND c.codigo = ?
                """;

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);
            ps.setString(2, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearInscripcion(rs);
                }
            }
        }

        return null;
    }

    public boolean insertar(Inscripcion inscripcion)
            throws SQLException, IOException {

        String sqlInsertar = """
                INSERT INTO inscripcion (id_alumno, id_curso, fecha, cuota_paga)
                SELECT a.id_alumno, c.id_curso, ?, ?
                FROM alumno a
                JOIN curso c ON c.codigo = ?
                WHERE a.dni = ?
                """;
        String sqlVacantes = "UPDATE curso SET vacantes = vacantes - 1 WHERE codigo = ?";

        try (Connection con = Conexion.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement psInsertar = con.prepareStatement(sqlInsertar);
                 PreparedStatement psVacantes = con.prepareStatement(sqlVacantes)) {

                psInsertar.setDate(1, Date.valueOf(inscripcion.getFecha()));
                psInsertar.setBoolean(2, inscripcion.getCuotaPaga());
                psInsertar.setString(3, inscripcion.getCurso().getCodigo());
                psInsertar.setString(4, inscripcion.getAlumno().getDni());

                if (psInsertar.executeUpdate() != 1) {
                    con.rollback();
                    return false;
                }

                psVacantes.setString(1, inscripcion.getCurso().getCodigo());

                if (psVacantes.executeUpdate() != 1) {
                    con.rollback();
                    return false;
                }

                con.commit();
                return true;

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    public boolean modificar(String dni, String codigo, Inscripcion inscripcion)
            throws SQLException, IOException {

        String sql = """
                UPDATE inscripcion i
                JOIN alumno a ON i.id_alumno = a.id_alumno
                JOIN curso c ON i.id_curso = c.id_curso
                SET i.fecha = ?, i.cuota_paga = ?
                WHERE a.dni = ? AND c.codigo = ?
                """;

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(inscripcion.getFecha()));
            ps.setBoolean(2, inscripcion.getCuotaPaga());
            ps.setString(3, dni);
            ps.setString(4, codigo);

            return ps.executeUpdate() == 1;
        }
    }

    public boolean eliminar(String dni, String codigo)
            throws SQLException, IOException {

        String sqlEliminar = """
                DELETE i FROM inscripcion i
                JOIN alumno a ON i.id_alumno = a.id_alumno
                JOIN curso c ON i.id_curso = c.id_curso
                WHERE a.dni = ? AND c.codigo = ?
                """;
        String sqlVacantes = "UPDATE curso SET vacantes = vacantes + 1 WHERE codigo = ?";

        try (Connection con = Conexion.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement psEliminar = con.prepareStatement(sqlEliminar);
                 PreparedStatement psVacantes = con.prepareStatement(sqlVacantes)) {

                psEliminar.setString(1, dni);
                psEliminar.setString(2, codigo);

                if (psEliminar.executeUpdate() != 1) {
                    con.rollback();
                    return false;
                }

                psVacantes.setString(1, codigo);

                if (psVacantes.executeUpdate() != 1) {
                    con.rollback();
                    return false;
                }

                con.commit();
                return true;

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    private Inscripcion mapearInscripcion(ResultSet rs) throws SQLException {

        Alumno alumno = new Alumno(
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("dni"),
                rs.getString("correo")
        );

        Curso curso = new Curso(
                rs.getString("codigo"),
                rs.getString("idioma"),
                rs.getString("nivel"),
                rs.getString("turno"),
                rs.getInt("cupo"),
                rs.getInt("vacantes"),
                rs.getDouble("cuota_mensual")
        );

        return new Inscripcion(
                alumno,
                curso,
                rs.getDate("fecha").toLocalDate(),
                rs.getBoolean("cuota_paga")
        );
    }
}
