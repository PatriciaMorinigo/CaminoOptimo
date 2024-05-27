package utils;

import com.opencsv.CSVReader;
import entidades.Config;
import entidades.Nodo;
import entidades.Vecino;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class LecturaCSV {

    private static final String pathCsv = Config.getPathCsv();

    //Lee CSV y crea un grafo
    public static ArrayList<Nodo> crearGrafo(String nombreArchivo) {
        //Creacion de lista de objetos del tipo Nodo
        ArrayList<Nodo> nodos = new ArrayList();
        int indiceColumnasCsv = 51;
        //Creacion de nueva instancia para lectura de .csv
        CSVReader reader;
        try {
            //Abrir archivo csv
            reader = new CSVReader(new FileReader(pathCsv + nombreArchivo), ',');
            //Variable que va a guardar dentro de un arreglo de string los valores de las celdas de cada fila
            String[] record;
            try {
                //Lectura linea por linea del archivo
                while ((record = reader.readNext()) != null) {
                    //Creacion de instancias de objetos
                    Nodo n = new Nodo();
                    Vecino v = new Vecino();
                    Integer[] costos = new Integer[indiceColumnasCsv - 3];
                    //Fin de creacion de instancias
                    try {
                        if (nodos.isEmpty()) {
                            //Primera lectura del archivo
                            n.setNodo(record[0]);
                            v.setNodo(record[1]);
                            for (int indice = 3; indice < indiceColumnasCsv; indice++) {
                                costos[indice - 3] = Integer.valueOf(record[indice]);
                            }
                            v.setCostos(costos);
                            n.setInd_obligatorio(record[2]);
                            n.addVecinos(v);
                            nodos.add(n);
                        } else {
                            //Existe ya un Nodo en la lista
                            //Bandera que indica que el Nodo fue encontrado
                            boolean existe = false;

                            // Recorrido a la lista de nodos registrados para verificar si ya se leyó
                            // el mismo Nodo con otro destino.
                            for (Nodo N : nodos) {
                                if (record[0].equals(N.getNodo())) {
                                    v.setNodo(record[1]);
                                    for (int indice = 3; indice < indiceColumnasCsv; indice++) {
                                        costos[indice - 3] = Integer.valueOf(record[indice]);
                                    }
                                    v.setCostos(costos);
                                    N.setInd_obligatorio(record[2]);
                                    N.addVecinos(v);
                                    existe = true;
                                }
                            }

                            //Si no existe el Nodo de origen actual dentro de la lista de nodos
                            if (existe != true) {
                                //Se añade un Nodo mas a la lista de nodos
                                n.setNodo(record[0]);
                                v.setNodo(record[1]);
                                for (int indice = 3; indice < indiceColumnasCsv; indice++) {
                                    costos[indice - 3] = Integer.valueOf(record[indice]);
                                }
                                v.setCostos(costos);
                                n.setInd_obligatorio(record[2]);
                                n.addVecinos(v);
                                nodos.add(n);
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println(record[0] + ", " + record[1] + ", " + record[2]);
                        throw new IOException(ex);
                    }
                }
            } catch (IOException ex) {
                System.out.println("Ocurrió un error al leer el archivo. ---> " + ex);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Ocurrió un error al buscar el archivo. ---> " + ex);
        }
        //Se devuelve la lista de nodos
        return nodos;
    }

    /**
     * Función que devuelve las calles en donde se puede iniciar el recorrido
     *
     * @return ArrayList con los nombres de las calles
     * @author Usuario
     */
    public static ArrayList<String> obtienePuntosSalida() {
        //Creacion de lista
        ArrayList<String> listaCalles = new ArrayList();
        //Creacion de nueva instancia para lectura de .csv
        CSVReader reader;
        try {
            //Abrir archivo csv
            reader = new CSVReader(new FileReader(pathCsv + "puntos_de_salida.csv"), ',');
            //Variable que va a guardar dentro de un arreglo de string los valores de las celdas de cada fila
            String[] record;
            try {
                //Lectura linea por linea del archivo
                while ((record = reader.readNext()) != null) {
                    listaCalles.add(record[0]);
                }
                //Devolvemos la lista de calles
                return listaCalles;
            } catch (IOException ex) {
                System.out.println("Ocurrió un error al leer el archivo. ---> " + ex);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Ocurrió un error al buscar el archivo. ---> " + ex);
        }
        return listaCalles;
    }

    /**
     * Función que devuelve las calles en donde se puede iniciar el recorrido
     *
     * @param calle
     * @return ArrayList con los nombres de las calles
     * @author Usuario
     */
    public static String obtieneNodoSalida(String calle) {
        //Creacion de nueva instancia para lectura de .csv
        CSVReader reader;
        try {
            //Abrir archivo csv
            reader = new CSVReader(new FileReader(pathCsv + "puntos_de_salida.csv"), ',');
            //Variable que va a guardar dentro de un arreglo de string los valores de las celdas de cada fila
            String[] record;
            try {
                //Lectura linea por linea del archivo
                while ((record = reader.readNext()) != null) {
                    if (record[0].equals(calle)) {
                        return record[1];
                    }
                }
            } catch (IOException ex) {
                System.out.println("Ocurrió un error al leer el archivo. ---> " + ex);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Ocurrió un error al buscar el archivo. ---> " + ex);
        }
        return null;
    }

    public static String obtieneCalleSalida(String nodo) {
        //Creacion de nueva instancia para lectura de .csv
        CSVReader reader;
        try {
            //Abrir archivo csv
            reader = new CSVReader(new FileReader(pathCsv + "puntos_de_salida.csv"), ',');
            //Variable que va a guardar dentro de un arreglo de string los valores de las celdas de cada fila
            String[] record;
            try {
                //Lectura linea por linea del archivo
                while ((record = reader.readNext()) != null) {
                    if (record[1].equals(nodo)) {
                        return record[0];
                    }
                }
            } catch (IOException ex) {
                System.out.println("Ocurrió un error al leer el archivo. ---> " + ex);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Ocurrió un error al buscar el archivo. ---> " + ex);
        }
        return null;
    }

    /**
     * Función que devuelve las calles en donde se puede iniciar el recorrido
     *
     * @param nodo
     * @return ArrayList con los nombres de las calles
     * @author Usuario
     */
    public static String obtieneCoordenada(Nodo nodo) {
        //Creacion de nueva instancia para lectura de .csv
        CSVReader reader;
        try {
            //Abrir archivo csv
            reader = new CSVReader(new FileReader(pathCsv + "coordenadas_nodos.csv"), ',');
            //Variable que va a guardar dentro de un arreglo de string los valores de las celdas de cada fila
            String[] record;
            try {
                //Lectura linea por linea del archivo
                while ((record = reader.readNext()) != null) {
                    if (record[0].equals(nodo.getNodo())) {
                        return "[" + record[1] + "," + record[2] + "],";
                    }
                }
            } catch (IOException ex) {
                System.out.println("Ocurrió un error al leer el archivo. ---> " + ex);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Ocurrió un error al buscar el archivo. ---> " + ex);
        }
        return null;
    }

    public static ArrayList<String> obtieneCoordenada2(Nodo nodo) {
        //Creacion de nueva instancia para lectura de .csv
        CSVReader reader;
        try {
            //Abrir archivo csv
            reader = new CSVReader(new FileReader(pathCsv + "coordenadas_nodos.csv"), ',');
            //Variable que va a guardar dentro de un arreglo de string los valores de las celdas de cada fila
            String[] record;
            try {
                //Lectura linea por linea del archivo
                while ((record = reader.readNext()) != null) {
                    if (record[0].equals(nodo.getNodo())) {
                        ArrayList<String> coordenadas = new ArrayList();
                        coordenadas.add(record[1]);
                        coordenadas.add(record[2]);
                        return coordenadas;
                    }
                }
            } catch (IOException ex) {
                System.out.println("Ocurrió un error al leer el archivo. ---> " + ex);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Ocurrió un error al buscar el archivo. ---> " + ex);
        }
        return null;
    }

    /**
     * Función utilizada para obtener el costo de un Nodo a otro
     *
     * @param nodoOrigen
     * @param nodoDestino
     * @param archivo
     * @return int
     * @author Usuario
     */
    public static int buscaCosto(String nodoOrigen, String nodoDestino, String archivo) {
        //Creacion de nueva instancia para lectura de .csv
        CSVReader reader;
        try {
            //Abrir archivo csv
            reader = new CSVReader(new FileReader(pathCsv + archivo), ',');
            //Variable que va a guardar dentro de un arreglo de string los valores de las celdas de cada fila
            String[] record;
            try {
                //Lectura linea por linea del archivo
                while ((record = reader.readNext()) != null) {
                    if (record[0].equals(nodoOrigen) && record[1].equals(nodoDestino)) {
                        return Integer.parseInt(record[2]);
                    }
                }
            } catch (IOException ex) {
                System.out.println("Ocurrió un error al leer el archivo. ---> " + ex);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Ocurrió un error al buscar el archivo. ---> " + ex);
        }
        return 0;
    }

    /**
     * Obtiene los nodos desde una CSV con los nombres de los nodos para formar
     * el recorrido
     *
     * @param Grafo
     * @param archivo
     * @return
     */
    public static ArrayList<Nodo> recorridoNodosCadena(ArrayList<Nodo> Grafo, String archivo) {
        //Creacion de nueva instancia para lectura de .csv
        CSVReader reader;
        ArrayList<Nodo> nodosRecorridos = new ArrayList();
        try {
            //Abrir archivo csv
            reader = new CSVReader(new FileReader(pathCsv + archivo), ',');
            //Variable que va a guardar dentro de un arreglo de string los valores de las celdas de cada fila
            String[] record;
            try {
                //Lectura linea por linea del archivo
                while ((record = reader.readNext()) != null) {
                    for (Nodo nodo : Grafo) {
                        if (nodo.getNodo().equals(record[0])) {
                            nodosRecorridos.add(nodo);
                        }
                    }
                }
            } catch (IOException ex) {
                System.out.println("Ocurrió un error al leer el archivo. ---> " + ex);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Ocurrió un error al buscar el archivo. ---> " + ex);
        }
        return nodosRecorridos;
    }
}
