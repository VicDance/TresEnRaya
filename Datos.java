/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TresEnRayaB;

/**
 *
 * @author Vicky
 */
public class Datos implements Constantes {

    private int estado;
    private int[][] tableroPos;
    private char[] tableroLetras;
    private int turno;
    private int posicion;
    private char ganador;

    public Datos() {
        this.estado = ESPERANDO;
        this.tableroPos = new int[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
        this.tableroLetras = new char[9];

    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int[][] getTableroPos() {
        return tableroPos;
    }

    public void setTableroPos(int[][] tableroPos) {
        this.tableroPos = tableroPos;
    }

    public char[] getTableroLetras() {
        return tableroLetras;
    }

    public void setTableroLetras(char[] tableroLetras) {
        this.tableroLetras = tableroLetras;
    }

    public char getGanador() {
        return ganador;
    }

    public void setGanador(char ganador) {
        this.ganador = ganador;
    }
}
