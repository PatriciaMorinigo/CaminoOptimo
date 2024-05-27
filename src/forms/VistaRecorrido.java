/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package forms;

import entidades.Config;
import entidades.Nodo;
import entidades.Vecino;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;
import utils.LecturaCSV;
import utils.OperacionesDb;
import utils.RoutePainter;

/**
 *
 * @author Usuario
 */
public class VistaRecorrido extends javax.swing.JFrame {
    
    private static int currentIndex = 0;
    private static ArrayList<GeoPosition> recorrido;
    private static JXMapViewer mapViewer;
    private static WaypointPainter<Waypoint> waypointPainter;
    private static RoutePainter routePainter;
    private static ArrayList<String> historialRecorrido;
    private static ArrayList<Nodo> Grafo = new ArrayList();

    //Constructor
    public VistaRecorrido(ArrayList<Nodo> nodosRecorridos, int cod_recorrido_cab) {
        initComponents();
        int segundoInicial = OperacionesDb.obtieneSegundoInicial(cod_recorrido_cab, Menu.conn);
        int segundoActual = segundoInicial;
        int x = 0;

        Grafo = LecturaCSV.crearGrafo(OperacionesDb.obtieneNombreArchivo(cod_recorrido_cab, Menu.conn));
        String nodosPorCoordenada = "";

        currentIndex = 0;
        recorrido = new ArrayList<>();
        historialRecorrido = new ArrayList<>();
        mapViewer = new JXMapViewer();

        //Configuración del mapa
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        //Creación y adición de coordenadas a la lista recorrido
        for (Nodo N : nodosRecorridos) {
            if (x < nodosRecorridos.size() - 1) {
                Nodo nodoVecino = nodosRecorridos.get(x + 1);
                if (x > 0){
                    int segundoCosto = devuelveSegundos(N, nodoVecino, segundoActual);
                    segundoActual += segundoCosto;
                    if (segundoActual >= 86400) {
                    segundoActual = segundoActual - 86400;
                    }
                }
            }

            nodosPorCoordenada += "|" + N.getNodo();
            ArrayList<String> coordenadas = LecturaCSV.obtieneCoordenada2(N);
            if (coordenadas != null) {
                Date date = new Date();
                date.setHours(segundoActual / 3600);
                date.setMinutes((segundoActual % 3600) / 60);
                date.setSeconds(segundoActual % 60);

                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

                Double latitud = Double.valueOf(coordenadas.get(0));
                Double longitud = Double.valueOf(coordenadas.get(1));
                recorrido.add(new GeoPosition(latitud, longitud));
                historialRecorrido.add(("Nodos: " + nodosPorCoordenada + "|\n" + dateFormat.format(date) + "\n-------------------------------------------------\n"));
                nodosPorCoordenada = "";
            }
            x++;
        }
        
        //Interacciones del mouse con el mapa
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        //Configuraciones para el dibujo de la ruta
        routePainter = new RoutePainter(recorrido);
        mapViewer.zoomToBestFit(new HashSet<>(recorrido), 1.0);
        mapViewer.setZoom(4);
        waypointPainter = new WaypointPainter<>();
        List<Painter<JXMapViewer>> painters = new ArrayList<>();
        painters.add(routePainter);
        painters.add(waypointPainter);
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(painter);

        PN_Mapa.removeAll();
        PN_Mapa.setLayout(new BorderLayout());
        PN_Mapa.add(mapViewer, BorderLayout.CENTER);
        PN_Mapa.revalidate();
        PN_Mapa.repaint();
        
        actualizarWaypoints();
        escribeHistorial();
    }

    private static void actualizarWaypoints() {
        SwingUtilities.invokeLater(() -> {
            waypointPainter.setWaypoints(new HashSet<>(Arrays.asList(new DefaultWaypoint(recorrido.get(currentIndex)))));
            routePainter.setCurrentIndex(currentIndex);
            mapViewer.repaint();
        });
    }

    private void escribeHistorial() {
        if (CB_Escribe_Historial.isSelected()) {
            int x = 0;
            TA_Historial.setText(null);
            for (String cadena : historialRecorrido) {
                TA_Historial.setText(TA_Historial.getText() + cadena);
                x++;

                if (x == currentIndex + 1) {
                    break;
                }
            }        
        }
    }

