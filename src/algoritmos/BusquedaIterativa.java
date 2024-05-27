/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algoritmos;

import entidades.Nodo;
import entidades.Vecino;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * Clase en donde se aplica la combinación de una busqueda por menor costo local
 * y busqueda en profundidad para iterar objetos Nodo en una lista
 *
 * @author Usuario
 */
public class BusquedaIterativa {
    public static Nodo BusquedaPorMenorCosto(ArrayList<Nodo> nodosRecorridos, ArrayList<Nodo> Grafo, int segundoActual) {
        Nodo nodoActual = nodosRecorridos.get(nodosRecorridos.size() - 1);
        //Se verifica que el Nodo actual no sea nulo
        if (nodoActual == null) {
            System.out.println("El nodo actual es nulo.");
            System.exit(0);
        }
        int indice = ((segundoActual * 48) / 86400);
        //Se crea el objeto Random para los sorteos en caso de empate
        Random moneda = new Random();
        //Se obtienen los vecinos no visitados
        ArrayList<Vecino> vecinosLibres = vecinosNoRecorridos(nodoActual, Grafo, nodosRecorridos);
        if (!vecinosLibres.isEmpty()) {
            //Se setea el primer vecino libre como vecino óptimo para las verificaciones
            Vecino vecinoOptimo = vecinosLibres.get(0);
            Nodo nodoOptimo = obtieneNodoVecino(vecinoOptimo, Grafo);
            //Inicio de las verificaciones de todos los vecinos libres
            for (Vecino V : vecinosLibres) {
                //Si el costo del vecino actual es menor que el costo del vecino óptimo
                if (V.getCostos()[indice] < vecinoOptimo.getCostos()[indice]) {
                    vecinoOptimo = V;
                    nodoOptimo = obtieneNodoVecino(vecinoOptimo, Grafo);
                    //En caso de que los costos sean iguales se realizan  verificaciones extras
                } else if (Objects.equals(V.getCostos()[indice], vecinoOptimo.getCostos()[indice])) {
                    //Se comparan la cantidad de vecinos que tienen ambos nodos buscando el que tenga una mayor cantidad, puesto que estos tienen una mayor probabilidad de recorridos libres
                    if (obtieneNodoVecino(V, Grafo).getVecinos().size() > obtieneNodoVecino(vecinoOptimo, Grafo).getVecinos().size()) {
                        vecinoOptimo = V;
                        nodoOptimo = obtieneNodoVecino(vecinoOptimo, Grafo);
                        //En caso de que la cantidad de vecinos sea la misma, la elección se realiza al azar
                    } else if (obtieneNodoVecino(V, Grafo).getVecinos().size() == obtieneNodoVecino(vecinoOptimo, Grafo).getVecinos().size()) {
                        Vecino seleccionEnProfundidad = BusquedaProfunda.seleccionaVecino(vecinosLibres, Grafo, nodosRecorridos);
                        if (seleccionEnProfundidad != null) {
                            vecinoOptimo = seleccionEnProfundidad;
                            nodoOptimo = obtieneNodoVecino(vecinoOptimo, Grafo);
                        } else {
                            //Al obtener un True como resultado, se setea como vecino optimo al vecino actual
                            if (moneda.nextBoolean()) {
                                vecinoOptimo = V;
                                nodoOptimo = obtieneNodoVecino(vecinoOptimo, Grafo);
                            }                        
                        }
                    }
                }
            }
            return nodoOptimo;
        } else {
            //En caso de que no tenga vecinos sin recorrer en profundidad se busca algún nodo sin recorrer 
            Vecino vecinoOptimo = BusquedaProfunda.expandeVecinos(nodoActual, Grafo, nodosRecorridos, indice);
            //Se obtiene el objeto Nodo a paratir del objeto Vecino
            Nodo nodoOptimo = null;
            //En caso de que la busqueda en profundidad haya devuelto un Nodo, entonces se setea este como nodo optimo
            if (vecinoOptimo != null) {
                nodoOptimo = obtieneNodoVecino(vecinoOptimo, Grafo);
            } else {
                //Si en profundidad no se pudo encontrar un nodo sin recorrer se setea el primer vecino como optimo para realizar algunas verificaciones
                vecinoOptimo = nodoActual.getVecinos().get(0);
                nodoOptimo = obtieneNodoVecino(vecinoOptimo, Grafo);
                //Verificación de vecinos
                for (Vecino V : nodoActual.getVecinos()) {
                    //Se obtiene el objeto Nodo a paratir del objeto Vecino
                    Nodo nodoVecino = obtieneNodoVecino(V, Grafo);
                    //Si los costos son iguales se verifica el historial del recorrido para ver que nodo fue el mas recorrido
                    int contadorVecinoOptimo = 0;
                    int contadorVecinoActual = 0;
                    //Se recorren los nodos ya recorridos para sumar en las variables contadores de cada nodo
                    for (Nodo N : nodosRecorridos) {
                        if (N.getNodo().equals(nodoOptimo.getNodo())) {
                            contadorVecinoOptimo++;
                        }
                        if (N.getNodo().equals(nodoVecino.getNodo())) {
                            contadorVecinoActual++;
                        }
                    }
                    //Si el vecino actual fue menos recorrido que el vecino óptimo, entonces este vecino es el elegido como vecino óptimo
                    if (contadorVecinoActual < contadorVecinoOptimo) {
                        vecinoOptimo = V;
                        nodoOptimo = nodoVecino;
                    } else if (contadorVecinoActual == contadorVecinoOptimo) {
                        //En caso de que ambos vecinos se recorrieron la misma cantidad de veces, se elige por medio del azar cual va a ser el vecino seleccionado
                        if (moneda.nextBoolean()) {
                            vecinoOptimo = V;
                            nodoOptimo = nodoVecino;
                        }
                    }
                }
            }
            return nodoOptimo;
        }
    }

    /**
     * Funcion que devuelve el objeto Nodo a partir de un objeto Vecino
     *
     * @param V
     * @param Grafo
     * @return Nodo
     */
    public static Nodo obtieneNodoVecino(Vecino V, ArrayList<Nodo> Grafo) {
        if (V == null) {
            System.out.println("Error en obtieneNodoVecino. Parametro V es nulo");
            return null;
        }

        for (Nodo N : Grafo) {
            if (N.getNodo().equals(V.getNodo())) {
                return N;
            }
        }
        return null;
    }

    /**
     * Función que devuelve una lista de vecinos no recorridos de un nodo
     *
     * @param nodoActual
     * @param Grafo
     * @param nodosRecorridos
     * @return
     */
    public static ArrayList<Vecino> vecinosNoRecorridos(Nodo nodoActual, ArrayList<Nodo> Grafo, ArrayList<Nodo> nodosRecorridos) {
        ArrayList<Vecino> vecinosLibres = new ArrayList();
        for (Vecino V : nodoActual.getVecinos()) {
            if (!nodosRecorridos.contains(obtieneNodoVecino(V, Grafo)) && obtieneNodoVecino(V, Grafo).getInd_obligatorio().equals("S")) {
                vecinosLibres.add(V);
            }
        }
        return vecinosLibres;
    }
}
