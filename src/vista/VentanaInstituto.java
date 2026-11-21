package vista;

import dao.AlumnoDAO;
import dao.CursoDAO;
import dao.InscripcionDAO;
import modelo.Alumno;
import modelo.Curso;
import modelo.Inscripcion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class VentanaInstituto extends JFrame {

    private final CursoDAO cursoDAO = new CursoDAO();
    private final AlumnoDAO alumnoDAO = new AlumnoDAO();
    private final InscripcionDAO inscripcionDAO = new InscripcionDAO();

    private final DefaultTableModel modeloCursos;
    private final JTable tablaCursos;

    private final DefaultTableModel modeloInscripciones;
    private final JTable tablaInscripciones;

    private final JTextField campoNombre = new JTextField(10);
    private final JTextField campoApellido = new JTextField(10);
    private final JTextField campoDni = new JTextField(8);
    private final JTextField campoCorreo = new JTextField(12);

    private final JComboBox<Alumno> comboAlumnos = new JComboBox<>();
    private final JComboBox<Curso> comboCursos = new JComboBox<>();
    private final JCheckBox checkCuotaPaga = new JCheckBox("Cuota paga");

    public VentanaInstituto() {
        super("Instituto de Idiomas");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelAltaAlumno(), BorderLayout.NORTH);

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

        JPanel panelTablaCursos = new JPanel(new BorderLayout());
        panelTablaCursos.add(new JLabel("Cursos del Instituto", SwingConstants.CENTER), BorderLayout.NORTH);
        panelTablaCursos.add(new JScrollPane(tablaCursos), BorderLayout.CENTER);

        String[] columnasInscripciones = {
                "DNI Alumno", "Alumno", "Código Curso", "Curso", "Fecha", "Cuota Paga"
        };

        modeloInscripciones = new DefaultTableModel(columnasInscripciones, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaInscripciones = new JTable(modeloInscripciones);
        tablaInscripciones.setAutoCreateRowSorter(true);

        JPanel panelTablaInscripciones = new JPanel(new BorderLayout());
        panelTablaInscripciones.add(new JLabel("Inscripciones Registradas", SwingConstants.CENTER), BorderLayout.NORTH);
        panelTablaInscripciones.add(new JScrollPane(tablaInscripciones), BorderLayout.CENTER);

        JPanel panelCentro = new JPanel(new GridLayout(2, 1, 10, 10));
        panelCentro.add(panelTablaCursos);
        panelCentro.add(panelTablaInscripciones);
        add(panelCentro, BorderLayout.CENTER);

        add(crearPanelInscripciones(), BorderLayout.SOUTH);

        cargar();
    }

    private JPanel crearPanelAltaAlumno() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel("Alta de Alumno", SwingConstants.CENTER), BorderLayout.NORTH);

        JPanel formulario = new JPanel();
        formulario.add(new JLabel("Nombre:"));
        formulario.add(campoNombre);
        formulario.add(new JLabel("Apellido:"));
        formulario.add(campoApellido);
        formulario.add(new JLabel("DNI:"));
        formulario.add(campoDni);
        formulario.add(new JLabel("Correo:"));
        formulario.add(campoCorreo);

        JButton botonGuardar = new JButton("Guardar Alumno");
        botonGuardar.addActionListener(e -> guardarAlumno());
        formulario.add(botonGuardar);

        panel.add(formulario, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelInscripciones() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));

        JPanel filaInscribir = new JPanel();
        filaInscribir.add(new JLabel("Alumno:"));
        filaInscribir.add(comboAlumnos);
        filaInscribir.add(new JLabel("Curso:"));
        filaInscribir.add(comboCursos);
        filaInscribir.add(checkCuotaPaga);

        JButton botonInscribir = new JButton("Inscribir");
        botonInscribir.addActionListener(e -> inscribirAlumno());
        filaInscribir.add(botonInscribir);

        JPanel filaBaja = new JPanel();
        JButton botonBaja = new JButton("Dar de Baja Inscripción Elegida");
        botonBaja.addActionListener(e -> darDeBajaInscripcion());
        filaBaja.add(botonBaja);

        panel.add(filaInscribir);
        panel.add(filaBaja);
        return panel;
    }

    private void guardarAlumno() {
        String nombre = campoNombre.getText().trim();
        String apellido = campoApellido.getText().trim();
        String dni = campoDni.getText().trim();
        String correoTexto = campoCorreo.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre, apellido y DNI son obligatorios");
            return;
        }

        try {
            int numeroDni = Integer.parseInt(dni);
            if (numeroDni <= 0) {
                JOptionPane.showMessageDialog(this, "El DNI tiene que ser un número positivo");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El DNI tiene que ser un número entero");
            return;
        }

        String correo = correoTexto.isEmpty() ? null : correoTexto;

        try {
            alumnoDAO.insertar(new Alumno(nombre, apellido, dni, correo));
            campoNombre.setText("");
            campoApellido.setText("");
            campoDni.setText("");
            campoCorreo.setText("");
            cargar();
            JOptionPane.showMessageDialog(this, "Alumno guardado");
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el alumno");
        }
    }

    private void inscribirAlumno() {
        Alumno alumnoElegido = (Alumno) comboAlumnos.getSelectedItem();
        Curso cursoElegido = (Curso) comboCursos.getSelectedItem();

        if (alumnoElegido == null || cursoElegido == null) {
            JOptionPane.showMessageDialog(this, "Elegí un alumno y un curso primero");
            return;
        }

        Inscripcion nueva = new Inscripcion(
                alumnoElegido,
                cursoElegido,
                LocalDate.now(),
                checkCuotaPaga.isSelected()
        );

        try {
            inscripcionDAO.insertar(nueva);
            checkCuotaPaga.setSelected(false);
            cargar();
            JOptionPane.showMessageDialog(this, "Inscripción guardada");
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar la inscripción");
        }
    }

    private void darDeBajaInscripcion() {
        int filaVista = tablaInscripciones.getSelectedRow();
        if (filaVista == -1) {
            JOptionPane.showMessageDialog(this, "Elegí una fila primero");
            return;
        }

        int filaModelo = tablaInscripciones.convertRowIndexToModel(filaVista);
        String dni = (String) modeloInscripciones.getValueAt(filaModelo, 0);
        String alumno = (String) modeloInscripciones.getValueAt(filaModelo, 1);
        String codigoCurso = (String) modeloInscripciones.getValueAt(filaModelo, 2);

        int r = JOptionPane.showConfirmDialog(
                this,
                "¿Dar de baja la inscripción de " + alumno + " en " + codigoCurso + "?"
        );

        if (r == JOptionPane.YES_OPTION) {
            try {
                inscripcionDAO.eliminar(dni, codigoCurso);
                cargar();
                JOptionPane.showMessageDialog(this, "Inscripción dada de baja");
            } catch (SQLException | IOException ex) {
                JOptionPane.showMessageDialog(this, "No se pudo dar de baja la inscripción");
            }
        }
    }

    private void cargar() {
        modeloCursos.setRowCount(0);
        modeloInscripciones.setRowCount(0);
        comboAlumnos.removeAllItems();
        comboCursos.removeAllItems();

        try {
            for (Curso c : cursoDAO.listar()) {
                modeloCursos.addRow(new Object[]{
                        c.getCodigo(),
                        c.getIdioma(),
                        c.getNivel(),
                        c.getTurno(),
                        c.getCupo(),
                        c.getInscriptos(),
                        c.getVacantes(),
                        c.getCuotaMensual()
                });
                comboCursos.addItem(c);
            }

            for (Alumno a : alumnoDAO.listar()) {
                comboAlumnos.addItem(a);
            }

            for (Inscripcion i : inscripcionDAO.listar()) {
                modeloInscripciones.addRow(new Object[]{
                        i.getAlumno().getDni(),
                        i.getAlumno().getApellido() + ", " + i.getAlumno().getNombre(),
                        i.getCurso().getCodigo(),
                        i.getCurso().getIdioma() + " " + i.getCurso().getNivel(),
                        i.getFecha().toString(),
                        i.getCuotaPaga() ? "Sí" : "No"
                });
            }
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo leer la base");
        }
    }

    public static void main(String[] args) {
        new VentanaInstituto().setVisible(true);
    }
}
