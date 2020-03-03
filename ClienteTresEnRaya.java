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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vicky
 */
public class ClienteTresEnRaya implements Constantes {

    private Socket cliente;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private int id;
    private Datos datos;

    public ClienteTresEnRaya() {
    }

    public static void main(String[] args) {
        new ClienteTresEnRaya().listen();
    }

    private void listen() {
        final int PUERTO = 6000;
        final String HOST = "localhost";

        datos = new Datos();
        Gson gson;
        Scanner scanner;

        try {
            cliente = new Socket(HOST, PUERTO);
            dataIn = new DataInputStream(cliente.getInputStream());
            dataOut = new DataOutputStream(cliente.getOutputStream());
            gson = new Gson();
            scanner = new Scanner(System.in);
            String json = dataIn.readUTF();
            datos = gson.fromJson(json, Datos.class);//lee turno
            id = datos.getTurno();
            if (id == 1) {
                System.out.println("Eres el jugador " + JUGADORX);
            } else {
                System.out.println("Eres el jugador " + JUGADORO);
            }

            json = dataIn.readUTF();
            datos = gson.fromJson(json, Datos.class);//lee estado
            System.out.println("Comenzando partida");

            while (datos.getEstado() == EN_PARTIDA) {
                if (id == datos.getTurno()) {
                    System.out.println("Elije posición");
                    muestraTableroPos();
                    muestraTableroLetras();
                    int posicion = scanner.nextInt();
                    datos.setPosicion(posicion);
                    json = gson.toJson(datos);
                    dataOut.writeUTF(json);//manda posicion
                    dataOut.flush();
                } else {
                    System.out.println("Espera tu turno");
                    muestraTableroPos();
                    muestraTableroLetras();
                }
                json = dataIn.readUTF();//recibe tablero
                datos = gson.fromJson(json, Datos.class);
                if (datos.getGanador() == 'o') {
                    muestraTableroLetras();
                    System.out.println("Ha habido un empate. Fin del juego");
                    break;
                }
                if (datos.getGanador() == JUGADORX || datos.getGanador() == JUGADORO) {
                    muestraTableroLetras();
                    System.out.println("El ganador es el jugador " + datos.getGanador());
                    break;
                }
            }
            if (datos.getEstado() == FINALIZADO) {
                System.exit(0);
            }
        } catch (IOException ex) {
            Logger.getLogger(ClienteTresEnRaya.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cliente.close();
                dataIn.close();
                dataOut.close();
            } catch (IOException ex) {
                Logger.getLogger(ClienteTresEnRaya.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void muestraTableroPos() {
        for (int i = 0; i < datos.getTableroPos().length; i++) {
            for (int j = 0; j < datos.getTableroPos()[i].length; j++) {
                System.out.print(datos.getTableroPos()[i][j] + " ");
            }
            System.out.println(" ");
        }
    }

    private void muestraTableroLetras() {
        for (int i = 0; i < datos.getTableroLetras().length; i++) {
            if (i == 2 || i == 5) {
                System.out.println(datos.getTableroLetras()[i] + "\n-----");
            } else if (i == 8) {
                System.out.println(datos.getTableroLetras()[i] + "\n");
            } else {
                System.out.print(datos.getTableroLetras()[i] + " |");
            }
        }
    }
}
