/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TresEnRayaB;

import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vicky
 */
public class HiloServidorTresEnRaya extends Thread implements Constantes {

    private DataOutputStream dataOut;
    private DataInputStream dataIn;
    private Socket jugador;
    private Reglas reglas;
    private Datos datos;
    private Gson gson;
    private int id;

    public HiloServidorTresEnRaya(int id, Socket jugador, DataOutputStream dataOut, Reglas reglas) {
        this.id = id;
        this.jugador = jugador;
        this.dataOut = dataOut;
        this.reglas = reglas;
    }

    public void run() {
        datos = new Datos();
        gson = new Gson();
        try {
            dataIn = new DataInputStream(jugador.getInputStream());
            int posicion = 0;
            boolean empate = false, gana = false;
            datos.setTurno(1);
            datos.setEstado(EN_PARTIDA);
            String json;
            while (true) {
                reglas.setToca(true);
                if (reglas.getTurno() == id) {
                    json = dataIn.readUTF();//lee letra
                    datos = gson.fromJson(json, Datos.class);
                    posicion = datos.getPosicion();
                }
                reglas.compruebaJugadaSynchronized(id, posicion);
                empate = reglas.compruebaEmpate();
                gana = reglas.compruebaGanador();
                if(empate){
                    datos.setEstado(FINALIZADO);
                    datos.setGanador('o');
                }
                if(gana){
                    datos.setEstado(FINALIZADO);
                    datos.setGanador(reglas.getGanador());
                }
                datos.setTableroLetras(reglas.getTableroLetras());
                datos.setTurno(reglas.getTurno());
                json = gson.toJson(datos);
                dataOut.writeUTF(json);//manda tablero
                
                if(reglas.getEstado() == FINALIZADO){
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(HiloServidorTresEnRaya.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                jugador.close();
                dataIn.close();
                dataOut.close();
            } catch (IOException ex) {
                Logger.getLogger(HiloServidorTresEnRaya.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
