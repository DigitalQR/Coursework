package Control.Visual;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import Control.MainControl;
import Control.Visual.Stage.MenuStage;
import Control.Visual.Stage.OverworldStage;
import Control.Visual.Stage.Stage;
import Entities.Player;
import RenderEngine.DisplayManager;
import RenderEngine.Renderer;

public class DisplayControl implements Runnable{
	
	private static void start(){
		DisplayManager.create(false);
		setupOpenGL();
		setupStages();
	}
	
	private static void setupOpenGL(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
		
	public static final int
	STAGE_OVERWORLD = 0,
	STAGE_MENU = 1,
	STAGE_TEST = 3;

	private static Stage[] stage;
	private static int STAGE_Current = STAGE_MENU;
	
	public static void setStage(int i){
		STAGE_Current = i;
	}
	
	private static void setupStages(){
		stage = new Stage[2];
		
		stage[0] = new OverworldStage();
		stage[1] = new MenuStage();
	}

	
	public void run(){
		start();
		GLU.gluLookAt(0, 0, 0, 0.0f, 0.0f, -1.0f, 0, 1, 0);

		for(int i = 0; i < stage.length; i++){
			stage[i].prepare();
		}
		
		Player.loadTexture();
		
		while(!Display.isCloseRequested() && !MainControl.CloseRequest){
			Renderer.prepare();
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			
			stage[STAGE_Current].update();

			processCamera();
			DisplayManager.update();
		}
		
		MainControl.CloseRequest = true;
		stop();
	}
	
	private static void processCamera(){		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		float WHR = Display.getWidth()/Display.getHeight();
		GLU.gluPerspective(80, WHR, 0.1f, 10000);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}
	
	//Closes everything down to prepare for the game's close
	private static void stop(){
		DisplayManager.close();
	}
}