    public static int devuelveCosto(Nodo nodoActual, Nodo nodoSiguiente, int segundoActual) {
        for (Nodo N : Grafo) {
            if (N.getNodo().equals(nodoActual.getNodo())) {
                nodoActual = N;
                break;
            }
        }

        for (Vecino V : nodoActual.getVecinos()) {
            if (V.getNodo().equals(nodoSiguiente.getNodo())) {
                return (V.getCostos()[((segundoActual * 48) / 86400)]);
            }
        }

        return 0;    
    }
    
    
    private static int devuelveSegundos(Nodo nodoActual, Nodo nodoSiguiente, int segundoActual) {
        for (Nodo N : Grafo) {
            if (N.getNodo().equals(nodoActual.getNodo())) {
                nodoActual = N;
                break;
            }
        }

        for (Vecino V : nodoActual.getVecinos()) {
            if (V.getNodo().equals(nodoSiguiente.getNodo())) {
                int costo = V.getCostos()[((segundoActual * 48) / 86400)];
                switch (costo) {
                    case 1:
                        return 1;
                    case 2:
                        return 7;
                    case 3:
                        return 10;
                    case 4:
                        return 33;
                    case 5:
                        return 40;
                    case 6:
                        return 50;
                    case 7:
                        return 60;
                    case 8:
                        return 63;
                    case 9:
                        return 67;
                    case 10:
                        return 70;
                    case 11:
                        return 75;
                    case 12:
                        return 80;
                    case 13:
                        return 90;
                }
            }
        }

        return 0;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PN_Contenedor_Mapa = new javax.swing.JPanel();
        PN_Mapa = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        BT_Anterior = new javax.swing.JButton();
        BT_Siguiente = new javax.swing.JButton();
        BT_Reiniciar = new javax.swing.JButton();
        BT_Completar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TA_Historial = new javax.swing.JTextArea();
        CB_Escribe_Historial = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Visualizador del Recorrido");
        setMaximumSize(new java.awt.Dimension(1117, 664));
        setMinimumSize(new java.awt.Dimension(1117, 664));

        PN_Contenedor_Mapa.setBorder(javax.swing.BorderFactory.createTitledBorder("Mapa"));
        PN_Contenedor_Mapa.setPreferredSize(new java.awt.Dimension(750, 640));

        javax.swing.GroupLayout PN_MapaLayout = new javax.swing.GroupLayout(PN_Mapa);
        PN_Mapa.setLayout(PN_MapaLayout);
        PN_MapaLayout.setHorizontalGroup(
            PN_MapaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 741, Short.MAX_VALUE)
        );
        PN_MapaLayout.setVerticalGroup(
            PN_MapaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout PN_Contenedor_MapaLayout = new javax.swing.GroupLayout(PN_Contenedor_Mapa);
        PN_Contenedor_Mapa.setLayout(PN_Contenedor_MapaLayout);
        PN_Contenedor_MapaLayout.setHorizontalGroup(
            PN_Contenedor_MapaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PN_Contenedor_MapaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PN_Mapa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        PN_Contenedor_MapaLayout.setVerticalGroup(
            PN_Contenedor_MapaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PN_Contenedor_MapaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PN_Mapa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Acciones"));

        BT_Anterior.setText("Anterior");
        BT_Anterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_AnteriorActionPerformed(evt);
            }
        });

        BT_Siguiente.setText("Siguiente");
        BT_Siguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_SiguienteActionPerformed(evt);
            }
        });

        BT_Reiniciar.setText("Reiniciar Recorrido");
        BT_Reiniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_ReiniciarActionPerformed(evt);
            }
        });

        BT_Completar.setText("Completar Recorrido");
        BT_Completar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_CompletarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BT_Reiniciar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BT_Anterior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BT_Completar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BT_Siguiente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BT_Anterior)
                    .addComponent(BT_Siguiente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BT_Reiniciar)
                    .addComponent(BT_Completar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        BT_Anterior.setMnemonic(KeyEvent.VK_LEFT);
        BT_Siguiente.setMnemonic(KeyEvent.VK_RIGHT);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Nodos y Tiempo"));

        TA_Historial.setColumns(20);
        TA_Historial.setLineWrap(true);
        TA_Historial.setRows(5);
        jScrollPane1.setViewportView(TA_Historial);

        CB_Escribe_Historial.setSelected(true);
        CB_Escribe_Historial.setText("¿Escribir el historial del recorrido?");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(CB_Escribe_Historial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CB_Escribe_Historial)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PN_Contenedor_Mapa, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PN_Contenedor_Mapa, javax.swing.GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BT_SiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_SiguienteActionPerformed
        if (currentIndex < recorrido.size() - 1) {
            currentIndex++;
            actualizarWaypoints();
            escribeHistorial();
        }
    }//GEN-LAST:event_BT_SiguienteActionPerformed

    private void BT_AnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_AnteriorActionPerformed
        if (currentIndex > 0) {
            currentIndex--;
            actualizarWaypoints();
            if (currentIndex > 0) {
                escribeHistorial();
            } else {
                TA_Historial.setText(null);
            }
        }
    }//GEN-LAST:event_BT_AnteriorActionPerformed

    private void BT_ReiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_ReiniciarActionPerformed
        currentIndex = 0;
        actualizarWaypoints();
        TA_Historial.setText(null);        
    }//GEN-LAST:event_BT_ReiniciarActionPerformed

    private void BT_CompletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_CompletarActionPerformed
        currentIndex = recorrido.size() - 1;
        actualizarWaypoints();
        escribeHistorial();
    }//GEN-LAST:event_BT_CompletarActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BT_Anterior;
    private javax.swing.JButton BT_Completar;
    private javax.swing.JButton BT_Reiniciar;
    private javax.swing.JButton BT_Siguiente;
    private javax.swing.JCheckBox CB_Escribe_Historial;
    private javax.swing.JPanel PN_Contenedor_Mapa;
    private javax.swing.JPanel PN_Mapa;
    private javax.swing.JTextArea TA_Historial;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
