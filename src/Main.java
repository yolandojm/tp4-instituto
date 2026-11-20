import vista.VentanaInstituto;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaInstituto().setVisible(true));
    }
}
