/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package socket;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.simple.JSONObject;

/**
 *
 * @author Delia
 */
public class DatosPersonales {
    
    public static String obtenerDB(String datoRecibido) throws SQLException
    {
        String servidor = "jdbc:mysql://localhost/sd_directorio_db";
        
        Connection miConexion = ConnectionDB.GetConnection(servidor);
                
        System.out.println(miConexion);
          
        JSONObject objJson = new JSONObject();

        if(miConexion != null){
            String cedula = datoRecibido;
            System.out.println("cedula = " + cedula);
            double dato = Double.valueOf(cedula);
             
            Statement s = miConexion.createStatement(); 
            ResultSet rs = s.executeQuery ("select * from datos_personales where cedula = " + dato);

            boolean isRecord = false;
            while (rs.next()){
                objJson.put("Nombre", rs.getString("nombre"));
                objJson.put("Apellido",  rs.getString("apellido"));
                objJson.put("CodigoSalud",  rs.getString("codigo_salud"));
                isRecord = true;
            }
            
            if (!isRecord){
                objJson.put("Nombre", "");
                objJson.put("Apellido", "");
                objJson.put("CodigoSalud", "");
            }
        }
        System.out.println(objJson.toString());
        String datosPersonales = objJson.toString();
        return datosPersonales;
    }
}
