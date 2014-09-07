package RenderEngine;


import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class DisplayManager{
	
	private static final int FPS = 120;
	
	
	public static void create(boolean fullscreen){
		
		try{
			DisplayMode display = null;
			DisplayMode[] mod = Display.getAvailableDisplayModes();
			
			for(DisplayMode m:mod){
				//720, 480
				//1280, 720
				//1920 1080
				if(m.getWidth() == 1280 && m.getHeight() == 720 && m.isFullscreenCapable()){
					display = m;
				}
			}
			
			Display.setDisplayMode(display);
			Display.setFullscreen(fullscreen);
			Display.create();
			
			Display.setTitle("Untiled Game");
			
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
