/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.util.ArrayList;

/**
 * Entidad que hace referencia a un nodo dentro del grafo
 * @author Usuario
 */
public class Nodo {
    private String nodo;
    private String ind_obligatorio;
    private ArrayList<Vecino> vecinos = new ArrayList();

    public String getNodo() {
        return nodo;
    }

    public void setNodo(String nodo) {
        this.nodo = nodo;
    }

    public String getInd_obligatorio() {
        return ind_obligatorio;
    }

    public void setInd_obligatorio(String ind_obligatorio) {
        this.ind_obligatorio = ind_obligatorio;
    }

    public ArrayList<Vecino> getVecinos() {
        return vecinos;
    }

    public void setVecinos(ArrayList<Vecino> vecinos) {
        this.vecinos = vecinos;
    }
    
    public void addVecinos(Vecino v) {
        this.vecinos.add(v);
    }    
}
