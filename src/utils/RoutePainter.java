package utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.painter.Painter;

/**
 * https://github.com/msteiger/jxmapviewer2/blob/master/examples/src/sample2_waypoints/RoutePainter.java
 * @author Usuario
 */
public class RoutePainter implements Painter<JXMapViewer> {
    private Color color = Color.RED;
    private final boolean antiAlias = true;

    private final List<GeoPosition> track;
    private int currentIndex;

    public RoutePainter(List<GeoPosition> track) {
        this.track = new ArrayList<>(track);
        this.currentIndex = 0; 
    }

    public void setCurrentIndex(int index) {
        this.currentIndex = index;
    }
    
    public void setCurrentColor(Color col) {
        this.color = col;
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
        g = (Graphics2D) g.create();
        // convert from viewport to world bitmap
        Rectangle rect = map.getViewportBounds();
        g.translate(-rect.x, -rect.y);
        if (antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        // do the drawing
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(4));
        drawRoute(g, map, currentIndex);
        
        // do the drawing again
        g.setColor(color);
        g.setStroke(new BasicStroke(2));
        drawRoute(g, map, currentIndex);
        g.dispose();
    }

    private void drawRoute(Graphics2D g, JXMapViewer map, int endIndex) {
        int lastX = 0;
        int lastY = 0;
        boolean first = true;
        for (int i = 0; i <= endIndex; i++) {
            GeoPosition gp = track.get(i);
            Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
            if (first) {
                first = false;
            } else {
                g.drawLine(lastX, lastY, (int) pt.getX(), (int) pt.getY());
            }
            lastX = (int) pt.getX();
            lastY = (int) pt.getY();
        }
    }
}
