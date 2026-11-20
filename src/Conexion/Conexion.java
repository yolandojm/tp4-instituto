package Conexion;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

    public class Conexion {

        public static Connection getConnection() throws SQLException, IOException {

            Properties propiedades = new Properties();

            try (FileInputStream archivo = new FileInputStream("config.properties")) {
                propiedades.load(archivo);
            }

            String url = propiedades.getProperty("db.url");
            String usuario = propiedades.getProperty("db.user");
            String clave = propiedades.getProperty("db.password");

            return DriverManager.getConnection(url, usuario, clave);
        }

    }
