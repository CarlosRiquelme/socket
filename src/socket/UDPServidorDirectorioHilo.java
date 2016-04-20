/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package servidoresUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.json.simple.JSONObject;
import socket.ConnectionDB;
import socket.DatosPersonales;

/**
 *
 * @author willian
 */
public class UDPServidorDirectorioHilo extends Thread{

    private DatagramPacket receivePacket;
    private DatagramSocket serverSocket;
    
    public UDPServidorDirectorioHilo(DatagramPacket receivePacket, DatagramSocket serverSocket){
        super("UDPServerHilo");
        this.receivePacket = receivePacket;
        this.serverSocket = serverSocket;
    }
    
    @Override
    public void run(){
        try {
            System.out.println("Aceptamos un paquete");
                
                // Datos recibidos e Identificamos quien nos envio
                String datoRecibido = new String(receivePacket.getData());

                InetAddress IPAddress = receivePacket.getAddress();

                int port = receivePacket.getPort();

                System.out.println("De : " + IPAddress + ":" + port);
                System.out.println("Cedula recibida : " + datoRecibido);
                
                // Buscamos datos personales de la cedula recibida
                String cadenaPersona = DatosPersonales.obtenerDB(datoRecibido).toString();

                // Enviamos la respuesta inmediatamente a ese mismo cliente
                // Es no bloqueante
                byte [] sendData = new byte[cadenaPersona.length()];
                sendData = cadenaPersona.getBytes();
                DatagramPacket sendPacket =
                        new DatagramPacket(sendData, sendData.length, IPAddress,
                        port);

                serverSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(UDPServidorDirectorioHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
