package Control.Visual;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.PNGDecoder;

public class DisplayManager{
	
	public static  int FPS = 120;
	public static boolean fullscreen = false;
	
	public static void create(DisplayMode display) throws LWJGLException{
			
		Display.setDisplayMode(display);
		Display.setFullscreen(fullscreen);
		try{
			Display.setIcon(new ByteBuffer[]{
					loadIcon(new File("Res/Button/Icon32.png")),
					loadIcon(new File("Res/Button/Icon64.png"))	
			});
		}catch(IOException e){
			e.printStackTrace();
		}
		
		Display.create();
			
		Display.setTitle(" S Q U A R E   O F F ");

	}
	
	private static ByteBuffer loadIcon(File file) throws IOException{
		InputStream is = new FileInputStream(file);
		try{
            PNGDecoder decoder = new PNGDecoder(is);
            ByteBuffer bb = ByteBuffer.allocateDirect(decoder.getWidth()*decoder.getHeight()*4);
            decoder.decode(bb, decoder.getWidth()*4, PNGDecoder.RGBA);
            bb.flip();
            return bb;
        }finally{
            is.close();
        }
	}
	
	public static void update(){
		Display.sync(FPS);
		Display.update();
	}
	
	public static void close(){
		Display.destroy();
	}
	
}
