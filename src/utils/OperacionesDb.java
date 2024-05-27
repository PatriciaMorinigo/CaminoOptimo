/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import entidades.DatosBusqueda;
import entidades.Nodo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class OperacionesDb {
    /**
     * Obtiene los registros de recorridos guardados en la base de datos
     * @param conn
     * @return 
     */
    public static ResultSet obtieneRegistros (Connection conn) {
        try {
            try {
                String sql = "SELECT C.*, (SELECT COUNT(*) FROM RECORRIDO_DET D WHERE C.COD_RECORRIDO_CAB = D.COD_RECORRIDO_CAB) NODOS_RECORRIDOS FROM RECORRIDO_CAB C ORDER BY SEGUNDO_INICIO ASC, COSTO ASC";
                PreparedStatement consulta = conn.prepareStatement(sql);
                return consulta.executeQuery();
            } catch (SQLException ex) {
                throw new Exception ("Ocurrió un error al obtener los datos de la cabecera. " + ex);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
            return null;
        }
    }
    
    /**
     * Función que registra en una base de datos cabecera y detalle del recorrido que se obtuvo
     * @param datosRecorrido
     * @param conn
     * @return 
     */
    public static int insertaDb (DatosBusqueda datosRecorrido, Connection conn, String nombreArchivo) {
        int cod_recorrido_cab = 0;
        
        try {
            //Obtención del código para la cabecera
            try {
                String sql = "SELECT IFNULL(MAX(C.COD_RECORRIDO_CAB), 0) + 1 CODIGO\n"
                           + "FROM RECORRIDO_CAB C";
                PreparedStatement consulta = conn.prepareStatement(sql);
                cod_recorrido_cab = consulta.executeQuery().getInt("CODIGO");
            } catch (SQLException ex) {
                throw new Exception("Ocurrió un error al obtener el código de la cabecera. " + ex);
            }
            
            //Inserción de la cabecera
            try {
                String sql = "INSERT INTO RECORRIDO_CAB VALUES (?, ?, ?, ?, ?)";
                PreparedStatement consulta = conn.prepareStatement(sql);
                consulta.setInt(1, cod_recorrido_cab);
                consulta.setInt(2, datosRecorrido.getCostoTotal());
                consulta.setInt(3, datosRecorrido.getSegundoInicio());
                consulta.setInt(4, datosRecorrido.getSegundoFinal());
                consulta.setString(5, nombreArchivo);
                consulta.execute();
            } catch (SQLException ex) {
                throw new Exception("Ocurrió un error en la inserción de la cabecera. " + ex);
            }
            
            //Inserción de los detalles
            try {
                int index = 1;
                for (Nodo N : datosRecorrido.getRecorrido()) {
                    String sql = "INSERT INTO RECORRIDO_DET VALUES ((SELECT IFNULL(MAX(D.COD_RECORRIDO_DET), 0) + 1 FROM RECORRIDO_DET D), ?, ?, ?)";
                    PreparedStatement consulta = conn.prepareStatement(sql);
                    consulta.setInt(1, index);
                    consulta.setString(2, N.getNodo());
                    consulta.setInt(3, cod_recorrido_cab);
                    consulta.execute();
                    index ++;
                }
            } catch (SQLException ex) {
                throw new Exception("Ocurrió un error en la inserción del detalle. " + ex);
            }
        } catch(Exception ex) { 
            JOptionPane.showMessageDialog(null, ex.getMessage());
            return 0;
        }
        return cod_recorrido_cab;
    }

    /**
     * Para obtener el nombre de la calle inicial apartir del nombre del nodo
     * @param cod_recorrido_cab
     * @param conn
     * @return 
     */
    public static String obtieneCalleInicial (int cod_recorrido_cab, Connection conn) {
        try {
            try {
                String sql = "SELECT * FROM RECORRIDO_DET D WHERE D.COD_RECORRIDO_CAB = ? ORDER BY D.ORDEN ASC LIMIT 1";
                PreparedStatement consulta = conn.prepareStatement(sql);
                consulta.setInt(1, cod_recorrido_cab);
                ResultSet resultados = consulta.executeQuery();
                while (resultados.next()) {
                    return LecturaCSV.obtieneCalleSalida(resultados.getString("NODO"));
                }
            } catch (SQLException ex) {
                throw new Exception ("Ocurrió un error al obtener los datos de la cabecera. " + ex);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
            return null;
        }        
        return null;
    }
    
    /**
     * Devuelve la lista de los nodos de un recorrido guardado en la base de datos
     * @param cod_recorrido_cab
     * @param conn
     * @return 
     */
    public static ArrayList<Nodo> obtieneRecorrido (int cod_recorrido_cab, Connection conn) {
        try {
            try {
                ArrayList<Nodo> nodosRecorridos = new ArrayList();
                String sql = "SELECT * FROM RECORRIDO_DET D WHERE D.COD_RECORRIDO_CAB = ? ORDER BY D.ORDEN ASC";
                PreparedStatement consulta = conn.prepareStatement(sql);
                consulta.setInt(1, cod_recorrido_cab);
                ResultSet resultados = consulta.executeQuery();
                
                while (resultados.next()) {
                    Nodo nodo = new Nodo();
                    nodo.setNodo(resultados.getString("NODO"));
                    nodosRecorridos.add(nodo);
                }
                
                return nodosRecorridos;
            } catch (SQLException ex) {
                throw new Exception ("Ocurrió un error al obtener los nodos recorridos. " + ex);
            }
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
            return null;
        }
    }
    
    /**
     * Funcion para obtener el la hora de inicio de un recorrido
     * @param cod_recorrido_cab
     * @param conn
     * @return 
     */
    public static int obtieneSegundoInicial (int cod_recorrido_cab, Connection conn) {
        try {
            try {
                String sql = "SELECT * FROM RECORRIDO_CAB C WHERE C.COD_RECORRIDO_CAB = ?";
                PreparedStatement consulta = conn.prepareStatement(sql);
                consulta.setInt(1, cod_recorrido_cab);
                ResultSet resultados = consulta.executeQuery();
                
                while (resultados.next()) {
                    return resultados.getInt("SEGUNDO_INICIO");
                }
                
                return 0;
            } catch (SQLException ex) {
                throw new Exception ("Ocurrió un error al obtener los nodos recorridos. " + ex);
            }
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
            return 0;
        }
    }
    
    /**
     * Función que obtiene de la db el nombre del archivo de itinerario que se analizó
     * @param cod_recorrido_cab
     * @param conn
     * @return 
     */
    public static String obtieneNombreArchivo (int cod_recorrido_cab, Connection conn) {
        try {
            try {
                String sql = "SELECT * FROM RECORRIDO_CAB C WHERE C.COD_RECORRIDO_CAB = ?";
                PreparedStatement consulta = conn.prepareStatement(sql);
                consulta.setInt(1, cod_recorrido_cab);
                ResultSet resultados = consulta.executeQuery();
                
                while (resultados.next()) {
                    return resultados.getString("NOMBRE_ARCHIVO");
                }
                
                return null;
            } catch (SQLException ex) {
                throw new Exception ("Ocurrió un error al obtener los nodos recorridos. " + ex);
            }
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
            return null;
        }
    }
    
    /**
     * Función para la eliminación de recorridos
     * @param cod_recorrido_cab
     * @param conn
     * @return 
     */
    public static boolean eliminaRecorrido (int cod_recorrido_cab, Connection conn) {
        try {
            //Eliminación del detalle
            try {
                String sql = "DELETE FROM RECORRIDO_DET WHERE COD_RECORRIDO_CAB = ?";
                PreparedStatement consulta = conn.prepareStatement(sql);
                consulta.setInt(1, cod_recorrido_cab);
                consulta.execute();
            } catch (SQLException ex) {
                throw new Exception ("Ocurrió un error al eliminar los detalles. " + ex);
            }
            //Eliminación de la cabecera
            try {
                String sql = "DELETE FROM RECORRIDO_CAB WHERE COD_RECORRIDO_CAB = ?";
                PreparedStatement consulta = conn.prepareStatement(sql);
                consulta.setInt(1, cod_recorrido_cab);
                consulta.execute();
            } catch (SQLException ex) {
                throw new Exception ("Ocurrió un error al eliminar la cabecera. " + ex);
            }
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
            return false;
        }
    }
}