//package Identikit;


import java.awt.image.BufferedImage;  
import java.io.File;  
import java.io.IOException;  
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.io.InputStream;

public class ImageUtilClient { 
	
	
    /** 
    * Carga una variable BufferedImage desde un archivo tipo imagen 
    * @param pathname la ruta en el disco del archivo de la imagen 
    * @return BufferedImage con la imagen cargada desde el archivo 
    */
    public static BufferedImage loadImage(String pathname) {  
        BufferedImage bufim = null;  
        try { 
			bufim = ImageIO.read(new File(pathname));  
       	} catch (Exception e) {  
      		e.printStackTrace();  
        	}  
		return bufim;  
    }  
     
    /** 
    * Guarda una BufferedImage al disco en formato ".png" o ".jpg"
    * @param bufferedImage la imagen que se desea guardar 
    * @param pathname la ruta del archivo en el cual se desea guardar la imagen incluyendo el nombre del archivo y su formato 
    */  
    
    public static byte[] createByte (BufferedImage originalImage){
            byte[] imageInByte = null;
            try{

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write( originalImage, "jpg", baos );
                    baos.flush();
                    imageInByte = baos.toByteArray();
                    baos.close();

            }catch(IOException e){
                    System.out.println(e.getMessage());
            }
            return 	imageInByte;
    }
	
    public static String createBufferedImage( byte[] imageInByte, String cedula, String nombre, String tipo) {
            String pathDirDest = "C:\\Users\\Delia\\Desktop\\socket\\cliente\\";
            
            String nombreDestino = (pathDirDest+cedula+"\\"+nombre+"."+tipo);
         
            String directorioDestino = pathDirDest+cedula+"\\";
            File folder = new File(directorioDestino);
            folder.mkdirs();

            try {
                // convert byte array back to BufferedImage
                InputStream in = new ByteArrayInputStream(imageInByte);
                BufferedImage bImageFromConvert = ImageIO.read(in);
                saveImage(bImageFromConvert, nombreDestino );
                
            } catch (IOException e) {
                System.out.println(e.getMessage());
                directorioDestino = "No se ha podido copiar la Imagen";
            }
        return directorioDestino;
    }
	
    public static void saveImage(BufferedImage bufferedImage, String pathname) {  
        try {  
      		String format = (pathname.endsWith(".png")) ? "png" : "jpg";  
      		ImageIO.write(bufferedImage, format, new File(pathname));  
        } catch (IOException e) {  
             	e.printStackTrace();  
        }  
    } 
}  
