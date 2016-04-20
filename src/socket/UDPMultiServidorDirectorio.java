
import java.net.*;

public class UDPMultiServidorDirectorio {

    public static void main(String a[]) throws Exception {

        int puerto = 9876;

        try {
            //1) Creamos el socket Servidor de Datagramas (UDP)
            DatagramSocket serverSocket = new DatagramSocket(puerto);

            byte[] receiveData = new byte[1024];
           
            //3) Servidor siempre esperando
            while (true) {

                receiveData = new byte[1024];

                DatagramPacket receivePacket =
                        new DatagramPacket(receiveData, receiveData.length);

                System.out.println("Esperando número de cédula... ");

                // 4) Receive LLAMADA BLOQUEANTE
                serverSocket.receive(receivePacket);

                //Desde aca en el hilo
                new UDPServidorDirectorioHilo(receivePacket, serverSocket).start();
                //Hasta en el hilo
            }

        } catch (SocketException ex) {
            System.out.println("Puerto UDP del Servidor de Directorio " + puerto + " esta ocupado.");
            System.exit(1);
        }
    }
}