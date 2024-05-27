package utils;

import entidades.Nodo;
import forms.VistaRecorrido;
import java.awt.Color;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

/**
 * Clase Java encargada de generar un mapa dentro de un JFrame de Java Swing
 *
 * @author Usuario
 */
public class GeneradorMapa {

    /**
     * Función que genera el mapa, botones y rutas en el mapa
     *
     * @param nodosRecorridos
     * @param cod_recorrido_cab
     */
    public static void generar(ArrayList<Nodo> nodosRecorridos, int cod_recorrido_cab) {
        VistaRecorrido vista = new VistaRecorrido(nodosRecorridos, cod_recorrido_cab);
        vista.setVisible(true);
        vista.setExtendedState(MAXIMIZED_BOTH);
    }

    public static JXMapViewer preview(ArrayList<Nodo> nodosRecorridos) {
        int currentIndex = 0; //indice del punto actual
        ArrayList<GeoPosition> recorrido; //coordenadas ruta
        JXMapViewer mapViewer;
        //Pintores
        WaypointPainter<Waypoint> waypointPainter;
        RoutePainter routePainter;

        recorrido = new ArrayList<>(); //lista vacia
        mapViewer = new JXMapViewer();

        //Configuración del mapa para OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        //Creación y adición de coordenadas a la lista recorrido
        for (Nodo N : nodosRecorridos) {
            ArrayList<String> coordenadas = LecturaCSV.obtieneCoordenada2(N);
            if (coordenadas != null) {
                Double latitud = Double.valueOf(coordenadas.get(0));
                Double longitud = Double.valueOf(coordenadas.get(1));
                recorrido.add(new GeoPosition(latitud, longitud));
            }
        }

        //Interacciones del mouse con el mapa
        MouseInputListener mia = new PanMouseInputListener(mapViewer); //mover mapa
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer)); //zoom mapa
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        //Configuraciones para el dibujo de la ruta
        routePainter = new RoutePainter(recorrido);
        mapViewer.zoomToBestFit(new HashSet<>(recorrido), 0.5);
        mapViewer.setZoom(5);

        waypointPainter = new WaypointPainter<>();
        List<Painter<JXMapViewer>> painters = new ArrayList<>();
        painters.add(routePainter);
        painters.add(waypointPainter);
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(painter);

        currentIndex = recorrido.size() - 1;
        routePainter.setCurrentColor(Color.RED);
        routePainter.setCurrentIndex(currentIndex);
        mapViewer.repaint();

        return mapViewer;
    }
}
