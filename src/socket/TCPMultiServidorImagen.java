
import java.net.*;
import java.io.*;

public class TCPMultiServidorImagen {

    public static void main(String[] args) throws IOException {
        String inputLine, outputLine;
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        boolean listening = true;

        try {
            serverSocket = new ServerSocket(9000);
        } catch (IOException e) {
            System.err.println("No se puede abrir el puerto: 9000 para TCPMultiServidorImagen.");
            System.exit(1);
        }
        System.out.println("Puerto abierto: 9000.");

        while (listening) {
            new TCPServidorImagenHilo(serverSocket.accept()).start();
            System.out.println("......................");
        }
        serverSocket.close();
    }
}
