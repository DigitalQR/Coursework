package Control.Visual;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class DisplayManager{
	
	public static  int FPS = 120;
	public static boolean fullscreen = false;
	
	public static void create(DisplayMode display) throws LWJGLException{
			
		Display.setDisplayMode(display);
		Display.setFullscreen(fullscreen);
		Display.create();
			
		Display.setTitle(" S Q U A R E   O F F ");

	}
	
	public static void update(){
		Display.sync(FPS);
		Display.update();
	}
	
	public static void close(){
		Display.destroy();
	}
	
}
