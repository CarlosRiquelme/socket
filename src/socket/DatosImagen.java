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
public class DatosImagen {
    
    public static String obtenerDB(String datoRecibido) throws SQLException
    {
        String servidor = "jdbc:mysql://localhost/sd_imagen_db";
        
        Connection miConexion = ConnectionDB.GetConnection(servidor);
                
        System.out.println(miConexion);
          
        JSONObject objJson = new JSONObject();

        if(miConexion != null){
            String cedula = datoRecibido;
            System.out.println("cedula = " + cedula);
            double dato = Double.valueOf(cedula);
             
            Statement s = miConexion.createStatement(); 
            ResultSet rs = s.executeQuery ("select * from datos_imagen where cedula = " + dato);

            boolean isRecord = false;
            while (rs.next()){
                objJson.put("NombreImagen", rs.getString("nombre"));
                objJson.put("TipoImagen",  rs.getString("tipo"));
                objJson.put("Imagen",  rs.getString("imagen"));
                isRecord = true;
            }
            
            if (!isRecord){
                objJson.put("NombreImagen", "");
                objJson.put("TipoImagen", "");
                objJson.put("Imagen", "");
            }
        }
        System.out.println(objJson.toString());
        String datosPersonales = objJson.toString();
        return datosPersonales;
    }
}
