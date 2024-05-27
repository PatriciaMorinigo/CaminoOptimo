/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.util.ArrayList;

/**
 * Entidad donde se detallan todo los datos relacionados con el recorrido
 * @author Usuario
 */
public class DatosBusqueda {
    private ArrayList<Nodo> recorrido;
    private int costoTotal;
    private int segundoInicio;
    private int segundoFinal;    

    public DatosBusqueda(ArrayList<Nodo> recorrido, int costoTotal, int segundoInicio, int segundoFinal) {
        this.recorrido = recorrido;
        this.costoTotal = costoTotal;
        this.segundoInicio = segundoInicio;
        this.segundoFinal = segundoFinal;
    }

    public ArrayList<Nodo> getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(ArrayList<Nodo> recorrido) {
        this.recorrido = recorrido;
    }

    public int getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(int costoTotal) {
        this.costoTotal = costoTotal;
    }

    public int getSegundoInicio() {
        return segundoInicio;
    }

    public void setSegundoInicio(int segundoInicio) {
        this.segundoInicio = segundoInicio;
    }

    public int getSegundoFinal() {
        return segundoFinal;
    }

    public void setSegundoFinal(int segundoFinal) {
        this.segundoFinal = segundoFinal;
    }
}
