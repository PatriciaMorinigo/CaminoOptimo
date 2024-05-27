/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algoritmos;

import entidades.Nodo;
import entidades.NodoSeleccionado;
import entidades.Vecino;
import java.util.ArrayList;

/**
 * Clase donde se ejecuta una busqueda profunda
 *
 * @author Usuario
 */
public class BusquedaProfunda {

    /**
     * Función que ejecuta una búsqueda en profundidad para que pueda ser
     * seleccionado un vecino ya visitado descartando el azar
     *
     * @param nodoActual
     * @param Grafo
     * @param nodosRecorridos
     * @param indice
     * @return
     */
    public static Vecino expandeVecinos(Nodo nodoActual, ArrayList<Nodo> Grafo, ArrayList<Nodo> nodosRecorridos, int indice) {
        ArrayList<NodoSeleccionado> listaNodosSeleccionados = new ArrayList();
        listaNodosSeleccionados.clear();

        //Profundidad 1 NodoActual-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                try {
                    //Verifica si el nodo fue visitado y si es obligatorio
                    if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getInd_obligatorio().equals("S")) {
                        //calcula el costo
                        int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice];
                        NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 2);
                        listaNodosSeleccionados.add(Ns);
                    }
                } catch (Exception ex) {
                    System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino2);
                }
            }
        }

        //Profundidad 2 NodoActual-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    try {
                        if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getInd_obligatorio().equals("S")) {
                            int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice];
                            NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 3);
                            listaNodosSeleccionados.add(Ns);
                        }
                    } catch (Exception ex) {
                        System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino3);
                    }
                }
            }
        }

        //Profundidad 3 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        try {
                            if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getInd_obligatorio().equals("S")) {
                                int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice];
                                NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 4);
                                listaNodosSeleccionados.add(Ns);
                            }
                        } catch (Exception ex) {
                            System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino4);
                        }
                    }
                }
            }
        }

        //Profundidad 4 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            try {
                                if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getInd_obligatorio().equals("S")) {
                                    int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice];
                                    NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 5);
                                    listaNodosSeleccionados.add(Ns);
                                }
                            } catch (Exception ex) {
                                System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino5);
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 5 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                try {
                                    if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getInd_obligatorio().equals("S")) {
                                        int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice];
                                        NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 6);
                                        listaNodosSeleccionados.add(Ns);
                                    }
                                } catch (Exception ex) {
                                    System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino6);
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 6 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    try {
                                        if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getInd_obligatorio().equals("S")) {
                                            int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice];
                                            NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 7);
                                            listaNodosSeleccionados.add(Ns);
                                        }
                                    } catch (Exception ex) {
                                        System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino7);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 7 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        try {
                                            if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getInd_obligatorio().equals("S")) {
                                                int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice];
                                                NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 8);
                                                listaNodosSeleccionados.add(Ns);
                                            }
                                        } catch (Exception ex) {
                                            System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino8);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 8 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        for (Vecino vecino9 : BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getVecinos()) {
                                            try {
                                                if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo).getInd_obligatorio().equals("S")) {
                                                    int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice] + vecino9.getCostos()[indice];
                                                    NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 9);
                                                    listaNodosSeleccionados.add(Ns);
                                                }
                                            } catch (Exception ex) {
                                                System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino9);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 9 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        for (Vecino vecino9 : BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getVecinos()) {
                                            for (Vecino vecino10 : BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo).getVecinos()) {
                                                try {
                                                    if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino10, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino10, Grafo).getInd_obligatorio().equals("S")) {
                                                        int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice] + vecino9.getCostos()[indice] + vecino10.getCostos()[indice];
                                                        NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 10);
                                                        listaNodosSeleccionados.add(Ns);
                                                    }
                                                } catch (Exception ex) {
                                                    System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino10);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 10 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        for (Vecino vecino9 : BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getVecinos()) {
                                            for (Vecino vecino10 : BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo).getVecinos()) {
                                                for (Vecino vecino11 : BusquedaIterativa.obtieneNodoVecino(vecino10, Grafo).getVecinos()) {
                                                    try {
                                                        if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino11, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino11, Grafo).getInd_obligatorio().equals("S")) {
                                                            int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice] + vecino9.getCostos()[indice] + vecino10.getCostos()[indice] + vecino11.getCostos()[indice];
                                                            NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 11);
                                                            listaNodosSeleccionados.add(Ns);
                                                        }
                                                    } catch (Exception ex) {
                                                        System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino11);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 11 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        for (Vecino vecino9 : BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getVecinos()) {
                                            for (Vecino vecino10 : BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo).getVecinos()) {
                                                for (Vecino vecino11 : BusquedaIterativa.obtieneNodoVecino(vecino10, Grafo).getVecinos()) {
                                                    for (Vecino vecino12 : BusquedaIterativa.obtieneNodoVecino(vecino11, Grafo).getVecinos()) {
                                                        try {
                                                            if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino12, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino12, Grafo).getInd_obligatorio().equals("S")) {
                                                                int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice] + vecino9.getCostos()[indice] + vecino10.getCostos()[indice] + vecino11.getCostos()[indice] + vecino12.getCostos()[indice];
                                                                NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 12);
                                                                listaNodosSeleccionados.add(Ns);
                                                            }
                                                        } catch (Exception ex) {
                                                            System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino12);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 12 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        for (Vecino vecino9 : BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getVecinos()) {
                                            for (Vecino vecino10 : BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo).getVecinos()) {
                                                for (Vecino vecino11 : BusquedaIterativa.obtieneNodoVecino(vecino10, Grafo).getVecinos()) {
                                                    for (Vecino vecino12 : BusquedaIterativa.obtieneNodoVecino(vecino11, Grafo).getVecinos()) {
                                                        for (Vecino vecino13 : BusquedaIterativa.obtieneNodoVecino(vecino12, Grafo).getVecinos()) {
                                                            try {
                                                                if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino13, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino13, Grafo).getInd_obligatorio().equals("S")) {
                                                                    int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice] + vecino9.getCostos()[indice] + vecino10.getCostos()[indice] + vecino11.getCostos()[indice] + vecino12.getCostos()[indice] + vecino13.getCostos()[indice];
                                                                    NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 13);
                                                                    listaNodosSeleccionados.add(Ns);
                                                                }
                                                            } catch (Exception ex) {
                                                                System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino13);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 13 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        for (Vecino vecino9 : BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getVecinos()) {
                                            for (Vecino vecino10 : BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo).getVecinos()) {
                                                for (Vecino vecino11 : BusquedaIterativa.obtieneNodoVecino(vecino10, Grafo).getVecinos()) {
                                                    for (Vecino vecino12 : BusquedaIterativa.obtieneNodoVecino(vecino11, Grafo).getVecinos()) {
                                                        for (Vecino vecino13 : BusquedaIterativa.obtieneNodoVecino(vecino12, Grafo).getVecinos()) {
                                                            for (Vecino vecino14 : BusquedaIterativa.obtieneNodoVecino(vecino13, Grafo).getVecinos()) {
                                                                try {
                                                                    if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino14, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino14, Grafo).getInd_obligatorio().equals("S")) {
                                                                        int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice] + vecino9.getCostos()[indice] + vecino10.getCostos()[indice] + vecino11.getCostos()[indice] + vecino12.getCostos()[indice] + vecino13.getCostos()[indice] + vecino14.getCostos()[indice];
                                                                        NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 14);
                                                                        listaNodosSeleccionados.add(Ns);
                                                                    }
                                                                } catch (Exception ex) {
                                                                    System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino14);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 14 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        for (Vecino vecino9 : BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getVecinos()) {
                                            for (Vecino vecino10 : BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo).getVecinos()) {
                                                for (Vecino vecino11 : BusquedaIterativa.obtieneNodoVecino(vecino10, Grafo).getVecinos()) {
                                                    for (Vecino vecino12 : BusquedaIterativa.obtieneNodoVecino(vecino11, Grafo).getVecinos()) {
                                                        for (Vecino vecino13 : BusquedaIterativa.obtieneNodoVecino(vecino12, Grafo).getVecinos()) {
                                                            for (Vecino vecino14 : BusquedaIterativa.obtieneNodoVecino(vecino13, Grafo).getVecinos()) {
                                                                for (Vecino vecino15 : BusquedaIterativa.obtieneNodoVecino(vecino14, Grafo).getVecinos()) {
                                                                    try {
                                                                        if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino15, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino15, Grafo).getInd_obligatorio().equals("S")) {
                                                                            int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice] + vecino9.getCostos()[indice] + vecino10.getCostos()[indice] + vecino11.getCostos()[indice] + vecino12.getCostos()[indice] + vecino13.getCostos()[indice] + vecino14.getCostos()[indice] + vecino15.getCostos()[indice];
                                                                            NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 15);
                                                                            listaNodosSeleccionados.add(Ns);
                                                                        }
                                                                    } catch (Exception ex) {
                                                                        System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino15);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 15 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        for (Vecino vecino9 : BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getVecinos()) {
                                            for (Vecino vecino10 : BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo).getVecinos()) {
                                                for (Vecino vecino11 : BusquedaIterativa.obtieneNodoVecino(vecino10, Grafo).getVecinos()) {
                                                    for (Vecino vecino12 : BusquedaIterativa.obtieneNodoVecino(vecino11, Grafo).getVecinos()) {
                                                        for (Vecino vecino13 : BusquedaIterativa.obtieneNodoVecino(vecino12, Grafo).getVecinos()) {
                                                            for (Vecino vecino14 : BusquedaIterativa.obtieneNodoVecino(vecino13, Grafo).getVecinos()) {
                                                                for (Vecino vecino15 : BusquedaIterativa.obtieneNodoVecino(vecino14, Grafo).getVecinos()) {
                                                                    for (Vecino vecino16 : BusquedaIterativa.obtieneNodoVecino(vecino15, Grafo).getVecinos()) {
                                                                        try {
                                                                            if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino16, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino16, Grafo).getInd_obligatorio().equals("S")) {
                                                                                int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice] + vecino9.getCostos()[indice] + vecino10.getCostos()[indice] + vecino11.getCostos()[indice] + vecino12.getCostos()[indice] + vecino13.getCostos()[indice] + vecino14.getCostos()[indice] + vecino15.getCostos()[indice] + vecino16.getCostos()[indice];
                                                                                NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 16);
                                                                                listaNodosSeleccionados.add(Ns);
                                                                            }
                                                                        } catch (Exception ex) {
                                                                            System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino16);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 16 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        for (Vecino vecino9 : BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getVecinos()) {
                                            for (Vecino vecino10 : BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo).getVecinos()) {
                                                for (Vecino vecino11 : BusquedaIterativa.obtieneNodoVecino(vecino10, Grafo).getVecinos()) {
                                                    for (Vecino vecino12 : BusquedaIterativa.obtieneNodoVecino(vecino11, Grafo).getVecinos()) {
                                                        for (Vecino vecino13 : BusquedaIterativa.obtieneNodoVecino(vecino12, Grafo).getVecinos()) {
                                                            for (Vecino vecino14 : BusquedaIterativa.obtieneNodoVecino(vecino13, Grafo).getVecinos()) {
                                                                for (Vecino vecino15 : BusquedaIterativa.obtieneNodoVecino(vecino14, Grafo).getVecinos()) {
                                                                    for (Vecino vecino16 : BusquedaIterativa.obtieneNodoVecino(vecino15, Grafo).getVecinos()) {
                                                                        for (Vecino vecino17 : BusquedaIterativa.obtieneNodoVecino(vecino16, Grafo).getVecinos()) {
                                                                            try {
                                                                                if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino17, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino17, Grafo).getInd_obligatorio().equals("S")) {
                                                                                    int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice] + vecino9.getCostos()[indice] + vecino10.getCostos()[indice] + vecino11.getCostos()[indice] + vecino12.getCostos()[indice] + vecino13.getCostos()[indice] + vecino14.getCostos()[indice] + vecino15.getCostos()[indice] + vecino16.getCostos()[indice] + vecino17.getCostos()[indice];
                                                                                    NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 17);
                                                                                    listaNodosSeleccionados.add(Ns);
                                                                                }
                                                                            } catch (Exception ex) {
                                                                                System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino17);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 17 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        for (Vecino vecino9 : BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getVecinos()) {
                                            for (Vecino vecino10 : BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo).getVecinos()) {
                                                for (Vecino vecino11 : BusquedaIterativa.obtieneNodoVecino(vecino10, Grafo).getVecinos()) {
                                                    for (Vecino vecino12 : BusquedaIterativa.obtieneNodoVecino(vecino11, Grafo).getVecinos()) {
                                                        for (Vecino vecino13 : BusquedaIterativa.obtieneNodoVecino(vecino12, Grafo).getVecinos()) {
                                                            for (Vecino vecino14 : BusquedaIterativa.obtieneNodoVecino(vecino13, Grafo).getVecinos()) {
                                                                for (Vecino vecino15 : BusquedaIterativa.obtieneNodoVecino(vecino14, Grafo).getVecinos()) {
                                                                    for (Vecino vecino16 : BusquedaIterativa.obtieneNodoVecino(vecino15, Grafo).getVecinos()) {
                                                                        for (Vecino vecino17 : BusquedaIterativa.obtieneNodoVecino(vecino16, Grafo).getVecinos()) {
                                                                            for (Vecino vecino18 : BusquedaIterativa.obtieneNodoVecino(vecino17, Grafo).getVecinos()) {
                                                                                try {
                                                                                    if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino18, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino18, Grafo).getInd_obligatorio().equals("S")) {
                                                                                        int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice] + vecino9.getCostos()[indice] + vecino10.getCostos()[indice] + vecino11.getCostos()[indice] + vecino12.getCostos()[indice] + vecino13.getCostos()[indice] + vecino14.getCostos()[indice] + vecino15.getCostos()[indice] + vecino16.getCostos()[indice] + vecino17.getCostos()[indice] + vecino18.getCostos()[indice];
                                                                                        NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 18);
                                                                                        listaNodosSeleccionados.add(Ns);
                                                                                    }
                                                                                } catch (Exception ex) {
                                                                                    System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino18);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 18 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        for (Vecino vecino9 : BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getVecinos()) {
                                            for (Vecino vecino10 : BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo).getVecinos()) {
                                                for (Vecino vecino11 : BusquedaIterativa.obtieneNodoVecino(vecino10, Grafo).getVecinos()) {
                                                    for (Vecino vecino12 : BusquedaIterativa.obtieneNodoVecino(vecino11, Grafo).getVecinos()) {
                                                        for (Vecino vecino13 : BusquedaIterativa.obtieneNodoVecino(vecino12, Grafo).getVecinos()) {
                                                            for (Vecino vecino14 : BusquedaIterativa.obtieneNodoVecino(vecino13, Grafo).getVecinos()) {
                                                                for (Vecino vecino15 : BusquedaIterativa.obtieneNodoVecino(vecino14, Grafo).getVecinos()) {
                                                                    for (Vecino vecino16 : BusquedaIterativa.obtieneNodoVecino(vecino15, Grafo).getVecinos()) {
                                                                        for (Vecino vecino17 : BusquedaIterativa.obtieneNodoVecino(vecino16, Grafo).getVecinos()) {
                                                                            for (Vecino vecino18 : BusquedaIterativa.obtieneNodoVecino(vecino17, Grafo).getVecinos()) {
                                                                                for (Vecino vecino19 : BusquedaIterativa.obtieneNodoVecino(vecino18, Grafo).getVecinos()) {
                                                                                    try {
                                                                                        if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino19, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino19, Grafo).getInd_obligatorio().equals("S")) {
                                                                                            int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice] + vecino9.getCostos()[indice] + vecino10.getCostos()[indice] + vecino11.getCostos()[indice] + vecino12.getCostos()[indice] + vecino13.getCostos()[indice] + vecino14.getCostos()[indice] + vecino15.getCostos()[indice] + vecino16.getCostos()[indice] + vecino17.getCostos()[indice] + vecino18.getCostos()[indice] + vecino19.getCostos()[indice];
                                                                                            NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 19);
                                                                                            listaNodosSeleccionados.add(Ns);
                                                                                        }
                                                                                    } catch (Exception ex) {
                                                                                        System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino19);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 19 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        for (Vecino vecino9 : BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getVecinos()) {
                                            for (Vecino vecino10 : BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo).getVecinos()) {
                                                for (Vecino vecino11 : BusquedaIterativa.obtieneNodoVecino(vecino10, Grafo).getVecinos()) {
                                                    for (Vecino vecino12 : BusquedaIterativa.obtieneNodoVecino(vecino11, Grafo).getVecinos()) {
                                                        for (Vecino vecino13 : BusquedaIterativa.obtieneNodoVecino(vecino12, Grafo).getVecinos()) {
                                                            for (Vecino vecino14 : BusquedaIterativa.obtieneNodoVecino(vecino13, Grafo).getVecinos()) {
                                                                for (Vecino vecino15 : BusquedaIterativa.obtieneNodoVecino(vecino14, Grafo).getVecinos()) {
                                                                    for (Vecino vecino16 : BusquedaIterativa.obtieneNodoVecino(vecino15, Grafo).getVecinos()) {
                                                                        for (Vecino vecino17 : BusquedaIterativa.obtieneNodoVecino(vecino16, Grafo).getVecinos()) {
                                                                            for (Vecino vecino18 : BusquedaIterativa.obtieneNodoVecino(vecino17, Grafo).getVecinos()) {
                                                                                for (Vecino vecino19 : BusquedaIterativa.obtieneNodoVecino(vecino18, Grafo).getVecinos()) {
                                                                                    for (Vecino vecino20 : BusquedaIterativa.obtieneNodoVecino(vecino19, Grafo).getVecinos()) {
                                                                                        try {
                                                                                            if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino20, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino20, Grafo).getInd_obligatorio().equals("S")) {
                                                                                                int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice] + vecino9.getCostos()[indice] + vecino10.getCostos()[indice] + vecino11.getCostos()[indice] + vecino12.getCostos()[indice] + vecino13.getCostos()[indice] + vecino14.getCostos()[indice] + vecino15.getCostos()[indice] + vecino16.getCostos()[indice] + vecino17.getCostos()[indice] + vecino18.getCostos()[indice] + vecino19.getCostos()[indice] + vecino20.getCostos()[indice];
                                                                                                NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 20);
                                                                                                listaNodosSeleccionados.add(Ns);
                                                                                            }
                                                                                        } catch (Exception ex) {
                                                                                            System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino20);
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 20 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        for (Vecino vecino9 : BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getVecinos()) {
                                            for (Vecino vecino10 : BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo).getVecinos()) {
                                                for (Vecino vecino11 : BusquedaIterativa.obtieneNodoVecino(vecino10, Grafo).getVecinos()) {
                                                    for (Vecino vecino12 : BusquedaIterativa.obtieneNodoVecino(vecino11, Grafo).getVecinos()) {
                                                        for (Vecino vecino13 : BusquedaIterativa.obtieneNodoVecino(vecino12, Grafo).getVecinos()) {
                                                            for (Vecino vecino14 : BusquedaIterativa.obtieneNodoVecino(vecino13, Grafo).getVecinos()) {
                                                                for (Vecino vecino15 : BusquedaIterativa.obtieneNodoVecino(vecino14, Grafo).getVecinos()) {
                                                                    for (Vecino vecino16 : BusquedaIterativa.obtieneNodoVecino(vecino15, Grafo).getVecinos()) {
                                                                        for (Vecino vecino17 : BusquedaIterativa.obtieneNodoVecino(vecino16, Grafo).getVecinos()) {
                                                                            for (Vecino vecino18 : BusquedaIterativa.obtieneNodoVecino(vecino17, Grafo).getVecinos()) {
                                                                                for (Vecino vecino19 : BusquedaIterativa.obtieneNodoVecino(vecino18, Grafo).getVecinos()) {
                                                                                    for (Vecino vecino20 : BusquedaIterativa.obtieneNodoVecino(vecino19, Grafo).getVecinos()) {
                                                                                        for (Vecino vecino21 : BusquedaIterativa.obtieneNodoVecino(vecino20, Grafo).getVecinos()) {
                                                                                            try {
                                                                                                if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino21, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino21, Grafo).getInd_obligatorio().equals("S")) {
                                                                                                    int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice] + vecino9.getCostos()[indice] + vecino10.getCostos()[indice] + vecino11.getCostos()[indice] + vecino12.getCostos()[indice] + vecino13.getCostos()[indice] + vecino14.getCostos()[indice] + vecino15.getCostos()[indice] + vecino16.getCostos()[indice] + vecino17.getCostos()[indice] + vecino18.getCostos()[indice] + vecino19.getCostos()[indice] + vecino20.getCostos()[indice] + vecino21.getCostos()[indice];
                                                                                                    NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 21);
                                                                                                    listaNodosSeleccionados.add(Ns);
                                                                                                }
                                                                                            } catch (Exception ex) {
                                                                                                System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino21);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 20 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        for (Vecino vecino9 : BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getVecinos()) {
                                            for (Vecino vecino10 : BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo).getVecinos()) {
                                                for (Vecino vecino11 : BusquedaIterativa.obtieneNodoVecino(vecino10, Grafo).getVecinos()) {
                                                    for (Vecino vecino12 : BusquedaIterativa.obtieneNodoVecino(vecino11, Grafo).getVecinos()) {
                                                        for (Vecino vecino13 : BusquedaIterativa.obtieneNodoVecino(vecino12, Grafo).getVecinos()) {
                                                            for (Vecino vecino14 : BusquedaIterativa.obtieneNodoVecino(vecino13, Grafo).getVecinos()) {
                                                                for (Vecino vecino15 : BusquedaIterativa.obtieneNodoVecino(vecino14, Grafo).getVecinos()) {
                                                                    for (Vecino vecino16 : BusquedaIterativa.obtieneNodoVecino(vecino15, Grafo).getVecinos()) {
                                                                        for (Vecino vecino17 : BusquedaIterativa.obtieneNodoVecino(vecino16, Grafo).getVecinos()) {
                                                                            for (Vecino vecino18 : BusquedaIterativa.obtieneNodoVecino(vecino17, Grafo).getVecinos()) {
                                                                                for (Vecino vecino19 : BusquedaIterativa.obtieneNodoVecino(vecino18, Grafo).getVecinos()) {
                                                                                    for (Vecino vecino20 : BusquedaIterativa.obtieneNodoVecino(vecino19, Grafo).getVecinos()) {
                                                                                        for (Vecino vecino21 : BusquedaIterativa.obtieneNodoVecino(vecino20, Grafo).getVecinos()) {
                                                                                            for (Vecino vecino22 : BusquedaIterativa.obtieneNodoVecino(vecino21, Grafo).getVecinos()) {
                                                                                                try {
                                                                                                    if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino22, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino22, Grafo).getInd_obligatorio().equals("S")) {
                                                                                                        int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice] + vecino9.getCostos()[indice] + vecino10.getCostos()[indice] + vecino11.getCostos()[indice] + vecino12.getCostos()[indice] + vecino13.getCostos()[indice] + vecino14.getCostos()[indice] + vecino15.getCostos()[indice] + vecino16.getCostos()[indice] + vecino17.getCostos()[indice] + vecino18.getCostos()[indice] + vecino19.getCostos()[indice] + vecino20.getCostos()[indice] + vecino21.getCostos()[indice] + vecino22.getCostos()[indice];
                                                                                                        NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 22);
                                                                                                        listaNodosSeleccionados.add(Ns);
                                                                                                    }
                                                                                                } catch (Exception ex) {
                                                                                                    System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino22);
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 20 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        for (Vecino vecino9 : BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getVecinos()) {
                                            for (Vecino vecino10 : BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo).getVecinos()) {
                                                for (Vecino vecino11 : BusquedaIterativa.obtieneNodoVecino(vecino10, Grafo).getVecinos()) {
                                                    for (Vecino vecino12 : BusquedaIterativa.obtieneNodoVecino(vecino11, Grafo).getVecinos()) {
                                                        for (Vecino vecino13 : BusquedaIterativa.obtieneNodoVecino(vecino12, Grafo).getVecinos()) {
                                                            for (Vecino vecino14 : BusquedaIterativa.obtieneNodoVecino(vecino13, Grafo).getVecinos()) {
                                                                for (Vecino vecino15 : BusquedaIterativa.obtieneNodoVecino(vecino14, Grafo).getVecinos()) {
                                                                    for (Vecino vecino16 : BusquedaIterativa.obtieneNodoVecino(vecino15, Grafo).getVecinos()) {
                                                                        for (Vecino vecino17 : BusquedaIterativa.obtieneNodoVecino(vecino16, Grafo).getVecinos()) {
                                                                            for (Vecino vecino18 : BusquedaIterativa.obtieneNodoVecino(vecino17, Grafo).getVecinos()) {
                                                                                for (Vecino vecino19 : BusquedaIterativa.obtieneNodoVecino(vecino18, Grafo).getVecinos()) {
                                                                                    for (Vecino vecino20 : BusquedaIterativa.obtieneNodoVecino(vecino19, Grafo).getVecinos()) {
                                                                                        for (Vecino vecino21 : BusquedaIterativa.obtieneNodoVecino(vecino20, Grafo).getVecinos()) {
                                                                                            for (Vecino vecino22 : BusquedaIterativa.obtieneNodoVecino(vecino21, Grafo).getVecinos()) {
                                                                                                for (Vecino vecino23 : BusquedaIterativa.obtieneNodoVecino(vecino22, Grafo).getVecinos()) {
                                                                                                    try {
                                                                                                        if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino23, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino23, Grafo).getInd_obligatorio().equals("S")) {
                                                                                                            int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice] + vecino9.getCostos()[indice] + vecino10.getCostos()[indice] + vecino11.getCostos()[indice] + vecino12.getCostos()[indice] + vecino13.getCostos()[indice] + vecino14.getCostos()[indice] + vecino15.getCostos()[indice] + vecino16.getCostos()[indice] + vecino17.getCostos()[indice] + vecino18.getCostos()[indice] + vecino19.getCostos()[indice] + vecino20.getCostos()[indice] + vecino21.getCostos()[indice] + vecino22.getCostos()[indice] + vecino23.getCostos()[indice];
                                                                                                            NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 23);
                                                                                                            listaNodosSeleccionados.add(Ns);
                                                                                                        }
                                                                                                    } catch (Exception ex) {
                                                                                                        System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino23);
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 20 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        for (Vecino vecino9 : BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getVecinos()) {
                                            for (Vecino vecino10 : BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo).getVecinos()) {
                                                for (Vecino vecino11 : BusquedaIterativa.obtieneNodoVecino(vecino10, Grafo).getVecinos()) {
                                                    for (Vecino vecino12 : BusquedaIterativa.obtieneNodoVecino(vecino11, Grafo).getVecinos()) {
                                                        for (Vecino vecino13 : BusquedaIterativa.obtieneNodoVecino(vecino12, Grafo).getVecinos()) {
                                                            for (Vecino vecino14 : BusquedaIterativa.obtieneNodoVecino(vecino13, Grafo).getVecinos()) {
                                                                for (Vecino vecino15 : BusquedaIterativa.obtieneNodoVecino(vecino14, Grafo).getVecinos()) {
                                                                    for (Vecino vecino16 : BusquedaIterativa.obtieneNodoVecino(vecino15, Grafo).getVecinos()) {
                                                                        for (Vecino vecino17 : BusquedaIterativa.obtieneNodoVecino(vecino16, Grafo).getVecinos()) {
                                                                            for (Vecino vecino18 : BusquedaIterativa.obtieneNodoVecino(vecino17, Grafo).getVecinos()) {
                                                                                for (Vecino vecino19 : BusquedaIterativa.obtieneNodoVecino(vecino18, Grafo).getVecinos()) {
                                                                                    for (Vecino vecino20 : BusquedaIterativa.obtieneNodoVecino(vecino19, Grafo).getVecinos()) {
                                                                                        for (Vecino vecino21 : BusquedaIterativa.obtieneNodoVecino(vecino20, Grafo).getVecinos()) {
                                                                                            for (Vecino vecino22 : BusquedaIterativa.obtieneNodoVecino(vecino21, Grafo).getVecinos()) {
                                                                                                for (Vecino vecino23 : BusquedaIterativa.obtieneNodoVecino(vecino22, Grafo).getVecinos()) {
                                                                                                    for (Vecino vecino24 : BusquedaIterativa.obtieneNodoVecino(vecino23, Grafo).getVecinos()) {
                                                                                                        try {
                                                                                                            if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino24, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino24, Grafo).getInd_obligatorio().equals("S")) {
                                                                                                                int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice] + vecino9.getCostos()[indice] + vecino10.getCostos()[indice] + vecino11.getCostos()[indice] + vecino12.getCostos()[indice] + vecino13.getCostos()[indice] + vecino14.getCostos()[indice] + vecino15.getCostos()[indice] + vecino16.getCostos()[indice] + vecino17.getCostos()[indice] + vecino18.getCostos()[indice] + vecino19.getCostos()[indice] + vecino20.getCostos()[indice] + vecino21.getCostos()[indice] + vecino22.getCostos()[indice] + vecino23.getCostos()[indice] + vecino24.getCostos()[indice];
                                                                                                                NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 24);
                                                                                                                listaNodosSeleccionados.add(Ns);
                                                                                                            }
                                                                                                        } catch (Exception ex) {
                                                                                                            System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino24);
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 21 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        for (Vecino vecino9 : BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getVecinos()) {
                                            for (Vecino vecino10 : BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo).getVecinos()) {
                                                for (Vecino vecino11 : BusquedaIterativa.obtieneNodoVecino(vecino10, Grafo).getVecinos()) {
                                                    for (Vecino vecino12 : BusquedaIterativa.obtieneNodoVecino(vecino11, Grafo).getVecinos()) {
                                                        for (Vecino vecino13 : BusquedaIterativa.obtieneNodoVecino(vecino12, Grafo).getVecinos()) {
                                                            for (Vecino vecino14 : BusquedaIterativa.obtieneNodoVecino(vecino13, Grafo).getVecinos()) {
                                                                for (Vecino vecino15 : BusquedaIterativa.obtieneNodoVecino(vecino14, Grafo).getVecinos()) {
                                                                    for (Vecino vecino16 : BusquedaIterativa.obtieneNodoVecino(vecino15, Grafo).getVecinos()) {
                                                                        for (Vecino vecino17 : BusquedaIterativa.obtieneNodoVecino(vecino16, Grafo).getVecinos()) {
                                                                            for (Vecino vecino18 : BusquedaIterativa.obtieneNodoVecino(vecino17, Grafo).getVecinos()) {
                                                                                for (Vecino vecino19 : BusquedaIterativa.obtieneNodoVecino(vecino18, Grafo).getVecinos()) {
                                                                                    for (Vecino vecino20 : BusquedaIterativa.obtieneNodoVecino(vecino19, Grafo).getVecinos()) {
                                                                                        for (Vecino vecino21 : BusquedaIterativa.obtieneNodoVecino(vecino20, Grafo).getVecinos()) {
                                                                                            for (Vecino vecino22 : BusquedaIterativa.obtieneNodoVecino(vecino21, Grafo).getVecinos()) {
                                                                                                for (Vecino vecino23 : BusquedaIterativa.obtieneNodoVecino(vecino22, Grafo).getVecinos()) {
                                                                                                    for (Vecino vecino24 : BusquedaIterativa.obtieneNodoVecino(vecino23, Grafo).getVecinos()) {
                                                                                                        for (Vecino vecino25 : BusquedaIterativa.obtieneNodoVecino(vecino24, Grafo).getVecinos()) {
                                                                                                            try {
                                                                                                                if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino25, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino25, Grafo).getInd_obligatorio().equals("S")) {
                                                                                                                    int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice] + vecino9.getCostos()[indice] + vecino10.getCostos()[indice] + vecino11.getCostos()[indice] + vecino12.getCostos()[indice] + vecino13.getCostos()[indice] + vecino14.getCostos()[indice] + vecino15.getCostos()[indice] + vecino16.getCostos()[indice] + vecino17.getCostos()[indice] + vecino18.getCostos()[indice] + vecino19.getCostos()[indice] + vecino20.getCostos()[indice] + vecino21.getCostos()[indice] + vecino22.getCostos()[indice] + vecino23.getCostos()[indice] + vecino24.getCostos()[indice] + vecino25.getCostos()[indice];
                                                                                                                    NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 25);
                                                                                                                    listaNodosSeleccionados.add(Ns);
                                                                                                                }
                                                                                                            } catch (Exception ex) {
                                                                                                                System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino25);
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 22 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        for (Vecino vecino9 : BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getVecinos()) {
                                            for (Vecino vecino10 : BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo).getVecinos()) {
                                                for (Vecino vecino11 : BusquedaIterativa.obtieneNodoVecino(vecino10, Grafo).getVecinos()) {
                                                    for (Vecino vecino12 : BusquedaIterativa.obtieneNodoVecino(vecino11, Grafo).getVecinos()) {
                                                        for (Vecino vecino13 : BusquedaIterativa.obtieneNodoVecino(vecino12, Grafo).getVecinos()) {
                                                            for (Vecino vecino14 : BusquedaIterativa.obtieneNodoVecino(vecino13, Grafo).getVecinos()) {
                                                                for (Vecino vecino15 : BusquedaIterativa.obtieneNodoVecino(vecino14, Grafo).getVecinos()) {
                                                                    for (Vecino vecino16 : BusquedaIterativa.obtieneNodoVecino(vecino15, Grafo).getVecinos()) {
                                                                        for (Vecino vecino17 : BusquedaIterativa.obtieneNodoVecino(vecino16, Grafo).getVecinos()) {
                                                                            for (Vecino vecino18 : BusquedaIterativa.obtieneNodoVecino(vecino17, Grafo).getVecinos()) {
                                                                                for (Vecino vecino19 : BusquedaIterativa.obtieneNodoVecino(vecino18, Grafo).getVecinos()) {
                                                                                    for (Vecino vecino20 : BusquedaIterativa.obtieneNodoVecino(vecino19, Grafo).getVecinos()) {
                                                                                        for (Vecino vecino21 : BusquedaIterativa.obtieneNodoVecino(vecino20, Grafo).getVecinos()) {
                                                                                            for (Vecino vecino22 : BusquedaIterativa.obtieneNodoVecino(vecino21, Grafo).getVecinos()) {
                                                                                                for (Vecino vecino23 : BusquedaIterativa.obtieneNodoVecino(vecino22, Grafo).getVecinos()) {
                                                                                                    for (Vecino vecino24 : BusquedaIterativa.obtieneNodoVecino(vecino23, Grafo).getVecinos()) {
                                                                                                        for (Vecino vecino25 : BusquedaIterativa.obtieneNodoVecino(vecino24, Grafo).getVecinos()) {
                                                                                                            for (Vecino vecino26 : BusquedaIterativa.obtieneNodoVecino(vecino25, Grafo).getVecinos()) {
                                                                                                                try {
                                                                                                                    if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino26, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino26, Grafo).getInd_obligatorio().equals("S")) {
                                                                                                                        int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice] + vecino9.getCostos()[indice] + vecino10.getCostos()[indice] + vecino11.getCostos()[indice] + vecino12.getCostos()[indice] + vecino13.getCostos()[indice] + vecino14.getCostos()[indice] + vecino15.getCostos()[indice] + vecino16.getCostos()[indice] + vecino17.getCostos()[indice] + vecino18.getCostos()[indice] + vecino19.getCostos()[indice] + vecino20.getCostos()[indice] + vecino21.getCostos()[indice] + vecino22.getCostos()[indice] + vecino23.getCostos()[indice] + vecino24.getCostos()[indice] + vecino25.getCostos()[indice] + vecino26.getCostos()[indice];
                                                                                                                        NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 26);
                                                                                                                        listaNodosSeleccionados.add(Ns);
                                                                                                                    }
                                                                                                                } catch (Exception ex) {
                                                                                                                    System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino26);
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Profundidad 22 NodoActual-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos-Vecinos
        for (Vecino vecino : nodoActual.getVecinos()) {
            Nodo nodoVecino = BusquedaIterativa.obtieneNodoVecino(vecino, Grafo);
            for (Vecino vecino2 : nodoVecino.getVecinos()) {
                for (Vecino vecino3 : BusquedaIterativa.obtieneNodoVecino(vecino2, Grafo).getVecinos()) {
                    for (Vecino vecino4 : BusquedaIterativa.obtieneNodoVecino(vecino3, Grafo).getVecinos()) {
                        for (Vecino vecino5 : BusquedaIterativa.obtieneNodoVecino(vecino4, Grafo).getVecinos()) {
                            for (Vecino vecino6 : BusquedaIterativa.obtieneNodoVecino(vecino5, Grafo).getVecinos()) {
                                for (Vecino vecino7 : BusquedaIterativa.obtieneNodoVecino(vecino6, Grafo).getVecinos()) {
                                    for (Vecino vecino8 : BusquedaIterativa.obtieneNodoVecino(vecino7, Grafo).getVecinos()) {
                                        for (Vecino vecino9 : BusquedaIterativa.obtieneNodoVecino(vecino8, Grafo).getVecinos()) {
                                            for (Vecino vecino10 : BusquedaIterativa.obtieneNodoVecino(vecino9, Grafo).getVecinos()) {
                                                for (Vecino vecino11 : BusquedaIterativa.obtieneNodoVecino(vecino10, Grafo).getVecinos()) {
                                                    for (Vecino vecino12 : BusquedaIterativa.obtieneNodoVecino(vecino11, Grafo).getVecinos()) {
                                                        for (Vecino vecino13 : BusquedaIterativa.obtieneNodoVecino(vecino12, Grafo).getVecinos()) {
                                                            for (Vecino vecino14 : BusquedaIterativa.obtieneNodoVecino(vecino13, Grafo).getVecinos()) {
                                                                for (Vecino vecino15 : BusquedaIterativa.obtieneNodoVecino(vecino14, Grafo).getVecinos()) {
                                                                    for (Vecino vecino16 : BusquedaIterativa.obtieneNodoVecino(vecino15, Grafo).getVecinos()) {
                                                                        for (Vecino vecino17 : BusquedaIterativa.obtieneNodoVecino(vecino16, Grafo).getVecinos()) {
                                                                            for (Vecino vecino18 : BusquedaIterativa.obtieneNodoVecino(vecino17, Grafo).getVecinos()) {
                                                                                for (Vecino vecino19 : BusquedaIterativa.obtieneNodoVecino(vecino18, Grafo).getVecinos()) {
                                                                                    for (Vecino vecino20 : BusquedaIterativa.obtieneNodoVecino(vecino19, Grafo).getVecinos()) {
                                                                                        for (Vecino vecino21 : BusquedaIterativa.obtieneNodoVecino(vecino20, Grafo).getVecinos()) {
                                                                                            for (Vecino vecino22 : BusquedaIterativa.obtieneNodoVecino(vecino21, Grafo).getVecinos()) {
                                                                                                for (Vecino vecino23 : BusquedaIterativa.obtieneNodoVecino(vecino22, Grafo).getVecinos()) {
                                                                                                    for (Vecino vecino24 : BusquedaIterativa.obtieneNodoVecino(vecino23, Grafo).getVecinos()) {
                                                                                                        for (Vecino vecino25 : BusquedaIterativa.obtieneNodoVecino(vecino24, Grafo).getVecinos()) {
                                                                                                            for (Vecino vecino26 : BusquedaIterativa.obtieneNodoVecino(vecino25, Grafo).getVecinos()) {
                                                                                                                for (Vecino vecino27 : BusquedaIterativa.obtieneNodoVecino(vecino26, Grafo).getVecinos()) {
                                                                                                                    try {
                                                                                                                        if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(vecino27, Grafo)) && BusquedaIterativa.obtieneNodoVecino(vecino27, Grafo).getInd_obligatorio().equals("S")) {
                                                                                                                            int costoActual = vecino.getCostos()[indice] + vecino2.getCostos()[indice] + vecino3.getCostos()[indice] + vecino4.getCostos()[indice] + vecino5.getCostos()[indice] + vecino6.getCostos()[indice] + vecino7.getCostos()[indice] + vecino8.getCostos()[indice] + vecino9.getCostos()[indice] + vecino10.getCostos()[indice] + vecino11.getCostos()[indice] + vecino12.getCostos()[indice] + vecino13.getCostos()[indice] + vecino14.getCostos()[indice] + vecino15.getCostos()[indice] + vecino16.getCostos()[indice] + vecino17.getCostos()[indice] + vecino18.getCostos()[indice] + vecino19.getCostos()[indice] + vecino20.getCostos()[indice] + vecino21.getCostos()[indice] + vecino22.getCostos()[indice] + vecino23.getCostos()[indice] + vecino24.getCostos()[indice] + vecino25.getCostos()[indice] + vecino26.getCostos()[indice] + vecino27.getCostos()[indice];
                                                                                                                            NodoSeleccionado Ns = new NodoSeleccionado(vecino, costoActual, 27);
                                                                                                                            listaNodosSeleccionados.add(Ns);
                                                                                                                        }
                                                                                                                    } catch (Exception ex) {
                                                                                                                        System.out.println("Ocurrió un error en la búsqueda en profundidad con el nodo -> " + vecino27);
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (listaNodosSeleccionados.isEmpty()) {
            return null;
        } else {
            NodoSeleccionado nodoSeleccionado = null;
            for (NodoSeleccionado N : listaNodosSeleccionados) {
                if (nodoSeleccionado == null || N.getCosto() < nodoSeleccionado.getCosto()) {
                    nodoSeleccionado = N;
                }
            }
            if (nodoSeleccionado == null) {
                return null;
            } else {
                return nodoSeleccionado.getVecino();
            }
        }
    }
    
    //Lista de nodos vecinos sin visitar y lista de nodos recorridos
    public static Vecino seleccionaVecino(ArrayList<Vecino> vecinosLibres, ArrayList<Nodo> Grafo, ArrayList<Nodo> nodosRecorridos) {
        ArrayList<NodoSeleccionado> listaNodos = new ArrayList();
        for (Vecino V : vecinosLibres) {
            //NodoSeleccionado(Vecino, cantidad de vecinos libres, 0)
            NodoSeleccionado ns = new NodoSeleccionado(V, 0, 0);
            for (Vecino V1 : BusquedaIterativa.obtieneNodoVecino(V, Grafo).getVecinos()) {
                if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(V1, Grafo)) && BusquedaIterativa.obtieneNodoVecino(V1, Grafo).getInd_obligatorio().equals("S")) {
                    ns.setCosto(ns.getCosto() + 1);
                }
                
                for (Vecino V2 : BusquedaIterativa.obtieneNodoVecino(V1, Grafo).getVecinos()) {
                    if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(V2, Grafo)) && BusquedaIterativa.obtieneNodoVecino(V2, Grafo).getInd_obligatorio().equals("S")) {
                        ns.setCosto(ns.getCosto() + 1);
                    }                        
                    
                    for (Vecino V3 : BusquedaIterativa.obtieneNodoVecino(V2, Grafo).getVecinos()) {
                        if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(V3, Grafo)) && BusquedaIterativa.obtieneNodoVecino(V3, Grafo).getInd_obligatorio().equals("S")) {
                            ns.setCosto(ns.getCosto() + 1);
                        }   
                        
                        for (Vecino V4 : BusquedaIterativa.obtieneNodoVecino(V3, Grafo).getVecinos()) {
                            if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(V4, Grafo)) && BusquedaIterativa.obtieneNodoVecino(V4, Grafo).getInd_obligatorio().equals("S")) {
                                ns.setCosto(ns.getCosto() + 1);
                            }            
                            
                            for (Vecino V5 : BusquedaIterativa.obtieneNodoVecino(V4, Grafo).getVecinos()) {
                                if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(V5, Grafo)) && BusquedaIterativa.obtieneNodoVecino(V5, Grafo).getInd_obligatorio().equals("S")) {
                                    ns.setCosto(ns.getCosto() + 1);
                                }      
                                
                                for (Vecino V6 : BusquedaIterativa.obtieneNodoVecino(V5, Grafo).getVecinos()) {
                                    if (!nodosRecorridos.contains(BusquedaIterativa.obtieneNodoVecino(V6, Grafo)) && BusquedaIterativa.obtieneNodoVecino(V6, Grafo).getInd_obligatorio().equals("S")) {
                                        ns.setCosto(ns.getCosto() + 1);
                                    }                        

                                }
                            }
                        } 
                    }                    
                }
            }
            listaNodos.add(ns);
        }
        
        NodoSeleccionado nodoSeleccionado = listaNodos.get(0);
        for (NodoSeleccionado Ns : listaNodos) {
            if (Ns.getCosto() > nodoSeleccionado.getCosto()) {
                nodoSeleccionado = Ns;
            }
        }
        
        if (nodoSeleccionado != null) {
            return nodoSeleccionado.getVecino();
        } else {
            return null;
        }
    }
}