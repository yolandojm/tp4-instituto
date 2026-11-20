package vista;

import DAO.AlumnoDAO;
import DAO.CursoDAO;
import modelo.Alumno;
import modelo.Curso;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class VentanaInstituto extends JFrame {

    private final CursoDAO cursoDAO = new CursoDAO();
    private final AlumnoDAO alumnoDAO = new AlumnoDAO();

    private final DefaultTableModel modeloCursos;
    private final JTable tablaCursos;

    private final JTextField campoNombre = new JTextField(15);
    private final JTextField campoApellido = new JTextField(15);
    private final JTextField campoDni = new JTextField(12);
    private final JTextField campoCorreo = new JTextField(18);

    public VentanaInstituto() {
        super("Instituto de Idiomas — Gestión de Inscripciones");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Cursos del Instituto", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setBorder(new EmptyBorder(8, 8, 0, 8));
        add(titulo, BorderLayout.NORTH);

        String[] columnasCursos = {
                "Código", "Idioma", "Nivel", "Turno",
                "Cupo", "Inscriptos", "Vacantes", "Cuota Mensual"
        };

        modeloCursos = new DefaultTableModel(columnasCursos, 0) {
            @Override
            public Class<?> getColumnClass(int columna) {
                return switch (columna) {
                    case 4, 5, 6 -> Integer.class;
                    case 7 -> Double.class;
                    default -> String.class;
                };
            }

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaCursos = new JTable(modeloCursos);
        tablaCursos.setAutoCreateRowSorter(true);

        JScrollPane scrollCursos = new JScrollPane(tablaCursos);
        scrollCursos.setBorder(new TitledBorder("Listado de Cursos (Ordenable por columna)"));
        add(scrollCursos, BorderLayout.CENTER);

        add(crearPanelAltaAlumno(), BorderLayout.SOUTH);

        cargarCursos();
    }

    private JPanel crearPanelAltaAlumno() {
        JPanel contenedor = new JPanel(new BorderLayout(8, 8));
        contenedor.setBorder(new TitledBorder("Alta de Alumno"));

        JPanel formulario = new JPanel(new GridLayout(2, 4, 8, 8));
        formulario.setBorder(new EmptyBorder(6, 10, 6, 10));

        formulario.add(new JLabel("Nombre:"));
        formulario.add(campoNombre);
        formulario.add(new JLabel("Apellido:"));
        formulario.add(campoApellido);
        formulario.add(new JLabel("DNI:"));
        formulario.add(campoDni);
        formulario.add(new JLabel("Correo (opcional):"));
        formulario.add(campoCorreo);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonGuardarAlumno = new JButton("Guardar Alumno");
        botonGuardarAlumno.addActionListener(e -> guardarAlumno());
        botones.add(botonGuardarAlumno);

        contenedor.add(formulario, BorderLayout.CENTER);
        contenedor.add(botones, BorderLayout.SOUTH);
        return contenedor;
    }

    private void guardarAlumno() {
        String nombre = campoNombre.getText().trim();
        String apellido = campoApellido.getText().trim();
        String dni = campoDni.getText().trim();
        String correoTexto = campoCorreo.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Nombre, apellido y DNI son obligatorios.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!dni.matches("\\d{7,10}")) {
            JOptionPane.showMessageDialog(
                    this,
                    "El DNI tiene que contener entre 7 y 10 números, sin puntos ni letras.",
                    "DNI inválido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!correoTexto.isEmpty() && (!correoTexto.contains("@") || !correoTexto.contains("."))) {
            JOptionPane.showMessageDialog(
                    this,
                    "El correo ingresado no tiene un formato válido.",
                    "Correo inválido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String correo = correoTexto.isEmpty() ? null : correoTexto;
        Alumno nuevo = new Alumno(nombre, apellido, dni, correo);

        try {
            alumnoDAO.insertar(nuevo);
            campoNombre.setText("");
            campoApellido.setText("");
            campoDni.setText("");
            campoCorreo.setText("");
            cargarCursos();
            JOptionPane.showMessageDialog(
                    this,
                    "Alumno registrado correctamente.",
                    "Alta de alumno",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    mensajeErrorAlumno(ex),
                    "No se pudo guardar",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró o no se pudo leer el archivo config.properties.",
                    "Error de configuración",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarCursos() {
        modeloCursos.setRowCount(0);
        try {
            for (Curso c : cursoDAO.listar()) {
                modeloCursos.addRow(new Object[] {
                        c.getCodigo(),
                        c.getIdioma(),
                        c.getNivel(),
                        c.getTurno(),
                        c.getCupo(),
                        c.getInscriptos(),
                        c.getVacantes(),
                        c.getCuotaMensual()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    mensajeErrorBase(ex),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró o no se pudo leer el archivo config.properties.",
                    "Error de configuración",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String mensajeErrorAlumno(SQLException ex) {
        return switch (ex.getErrorCode()) {
            case 1062 -> "Ya existe un alumno registrado con ese DNI.";
            case 0 -> "No se pudo conectar con la base de datos. Verificá que el servidor MySQL esté encendido.";
            case 1045 -> "Usuario o contraseña de la base de datos incorrectos.";
            case 1049 -> "La base de datos 'instituto' no existe. Ejecutá primero el script instituto.sql.";
            default -> "No se pudo guardar el alumno en la base de datos.";
        };
    }

    private String mensajeErrorBase(SQLException ex) {
        return switch (ex.getErrorCode()) {
            case 0 -> "No se pudo conectar con la base de datos. Verificá que el servidor MySQL esté encendido.";
            case 1045 -> "Usuario o contraseña de la base de datos incorrectos.";
            case 1049 -> "La base de datos 'instituto' no existe. Ejecutá primero el script instituto.sql.";
            default -> "No se pudieron cargar los cursos desde la base de datos.";
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaInstituto().setVisible(true));
    }
}
