
import java.net.*;
import java.text.ParseException;
import socket.DatosSeguro;

public class UDPServidorSalud {

    public static void main(String a[]) throws Exception {

        int puerto = 7000;
        
        try {

            DatagramSocket socket = new DatagramSocket(puerto);
            byte[] receiveData = new byte[1024];
            
            while (true) {
                
                receiveData = new byte[1024];
                DatagramPacket receivePacket =
                        new DatagramPacket(receiveData, receiveData.length);
                System.out.println("Esperando Número de seguro... ");

                //Llamada BLOQUEANTE
                socket.receive(receivePacket);

                String codigoSeguro = new String(receivePacket.getData());
                InetAddress IPAddress = receivePacket.getAddress();
                int puertoDestino = receivePacket.getPort();
               
                String datoEnviar = DatosSeguro.obtenerDB(codigoSeguro);
                
                byte[] sendData = datoEnviar.getBytes();
                
                DatagramPacket sendPacketServidorProxy = new DatagramPacket(sendData, sendData.length, IPAddress,
                        puertoDestino);
                socket.send(sendPacketServidorProxy);
            }
        } catch (SocketException e) {
            System.out.println("Puerto del Servidor de Seguro " + puerto + " está ocupado.");
            System.exit(1);
        }
    }
}
