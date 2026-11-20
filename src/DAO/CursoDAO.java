
package DAO;

import Conexion.Conexion;
import modelo.Curso;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {

    public List<Curso> listar() throws SQLException, IOException {

        List<Curso> cursos = new ArrayList<>();

        String sql = "SELECT codigo, idioma, nivel, turno, cupo, cuota_mensual FROM curso";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Curso curso = new Curso(
                        rs.getString("codigo"),
                        rs.getString("idioma"),
                        rs.getString("nivel"),
                        rs.getString("turno"),
                        rs.getInt("cupo"),
                        rs.getDouble("cuota_mensual")
                );

                cursos.add(curso);
            }
        }

        return cursos;
    }

    public Curso buscarPorCodigo(String codigo) throws SQLException, IOException {

        String sql = "SELECT codigo, idioma, nivel, turno, cupo, cuota_mensual FROM curso WHERE codigo = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new Curso(
                            rs.getString("codigo"),
                            rs.getString("idioma"),
                            rs.getString("nivel"),
                            rs.getString("turno"),
                            rs.getInt("cupo"),
                            rs.getDouble("cuota_mensual")
                    );
                }
            }
        }

        return null;
    }

    public boolean insertar(Curso curso) throws SQLException, IOException {

        String sql = "INSERT INTO curso (codigo, idioma, nivel, turno, cupo, vacantes, cuota_mensual) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, curso.getCodigo());
            ps.setString(2, curso.getIdioma());
            ps.setString(3, curso.getNivel());
            ps.setString(4, curso.getTurno());
            ps.setInt(5, curso.getCupo());
            ps.setInt(6, curso.getCupo());
            ps.setDouble(7, curso.getCuotaMensual());

            return ps.executeUpdate() == 1;
        }
    }

    public boolean modificar(String codigoActual, Curso curso) throws SQLException, IOException {

        String sql = "UPDATE curso SET codigo = ?, idioma = ?, nivel = ?, turno = ?, cupo = ?, cuota_mensual = ? WHERE codigo = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, curso.getCodigo());
            ps.setString(2, curso.getIdioma());
            ps.setString(3, curso.getNivel());
            ps.setString(4, curso.getTurno());
            ps.setInt(5, curso.getCupo());
            ps.setDouble(6, curso.getCuotaMensual());
            ps.setString(7, codigoActual);

            return ps.executeUpdate() == 1;
        }
    }


    public boolean eliminar(String codigo) throws SQLException, IOException {

        String sql = "DELETE FROM curso WHERE codigo = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);

            return ps.executeUpdate() == 1;
        }
    }
}

