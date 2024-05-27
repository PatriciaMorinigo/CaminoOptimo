/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import entidades.Config;
import entidades.Nodo;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Usuario
 */
public class ExportaRecorrido {
    /**
     * Funcion utilizada para generar html con el mapa que contiene el trazado
     * del recorrido optimo
     * @param nodosRecorridos lista de nodos recorridos
     * @param costoTotal costo total del recorrido
     * @author Usuario
     */
    public static void generaHtml(ArrayList<Nodo> nodosRecorridos, int costoTotal) {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String html = "<!DOCTYPE HTML>\n"
                        + "<html>\n"
                        + "\n"
                        + "<head>\n"
                        + "  <script src=\"https://unpkg.com/wrld.js@1.x.x\"></script>\n"
                        + "  <link href=\"https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.0.1/leaflet.css\" rel=\"stylesheet\"/>\n"
                        + "</head>\n"
                        + "\n"
                        + "<style>\n"
                        + "  html,\n"
                        + "  body,\n"
                        + "  #map {\n"
                        + "    margin: 0;\n"
                        + "    width: 100%;\n"
                        + "    height: 100%;\n"
                        + "  }\n"
                        + "\n"
                        + "  #mapWrapper {\n"
                        + "    padding: 10px;\n"
                        + "    margin: 10px;\n"
                        + "    width: 100%;\n"
                        + "    height: 100%;\n"
                        + "  }\n"
                        + "</style>\n"
                        + "\n"
                        + "\n"
                        + "<style>\n"
                        + "  .arrow-icon {\n"
                        + "    width: 14px;\n"
                        + "    height: 14px;\n"
                        + "  }\n"
                        + "\n"
                        + "  .arrow-icon>div {\n"
                        + "    margin-left: -1px;\n"
                        + "    margin-top: -3px;\n"
                        + "    transform-origin: center center;\n"
                        + "    font: 12px/1.5 \"Helvetica Neue\", Arial, Helvetica, sans-serif;\n"
                        + "  }\n"
                        + "</style>\n"
                        + "\n"
                        + "\n"
                        + "<body>\n"
                        + "  <div id=\"mapWrapper\">\n"
                        + "    <div id=\"map\"></div>\n"
                        + "  </div>\n"
                        + "</body>\n"
                        + "\n"
                        + "\n"
                        + "\n"
                        + "\n"
                        + "<script>\n"
                        + "  function getArrows(arrLatlngs, color, arrowCount, mapObj) {\n"
                        + "\n"
                        + "    if (typeof arrLatlngs === undefined || arrLatlngs == null ||\n"
                        + "      (!arrLatlngs.length) || arrLatlngs.length < 2)\n"
                        + "      return [];\n"
                        + "\n"
                        + "    if (typeof arrowCount === 'undefined' || arrowCount == null)\n"
                        + "      arrowCount = 1;\n"
                        + "\n"
                        + "    if (typeof color === 'undefined' || color == null)\n"
                        + "      color = '';\n"
                        + "    else\n"
                        + "      color = 'color:' + color;\n"
                        + "\n"
                        + "    var result = [];\n"
                        + "    for (var i = 1; i < arrLatlngs.length; i++) {\n"
                        + "      var icon = L.divIcon({ className: 'arrow-icon', bgPos: [5, 5], html: '<div style=\"' + color + ';transform: rotate(' + getAngle(arrLatlngs[i - 1], arrLatlngs[i], -1).toString() + 'deg)\">▶</div>' });\n"
                        + "      \n"
                        + "      for (var c = 1; c <= arrowCount; c++) {\n"
                        + "        result.push(L.marker(myMidPoint(arrLatlngs[i], arrLatlngs[i - 1], (c / (arrowCount + 1)), mapObj), { icon: icon }));\n"
                        + "      }\n"
                        + "    }\n"
                        + "    return result;\n"
                        + "  }\n"
                        + "\n"
                        + "  function getAngle(latLng1, latlng2, coef) {\n"
                        + "    var dy = latlng2[0] - latLng1[0];\n"
                        + "    var dx = Math.cos(Math.PI / 180 * latLng1[0]) * (latlng2[1] - latLng1[1]);\n"
                        + "    var ang = ((Math.atan2(dy, dx) / Math.PI) * 180 * coef);\n"
                        + "    return (ang).toFixed(2);\n"
                        + "  }\n"
                        + "\n"
                        + "  function myMidPoint(latlng1, latlng2, per, mapObj) {\n"
                        + "    if (!mapObj)\n"
                        + "      throw new Error('map is not defined');\n"
                        + "\n"
                        + "    var halfDist, segDist, dist, p1, p2, ratio,\n"
                        + "      points = [];\n"
                        + "\n"
                        + "    p1 = mapObj.project(new L.latLng(latlng1));\n"
                        + "    p2 = mapObj.project(new L.latLng(latlng2));\n"
                        + "\n"
                        + "    halfDist = distanceTo(p1, p2) * per;\n"
                        + "\n"
                        + "    if (halfDist === 0)\n"
                        + "      return mapObj.unproject(p1);\n"
                        + "\n"
                        + "    dist = distanceTo(p1, p2);\n"
                        + "\n"
                        + "    if (dist > halfDist) {\n"
                        + "      ratio = (dist - halfDist) / dist;\n"
                        + "      var res = mapObj.unproject(new Point(p2.x - ratio * (p2.x - p1.x), p2.y - ratio * (p2.y - p1.y)));\n"
                        + "      return [res.lat, res.lng];\n"
                        + "    }\n"
                        + "\n"
                        + "  }\n"
                        + "\n"
                        + "  function distanceTo(p1, p2) {\n"
                        + "    var x = p2.x - p1.x,\n"
                        + "      y = p2.y - p1.y;\n"
                        + "\n"
                        + "    return Math.sqrt(x * x + y * y);\n"
                        + "  }\n"
                        + "\n"
                        + "  function toPoint(x, y, round) {\n"
                        + "    if (x instanceof Point) {\n"
                        + "      return x;\n"
                        + "    }\n"
                        + "    if (isArray(x)) {\n"
                        + "      return new Point(x[0], x[1]);\n"
                        + "    }\n"
                        + "    if (x === undefined || x === null) {\n"
                        + "      return x;\n"
                        + "    }\n"
                        + "    if (typeof x === 'object' && 'x' in x && 'y' in x) {\n"
                        + "      return new Point(x.x, x.y);\n"
                        + "    }\n"
                        + "    return new Point(x, y, round);\n"
                        + "  }\n"
                        + "\n"
                        + "  function Point(x, y, round) {\n"
                        + "    this.x = (round ? Math.round(x) : x);\n"
                        + "    this.y = (round ? Math.round(y) : y);\n"
                        + "  }  \n"
                        + "</script>\n"
                        + "\n"
                        + "<script>\n"
                        + "  var map = new L.Map('map', {\n"
                        + "    center: new L.LatLng(-27.331143, -55.865720),\n"
                        + "    zoom: 16\n"
                        + "  });\n"
                        + "\n"
                        + "  L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', { attribution: '&copy; <a href=\"https://osm.org/copyright\">OpenStreetMap</a> contributors' }).addTo(map);\n"
                        + "\n"
                        + "  var coordenadas = [\n";
                for (Nodo n : nodosRecorridos) {
                    String coordenada = LecturaCSV.obtieneCoordenada(n);
                    if (coordenada != null) {
                        html = html + "\t\t\t" + coordenada + "\n";
                    }
                }
                html += "   ];\n"
                        + "\n"
                        + "  const sleep = ms => new Promise(r => setTimeout(r, ms));\n"
                        + "\n"
                        + "  async function printLine() {\n"
                        + "    for (let i = 0; i < coordenadas.length; i++) {\n"
                        + "      await sleep(50);\n"
                        + "      /*if (i >= 1 && i % 1 === 0) {\n"
                        + "        // Elimina las líneas y flechas anteriores cada 1 línea\n"
                        + "        map.eachLayer(function (layer) {\n"
                        + "          if (layer instanceof L.Polyline || layer instanceof L.Marker) {\n"
                        + "            map.removeLayer(layer);\n"
                        + "          }\n"
                        + "        });\n"
                        + "      }*/\n"
                        + "      var polyline = L.polyline([coordenadas[i], coordenadas[i + 1]], { edit_with_drag: false }).addTo(map);\n"
                        + "      L.featureGroup(getArrows([coordenadas[i], coordenadas[i + 1]], 'green', 1, map)).addTo(map);\n"
                        + "    }\n"
                        + "    printLine();\n"
                        + "  }\n"
                        + "  printLine()"
                        + "\n"
                        + "\n"
                        + "</script>\n"
                        + "\n"
                        + "\n"
                        + "</html>";
                try {
                    Date fecha = new Date();
                    SimpleDateFormat Ff = new SimpleDateFormat("_ddMMyyyy_HHmmss");
                    String ruta = Config.getPathExport() + "recorrido_" + costoTotal + Ff.format(fecha) + ".html";
                    File file = new File(ruta);
                    // Si el archivo no existe es creado
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileWriter fw = new FileWriter(file);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(html);
                    bw.close();
                } catch (IOException ex) {
                    System.out.println("Ocurrió un error al generar el mapa --->" + ex);
                }
            }
        });
    }
}