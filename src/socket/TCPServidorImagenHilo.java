import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage; 
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import socket.DatosImagen;

public class TCPServidorImagenHilo extends Thread {

    private Socket socket = null;
    private Object objetSend;
    private ObjectOutputStream salidaObje;
    private ArrayList list;

    public TCPServidorImagenHilo(Socket socket) {
        super("TCPServerHiloIdent");
        this.socket = socket;
    }

    public void run() {

        try {
            //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //out.println("Bienvenido!");
            String inputLine; 
            OutputStream outputLine = socket.getOutputStream();

            //construyendo object output para enviar objetos por sockets.
            this.salidaObje = new ObjectOutputStream(outputLine);

            inputLine = in.readLine();
          	
            String datoEnviar = DatosImagen.obtenerDB(inputLine);
            System.out.println(datoEnviar);
            salidaObje.writeObject(datoEnviar);
            ///ENVIAR EL PATH DONDE SE GUARDO LA IMAGEN
            /*ArrayList<byte[]> listaImagenes = ImageUtilServer.sendImage(inputLine);
            System.out.println("listaImagenes: "+listaImagenes);

            if (listaImagenes != null) {	
                this.sendData(listaImagenes);
            } else {

                this.sendData("No existe el numero");
            }*/
            in.close();
            socket.close();
            System.out.println("Finalizando Hilo");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(TCPServidorImagenHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendData(Object sendObj){
        try{
			
            System.out.println("Objeto .: "+sendObj); 
            //list = (ArrayList) sendObj;
			
            if(sendObj instanceof ArrayList) {
                System.out.println("ENTRO EN INSTANCEOF .: "); 
                list = (ArrayList) sendObj;
                salidaObje.writeObject(list);
            }else{
                salidaObje.writeObject(sendObj);
            }
        
            System.out.println("Objeto enviado.: "+list);   
        }catch(Exception e){
            System.out.println("Exception: " + e.getMessage());   
        }
    }
}
