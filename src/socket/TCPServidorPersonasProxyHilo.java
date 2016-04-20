/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package servidorProxy;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author willian
 */
public class TCPServidorPersonasProxyHilo extends Thread {

    private Socket socket = null;

    public TCPServidorPersonasProxyHilo(Socket socket) {
        super("TCPServerHilo");
        this.socket = socket;
    }

    public void run() {

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                    socket.getInputStream()));
            out.println("Bienvenido!");
            String inputLine, outputLine = "";
            
            String cedula;
            String datosPersonales = "";
            String datosSeguro= "";
            String datosImagen= "";
            
            //JSON a enviar
            JSONObject objJsonEnviar = new JSONObject();
            JSONParser parser = new JSONParser();

            while ((inputLine = in.readLine()) != null) {
                cedula = inputLine;
                System.out.println("Cedula recibida: " + cedula);

                if (inputLine.equals("exit")) {
                    out.println(inputLine);
                    break;
                }
                //Las llamadas a los Servidores
                try {   
                    String codigoSalud="";
                    try{
                        //Llamanda al servidor de directorio
                        datosPersonales = llamarServidorUDP(cedula, 9876);
                        System.out.println("Datos Personales: " + datosPersonales);
                        
                        if(datosPersonales.length()>0){
                            Object objDatosPersonales = parser.parse(datosPersonales);
                            JSONObject jsonObjectDatosPersonales = (JSONObject) objDatosPersonales;
                            codigoSalud = (String) jsonObjectDatosPersonales.get("CodigoSalud");
                            objJsonEnviar.put("Cedula", cedula);
                            objJsonEnviar.put("Nombre", jsonObjectDatosPersonales.get("Nombre"));
                            objJsonEnviar.put("Apellido", jsonObjectDatosPersonales.get("Apellido"));
                            objJsonEnviar.put("CodigoSalud", jsonObjectDatosPersonales.get("CodigoSalud"));    
                        }else{  
                            objJsonEnviar.put("Cedula", "No disponible");
                            objJsonEnviar.put("Nombre", "No disponible");
                            objJsonEnviar.put("Apellido", "No disponible");
                            objJsonEnviar.put("CodigoSalud", "No disponible");
                            objJsonEnviar.put("Estado", "No disponible");
                            objJsonEnviar.put("SeguroSalud", "No disponible");
                            objJsonEnviar.put("Duracion", "No disponible");
                        }
    
                        try{
                            if(codigoSalud.length()>0){
                                //Llamanda al servidor de salud
                                datosSeguro = llamarServidorUDP(codigoSalud, 7000);
                                System.out.println("Datos Seguro: " + datosSeguro);
                                if(datosSeguro.length()>0){
                                    Object objDatosSeguro = parser.parse(datosSeguro);
                                    JSONObject jsonObjectDatosSeguro = (JSONObject) objDatosSeguro;
                                    objJsonEnviar.put("Estado", jsonObjectDatosSeguro.get("Estado"));
                                    objJsonEnviar.put("SeguroSalud",   jsonObjectDatosSeguro.get("SeguroSalud"));
                                    objJsonEnviar.put("Duracion",   jsonObjectDatosSeguro.get("Duracion"));
                                }else{
                                    objJsonEnviar.put("Estado", "No disponible");
                                    objJsonEnviar.put("SeguroSalud", "No disponible");
                                    objJsonEnviar.put("Duracion", "No disponible");
                                }
                            }else{
                                objJsonEnviar.put("Estado", "No disponible");
                                objJsonEnviar.put("SeguroSalud", "No disponible");
                                objJsonEnviar.put("Duracion", "No disponible");
                            }
                        }catch (Exception ex) {
                            ex.printStackTrace();
                            objJsonEnviar.put("Estado", "No disponible");
                            objJsonEnviar.put("SeguroSalud", "No disponible");
                            objJsonEnviar.put("Duracion", "No disponible");
                        }
                    }catch (Exception ex) {
                        ex.printStackTrace();
                        objJsonEnviar.put("Cedula", "Error");
                        objJsonEnviar.put("Nombre", "Error");
                        objJsonEnviar.put("Apellido", "Error");
                        objJsonEnviar.put("CodigoSalud", "Error");
                        objJsonEnviar.put("Estado", "Error");
                        objJsonEnviar.put("SeguroSalud", "Error");
                        objJsonEnviar.put("Duracion", "Error");
                    }
                    
                    try{
                        datosImagen = llamarServidorImagenes(cedula, 9000);
                        System.out.println("Datos Imagen: " + datosImagen);
                        if(datosImagen.length()>0){
                            Object objDatosImagen = parser.parse(datosImagen);
                            JSONObject jsonObjectDatosImagen = (JSONObject) objDatosImagen;
                            System.out.println("Datos JSON IMAGEN: " + jsonObjectDatosImagen.toJSONString());
                            
                            objJsonEnviar.put("NombreImagen", jsonObjectDatosImagen.get("NombreImagen"));
                            objJsonEnviar.put("TipoImagen", jsonObjectDatosImagen.get("TipoImagen"));
                            objJsonEnviar.put("Imagen", jsonObjectDatosImagen.get("Imagen"));
                        }else{
                            objJsonEnviar.put("NombreImagen", "No disponible");
                            objJsonEnviar.put("TipoImagen", "No disponible");
                            objJsonEnviar.put("Imagen", "No disponible");
                        }
                        
                    }catch (Exception ex) {
                        ex.printStackTrace();
                        objJsonEnviar.put("NombreImagen", "Error");
                        objJsonEnviar.put("TipoImagen", "Error");
                        objJsonEnviar.put("Imagen", "Error");
                    }             
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
                String datoEnviar = objJsonEnviar.toJSONString();
                System.out.println("Datos a Enviar: " + datoEnviar);
                
                out.println(datoEnviar);
            }
            out.close();
            in.close();
            socket.close();
            System.out.println("Finalizando Hilo");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private String llamarServidorUDP(String inputLine, int puerto) throws Exception {
        // Datos necesario
        String direccionServidor = "127.0.0.1";
        String datosRetornar = "";

        int puertoServidor = puerto;

        try {

            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

            DatagramSocket clientSocket = new DatagramSocket();

            InetAddress IPAddress = InetAddress.getByName(direccionServidor);
            System.out.println("Intentando conectar a = " + IPAddress + ":" + puertoServidor + " via UDP...");

            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];

//            System.out.print("Ingrese el mensaje a enviar: ");
//            String sentence = inFromUser.readLine();
            sendData = inputLine.getBytes();

            System.out.println("Enviar " + sendData.length + " bytes al servidor.");
            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, puertoServidor);

            clientSocket.send(sendPacket);

            DatagramPacket receivePacket =
                    new DatagramPacket(receiveData, receiveData.length);

            System.out.println("Esperamos si viene el paquete");

            //Vamos a hacer una llamada BLOQUEANTE entonces establecemos un timeout maximo de espera
            clientSocket.setSoTimeout(5000);

            try {
                // ESPERAMOS LA RESPUESTA, BLOQUEANTE
                clientSocket.receive(receivePacket);

                String modifiedSentence = new String(receivePacket.getData());
                InetAddress returnIPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();

                System.out.println("Respuesta desde =  " + returnIPAddress + ":" + port);

                //Asignamos los datos a retornar de la persona
                datosRetornar = modifiedSentence.trim();

            } catch (SocketTimeoutException ste) {

                System.out.println("TimeOut: El paquete al UDP se asume perdido.");
            }
            clientSocket.close();
        } catch (UnknownHostException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return datosRetornar;
    }
    
    
    
    private String llamarServidorImagenes(String inputLine, int puerto) throws Exception {
        Socket unSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        String fromServer = "";
        
        ArrayList list = null;
		Object aux;

		ObjectInputStream entrada = null;
        try {
            unSocket = new Socket("localhost", puerto);
            // enviamos nosotros
            out = new PrintWriter(unSocket.getOutputStream(), true);

            //viene del servidor
           // in = new BufferedReader(new InputStreamReader(unSocket.getInputStream()));
           entrada = new ObjectInputStream(unSocket.getInputStream());
        
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			String fromUser;

			fromUser = inputLine;
			if (fromUser != null) {
				System.out.println("Cliente: " + fromUser);
				//escribimos al servidor
				out.println(fromUser);
			}
			
			unSocket.setSoTimeout(5000);
                        try{
                            fromServer = entrada.readObject().toString();// Se lee el objeto
                            System.out.println("Respuesta desde :  " + fromServer);
                            System.out.println("Respuesta desde =  " + "127.0.0.1" + ":" + puerto);
                        }  catch (SocketTimeoutException exception) {
                            System.out.println("TimeOut: El paquete al Servidor de Imagenes se asume perdido.");
                        }
			out.close();
			in.close();
			stdIn.close();
			unSocket.close();
			
        
        } catch (UnknownHostException e) {
            System.err.println("Host desconocido");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error de I/O en la conexion al host");
            e.printStackTrace();           
            System.exit(1);
        }catch (Exception ex){
            
	}
        
        return fromServer.trim();
    }
}

