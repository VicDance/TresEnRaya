/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TresEnRayaB;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vicky
 */
public class Reglas implements Constantes {

    private char[] tableroLetras;
    private int[][] tableroPos;
    private int estado;
    private int contCasillas;
    private char ganador;
    private int turno;
    private boolean toca;
    //private Datos datos;

    public Reglas() {
        this.estado = ESPERANDO;
        this.tableroPos = new int[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
        this.tableroLetras = new char[]{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        this.turno = 1;
        this.toca = true;
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

    public char getGanador() {
        return ganador;
    }

    public void setGanador(char ganador) {
        this.ganador = ganador;
    }

    public char[] getTableroLetras() {
        return tableroLetras;
    }

    public void setTableroLetras(char[] tableroLetras) {
        this.tableroLetras = tableroLetras;
    }

    public void setToca(boolean toca) {
        this.toca = toca;
    }

    public synchronized void compruebaJugadaSynchronized(int id, int posicion) {
        while (id != turno) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Reglas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        while (toca) {
            if (id == 1 && tableroLetras[posicion] == ' ') {
                tableroLetras[posicion] = 'X';
                contCasillas++;
            } else if (id == 2 && tableroLetras[posicion] == ' ') {
                tableroLetras[posicion] = 'O';
                contCasillas++;
            }
            turno++;
            if (turno > 2) {
                turno = 1;
            }
            toca = false;
            notify();
        }
    }

    public boolean compruebaGanador() {
        boolean gana = false;

        if (turno == 1) {
            if ((tableroLetras[0] == 'X' && tableroLetras[1] == 'X' && tableroLetras[2] == 'X')
                    || (tableroLetras[3] == 'X' && tableroLetras[4] == 'X' && tableroLetras[5] == 'X') || (tableroLetras[6] == 'X' && tableroLetras[7] == 'X' && tableroLetras[8] == 'X')
                    || (tableroLetras[0] == 'X' && tableroLetras[3] == 'X' && tableroLetras[6] == 'X') || (tableroLetras[1] == 'X' && tableroLetras[4] == 'X' && tableroLetras[7] == 'X')
                    || (tableroLetras[2] == 'X' && tableroLetras[5] == 'X' && tableroLetras[8] == 'X') || (tableroLetras[0] == 'X' && tableroLetras[4] == 'X' && tableroLetras[8] == 'X')
                    || (tableroLetras[6] == 'X' && tableroLetras[6] == 'X' && tableroLetras[2] == 'X')) {

                gana = true;
                ganador = JUGADORX;
                estado = FINALIZADO;
            }
        } else if (turno == 2) {
            if ((tableroLetras[0] == 'O' && tableroLetras[1] == 'O' && tableroLetras[2] == 'O')
                    || (tableroLetras[3] == 'O' && tableroLetras[4] == 'O' && tableroLetras[5] == 'O') || (tableroLetras[6] == 'O' && tableroLetras[7] == 'O' && tableroLetras[8] == 'O')
                    || (tableroLetras[0] == 'O' && tableroLetras[3] == 'O' && tableroLetras[6] == 'O') || (tableroLetras[1] == 'O' && tableroLetras[4] == 'O' && tableroLetras[7] == 'O')
                    || (tableroLetras[2] == 'O' && tableroLetras[5] == 'O' && tableroLetras[8] == 'O') || (tableroLetras[0] == 'O' && tableroLetras[4] == 'O' && tableroLetras[8] == 'O')
                    || (tableroLetras[6] == 'O' && tableroLetras[6] == 'O' && tableroLetras[2] == 'O')) {

                gana = true;
                ganador = JUGADORO;
                estado = FINALIZADO;
            }
        }

        return gana;
    }

    public boolean compruebaEmpate() {
        boolean empate = false;
        if (/*turno == 1 && */!compruebaGanador() && contCasillas == 9) {
            empate = true;
            estado = FINALIZADO;
            /*} else if (turno == 2 && !compruebaGanador() && contCasillas == 9) {
             empate = true;
             estado = FINALIZADO;
             }*/

        }
        return empate;
    }
}
