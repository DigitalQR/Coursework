package RenderEngine;


import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class DisplayManager{
	
	private static final int FPS = 120;
	
	
	public static void create(){
		
		try{
			Display.setDisplayMode(new DisplayMode(1000, 600));
			Display.create();
			Display.setTitle("Untitled");
			
		}catch(LWJGLException e){
			System.err.println("[ERROR]" + e.getMessage());
			e.printStackTrace();
			System.exit(0);
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
