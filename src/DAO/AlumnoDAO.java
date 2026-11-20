
package DAO;

import Conexion.Conexion;
import modelo.Alumno;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO {

    public List<Alumno> listar() throws SQLException, IOException {

        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT nombre, apellido, dni, correo FROM alumno";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Alumno alumno = new Alumno(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("correo")
                );

                alumnos.add(alumno);
            }
        }

        return alumnos;
    }

    public Alumno buscarPorDni(String dni) throws SQLException, IOException {

        String sql = "SELECT nombre, apellido, dni, correo FROM alumno WHERE dni = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new Alumno(
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("dni"),
                            rs.getString("correo")
                    );
                }
            }
        }

        return null;
    }

    public boolean insertar(Alumno alumno) throws SQLException, IOException {

        String sql = "INSERT INTO alumno (nombre, apellido, dni, correo) VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, alumno.getNombre());
            ps.setString(2, alumno.getApellido());
            ps.setString(3, alumno.getDni());
            ps.setString(4, alumno.getCorreo());

            return ps.executeUpdate() == 1;
        }
    }

    public boolean modificar(String dniActual, Alumno alumno) throws SQLException, IOException {

        String sql = "UPDATE alumno SET nombre = ?, apellido = ?, dni = ?, correo = ? WHERE dni = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, alumno.getNombre());
            ps.setString(2, alumno.getApellido());
            ps.setString(3, alumno.getDni());
            ps.setString(4, alumno.getCorreo());
            ps.setString(5, dniActual);

            return ps.executeUpdate() == 1;
        }
    }

    public boolean eliminar(String dni) throws SQLException, IOException {

        String sql = "DELETE FROM alumno WHERE dni = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);

            return ps.executeUpdate() == 1;
        }
    }
}
