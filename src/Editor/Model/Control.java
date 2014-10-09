package Editor.Model;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import Control.MainControl;
import Control.Visual.Stage.Stage;
import RenderEngine.DisplayManager;
import RenderEngine.Renderer;

public class Control{
	
	private static Stage mainStage = new ModelStage();
	
	public static void main(String[] args){
		DisplayManager.create(false);
		setupOpenGL();
		Option.launch();
		Option.setNew();
		run();
	}
	
	private static void setupOpenGL(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_POINT);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
		
	public static void run(){
		GLU.gluLookAt(0, 0, 0, 0.0f, 0.0f, -1.0f, 0, 1, 0);
		mainStage.prepare();
		Option.updateNodeList();
		
		while(!Display.isCloseRequested() && !MainControl.CloseRequest){
			Renderer.prepare();
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			
			mainStage.update();

			processCamera();
			DisplayManager.update();
		}
		
		stop();
	}
	
	private static void processCamera(){		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		float WHR = Display.getWidth()/Display.getHeight();
		GLU.gluPerspective(80, WHR, 0.01f, 10000);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}
	
	private static void stop(){
		DisplayManager.close();
		System.exit(0);
	}
}
