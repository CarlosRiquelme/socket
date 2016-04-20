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
public class DatosSeguro {
    
    public static String obtenerDB(String datoRecibido) throws SQLException
    {
        String servidor = "jdbc:mysql://localhost/sd_salud_db";
        
        Connection miConexion = ConnectionDB.GetConnection(servidor);
                
        System.out.println(miConexion);
          
        JSONObject objJson = new JSONObject();

        if(miConexion != null){
            String codigoSalud = datoRecibido;
            
            System.out.println("Codigo Salud = " + codigoSalud);
            double dato = Double.valueOf(codigoSalud);
            
            Statement s = miConexion.createStatement(); 
            ResultSet rs = s.executeQuery ("select * from datos_seguro where codigo_salud = " + dato );

            boolean isRecord = false;
            while (rs.next()){
                objJson.put("Estado", rs.getString("estado"));
                objJson.put("SeguroSalud",  rs.getString("seguro"));
                objJson.put("Duracion",  rs.getString("duracion"));
                isRecord = true;
                System.out.println("IsRecord: " + isRecord);
            }
            
            if (!isRecord){
                objJson.put("Estado", "noExiste");
                objJson.put("SeguroSalud", "--");
                objJson.put("Duracion", "--");
                System.out.println("IsRecord: " + isRecord);
            }
        }
        System.out.println(objJson.toString());
        String datosPersonales = objJson.toString();
        return datosPersonales;
    }
}
