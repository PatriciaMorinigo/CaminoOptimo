/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

/**
 * Entidad que hace referencia a un vecino de un nodo
 * @author Usuario
 */
public class Vecino {
    String nodo;
    Integer[] costos;

    public String getNodo() {
        return nodo;
    }

    public void setNodo(String nodo) {
        this.nodo = nodo;
    }

    public Integer[] getCostos() {
        return costos;
    }

    public void setCostos(Integer[] costos) {
        this.costos = costos;
    }
    
    public void setCosto(int indice, int costo) {
        this.costos[indice] = costo;
    }
}
