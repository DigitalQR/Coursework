package Control.Visual;


import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import Debug.ErrorPopup;
import Tools.Maths.Vector2f;

public class DisplayManager{
	
	public static  int FPS = 120;
	public static Vector2f p480 = new Vector2f(800, 480);
	public static Vector2f p720 = new Vector2f(1280, 720);
	public static Vector2f p1080= new Vector2f(1920, 1080);
	
	public static void create(boolean fullscreen){
		Vector2f Res = p720; //new Vector2f(1280, 1024)
		
		try{
			DisplayMode display = null;
			DisplayMode[] mod = Display.getAvailableDisplayModes();
			
			for(DisplayMode m:mod){
				if(m.isFullscreenCapable()){
					System.out.println("Resolution: " + m.getWidth() + ", " + m.getHeight());
				}
				if(m.getWidth() == Res.x && m.getHeight() == Res.y && m.isFullscreenCapable()){
					display = m;
				}
			}
			
			Display.setDisplayMode(display);
			Display.setFullscreen(fullscreen);
			Display.create();
			
			Display.setTitle("Untiled Game");
		}catch(LWJGLException e){
			ErrorPopup.createMessage(e, true);
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
