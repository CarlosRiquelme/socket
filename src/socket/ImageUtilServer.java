import java.awt.image.BufferedImage;  
import java.io.File;  
import java.io.IOException;  
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.io.InputStream;

public class ImageUtilServer { 
	
	
	public static ArrayList<byte[]>  sendImage(String nro_cedula) {  
		String pathDirectorio = "C:\\Users\\Delia\\Desktop\\socket\\Imagenes\\";
		byte[] imageInByte1 = null;
		byte[] imageInByte2 = null;
		byte[] imageInByte3 = null;
		
		String directorioOrigen = pathDirectorio + nro_cedula +"/";
		File folder = new File(directorioOrigen);
		if(folder.exists()){
			String caraDirectorio = directorioOrigen + "cara.jpg";
			String perfilDirectorio = directorioOrigen + "perfil.jpg";
			String cuerpoDirectorio = directorioOrigen + "cuerpo.jpg";

			BufferedImage imagen1 = null;
			BufferedImage imagen2 = null;
			BufferedImage imagen3 = null;
			
			imagen1= loadImage(caraDirectorio);
			imagen2= loadImage(perfilDirectorio);
			imagen3= loadImage(cuerpoDirectorio);
			
			imageInByte1 = createByte(imagen1);
			imageInByte2 = createByte(imagen2);
			imageInByte3 = createByte(imagen3);
			ArrayList<byte[]> listImage = new ArrayList();
		  
			listImage.add(imageInByte1);
			listImage.add(imageInByte2);
			listImage.add(imageInByte3);
			
			return listImage;
		}else{
			return null;
		}
		
		

		
	} 
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
	
	public static void saveImage(BufferedImage bufferedImage, String pathname) {  
        try {  
      		String format = (pathname.endsWith(".png")) ? "png" : "jpg";  
      		ImageIO.write(bufferedImage, format, new File(pathname));  
        } catch (IOException e) {  
             	e.printStackTrace();  
        }  
    }
}  
