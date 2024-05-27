/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import entidades.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Clase de conexión con la base de datos SQLite
 * @author Usuario
 */

public class ConexionDb {
    public static Connection getConnection() {
        String url = "jdbc:sqlite:" + Config.getPathDb() + "test.db";
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);
            return conn;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al conectarse a la base de datos. " + ex, "Error de conexión", 2);
            return null;
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al agregar la librería. " + ex, "Error de conexión", 2);
            return null;
        }
    }

    public static void close(Connection con) {
        try {
            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al desconectarse de la base de datos. " + ex, "Error de conexión", 2);
        }
    }
}