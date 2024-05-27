/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

/**
 * Clase con la configuraciones de Path para archivos Csv y DB SQLite
 * @author Usuario
 */
public class Config {
    private static final String archivoIntinerarioUno = "grafo_encar_p1.csv";
    private static final String archivoIntinerarioDos = "grafo_encar_p2.csv";
    private static final String archivoIntinerarioTres = "grafo_encar_p3.csv";
    private static final String archivoIntinerarioCompleto = "grafo_encar.csv";
    private static final String pathDb = "C:\\Desktop\\Camino 10.0\\CaminoOptimoIA-master\\";
    private static final String pathCsv = "C:\\Desktop\\Camino 10.0\\CaminoOptimoIA-master\\src\\csv\\";
    private static final String pathExport = "C:\\Desktop\\Camino 10.0\\CaminoOptimoIA-master\\test\\";

    public static String getArchivoIntinerarioUno() {
        return archivoIntinerarioUno;
    }

    public static String getArchivoIntinerarioDos() {
        return archivoIntinerarioDos;
    }

    public static String getArchivoIntinerarioTres() {
        return archivoIntinerarioTres;
    }

    public static String getArchivoIntinerarioCompleto() {
        return archivoIntinerarioCompleto;
    }

    public static String getPathDb() {
        return pathDb;
    }

    public static String getPathCsv() {
        return pathCsv;
    }

    public static String getPathExport() {
        return pathExport;
    }
}
