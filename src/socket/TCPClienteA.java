/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package clientesTCP;

import java.io.*;
import java.net.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
//import org.json.simple.parser.ParseException;

/**
 *
 * @author willian
 */
public class TCPClienteA {

    public static void main(String[] args) throws IOException, Exception {

        Socket unSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            unSocket = new Socket("localhost", 4444);
            // enviamos nosotros
            out = new PrintWriter(unSocket.getOutputStream(), true);

            //viene del servidor
            in = new BufferedReader(new InputStreamReader(unSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Host desconocido");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error de I/O en la conexion al host");
            System.exit(1);
        }


        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;
        //ReadPersona leerPer = new ReadPersona();
        boolean continuar = true;
        
        fromServer = in.readLine();
        System.out.println("Servidor: " + fromServer);
        
        System.out.print("Ingrese Cedula: ");
        fromUser = stdIn.readLine();
        if (fromUser != null) {
            //System.out.println("Ingrese Cedula: " + fromUser);

            //escribimos al servidor
            out.println(fromUser);
        }
        fromServer = in.readLine();
        mostrarDatos(fromServer);
        
        fromServer = in.readLine();
        System.out.println(fromServer);
        
              
        fromServer = "exit";
        out.close();
        in.close();
        stdIn.close();
        unSocket.close();
    }

    public static void mostrarDatos(String cadenaDatos) throws Exception {
        JSONParser parser = new JSONParser();
        //Sacamos los espacios en blanco
        cadenaDatos = cadenaDatos.trim();

        if (cadenaDatos.equals("") == true) {
            System.out.println("No existe informacion de Identificacion Personal");
        } else {
            System.out.println("Entro en Parser de JSON");
            try {
                Object obj = parser.parse(cadenaDatos);

                JSONObject jsonObject = (JSONObject) obj;
                
                //1
                String cedula = (String) jsonObject.get("Cedula");
                System.out.println("Cédula de Identidad: " + cedula);
                //2
                String nombre = (String) jsonObject.get("Nombre");
                System.out.println("Nombre: " + nombre);
                //3
                String apellido = (String) jsonObject.get("Apellido");
                System.out.println("Apellido: " + apellido);
                //4
                String codigoSalud = (String) jsonObject.get("CodigoSalud");
                System.out.println("Código Salud: " + codigoSalud);
                //5
                String estado = (String) jsonObject.get("Estado");
                System.out.println("Estado del Seguro: " + estado);
                //6
                String seguroSalud = (String) jsonObject.get("SeguroSalud");
                System.out.println("Nombre del Seguro: " + seguroSalud);
                //7
                String duracion = (String) jsonObject.get("Duracion");
                System.out.println("Duración del Seguro: " + duracion);
                //8
                String nombreImagen = (String) jsonObject.get("NombreImagen");
                //System.out.println("NombreImagen: " + nombreImagen);
                //9
                String tipoImagen = (String) jsonObject.get("TipoImagen");
                //System.out.println("NombreImagen: " + tipoImagen);
                //10
                
                String imagen = (String) jsonObject.get("Imagen");
                
                byte[] bitsImagen = imagen.getBytes();
                //System.out.println("NombreImagen: " + imagen);

                String pathImage = ImageUtilClient.createBufferedImage(bitsImagen,cedula,nombreImagen,tipoImagen);
                
                
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
