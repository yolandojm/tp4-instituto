
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
                       c.cupo, c.cuota_mensual,
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

    public Inscripcion buscarPorDniYCodigo(String dni, String codigo)
            throws SQLException, IOException {

        String sql = """
                SELECT a.nombre, a.apellido, a.dni, a.correo,
                       c.codigo, c.idioma, c.nivel, c.turno,
                       c.cupo, c.cuota_mensual,
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

        String sqlAlumno = "SELECT id_alumno FROM alumno WHERE dni = ?";
        String sqlCurso = "SELECT id_curso, vacantes FROM curso WHERE codigo = ? FOR UPDATE";
        String sqlExiste = """
                SELECT id_inscripcion FROM inscripcion
                WHERE id_alumno = ? AND id_curso = ?
                """;
        String sqlInsertar = """
                INSERT INTO inscripcion (id_alumno, id_curso, fecha, cuota_paga)
                VALUES (?, ?, ?, ?)
                """;
        String sqlVacantes = "UPDATE curso SET vacantes = vacantes - 1 WHERE id_curso = ?";

        try (Connection con = Conexion.getConnection()) {
            con.setAutoCommit(false);

            try {
                int idAlumno;
                int idCurso;
                int vacantes;

                try (PreparedStatement ps = con.prepareStatement(sqlAlumno)) {
                    ps.setString(1, inscripcion.getAlumno().getDni());

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("El alumno no existe.");
                        }
                        idAlumno = rs.getInt("id_alumno");
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(sqlCurso)) {
                    ps.setString(1, inscripcion.getCurso().getCodigo());

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("El curso no existe.");
                        }
                        idCurso = rs.getInt("id_curso");
                        vacantes = rs.getInt("vacantes");
                    }
                }

                if (vacantes <= 0) {
                    throw new SQLException("El curso no tiene vacantes.");
                }

                try (PreparedStatement ps = con.prepareStatement(sqlExiste)) {
                    ps.setInt(1, idAlumno);
                    ps.setInt(2, idCurso);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            throw new SQLException("El alumno ya está inscripto en este curso.");
                        }
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(sqlInsertar)) {
                    ps.setInt(1, idAlumno);
                    ps.setInt(2, idCurso);
                    ps.setDate(3, Date.valueOf(inscripcion.getFecha()));
                    ps.setBoolean(4, inscripcion.getCuotaPaga());
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(sqlVacantes)) {
                    ps.setInt(1, idCurso);
                    ps.executeUpdate();
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

        String sqlBuscar = """
                SELECT i.id_inscripcion, i.id_curso
                FROM inscripcion i
                JOIN alumno a ON i.id_alumno = a.id_alumno
                JOIN curso c ON i.id_curso = c.id_curso
                WHERE a.dni = ? AND c.codigo = ?
                FOR UPDATE
                """;

        String sqlEliminar = "DELETE FROM inscripcion WHERE id_inscripcion = ?";
        String sqlVacantes = "UPDATE curso SET vacantes = vacantes + 1 WHERE id_curso = ?";

        try (Connection con = Conexion.getConnection()) {
            con.setAutoCommit(false);

            try {
                int idInscripcion;
                int idCurso;

                try (PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
                    ps.setString(1, dni);
                    ps.setString(2, codigo);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            return false;
                        }

                        idInscripcion = rs.getInt("id_inscripcion");
                        idCurso = rs.getInt("id_curso");
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(sqlEliminar)) {
                    ps.setInt(1, idInscripcion);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(sqlVacantes)) {
                    ps.setInt(1, idCurso);
                    ps.executeUpdate();
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
