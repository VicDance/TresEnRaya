/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TresEnRayaB;

import com.google.gson.Gson;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vicky
 */
public class ServidorTresEnRaya implements Constantes {

    public static void main(String[] args) {
        new ServidorTresEnRaya().listen();
    }

    private void listen() {
        final int PUERTO = 6000;
        ServerSocket servidor = null;
        try {
            servidor = new ServerSocket(PUERTO);
            System.out.println("Conectado al servidor: " + servidor);
        } catch (IOException ex) {
            System.out.println("IMPOSIBLE CONECTAR CON EL SERVIDOR " + servidor);
            System.exit(-1);
        }

        Socket jugadorX = null, jugadorO = null;
        DataOutputStream dataOutX = null;
        DataOutputStream dataOutO = null;
        Datos datos;
        Gson gson;
        Reglas reglas;

        int contPartida = 1;

        System.out.println("Servidor Tres en Raya prestando servicio...");
        reglas = new Reglas();
        try {
            while (true) {
                System.out.println("Esperando jugadores para la partida " + contPartida);
                jugadorX = servidor.accept();
                System.out.println("Jugador " + JUGADORX + " conectado a la partida " + contPartida);
                dataOutX = new DataOutputStream(jugadorX.getOutputStream());
                datos = new Datos();
                datos.setTurno(1);
                gson = new Gson();
                String json = gson.toJson(datos);
                dataOutX.writeUTF(json);
                dataOutX.flush();
                HiloServidorTresEnRaya hiloX = new HiloServidorTresEnRaya(1, jugadorX, dataOutX, reglas);
                
                jugadorO = servidor.accept();
                System.out.println("Jugador " + JUGADORO + " conectado a la partida " + contPartida);
                dataOutO = new DataOutputStream(jugadorO.getOutputStream());
                datos.setTurno(2);
                json = gson.toJson(datos);
                dataOutO.writeUTF(json);
                dataOutO.flush();
                HiloServidorTresEnRaya hiloO = new HiloServidorTresEnRaya(2, jugadorO, dataOutO, reglas);
                
                datos.setEstado(EN_PARTIDA);
                datos.setTurno(1);
                json = gson.toJson(datos);
                dataOutX.writeUTF(json);
                dataOutX.flush();
                dataOutO.writeUTF(json);
                dataOutO.flush();
                
                hiloX.start();
                hiloO.start();
                
                /*if(reglas.getEstado() == FINALIZADO){
                    System.out.println("Terminando el juego");
                    break;
                }*/
                contPartida++;
            }
        } catch (IOException ex) {
            Logger.getLogger(ServidorTresEnRaya.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                servidor.close();
                jugadorX.close();
                jugadorO.close();
                dataOutX.close();
                dataOutO.close();
            } catch (IOException ex) {
                Logger.getLogger(ServidorTresEnRaya.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
