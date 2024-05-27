/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

/**
 *
 * @author PC
 */
public class NodoSeleccionado {
    private Vecino vecino;
    private int costo;
    //private int profundidad;

    public NodoSeleccionado(Vecino vecino, int costo, int profundidad) {
        this.vecino = vecino;
        this.costo = costo;
        //this.profundidad = profundidad;
    }

    public NodoSeleccionado() {
        
    }

    public Vecino getVecino() {
        return vecino;
    }

    public void setVecino(Vecino vecino) {
        this.vecino = vecino;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

   /* public int getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(int profundidad) {
        this.profundidad = profundidad;
    }*/
}
