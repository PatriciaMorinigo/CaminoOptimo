package main;

import com.formdev.flatlaf.FlatDarculaLaf;
import forms.Menu;
import static java.awt.Frame.MAXIMIZED_BOTH;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Archivo principal del proyecto
 *
 * @author Usuario
 */
public class CaminoOptimoIA {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Error al inicializar flatlaf ---> " + ex);
        }

        Menu m = new Menu();
        m.setLocationRelativeTo(null);
        m.setVisible(true);
    }
}
