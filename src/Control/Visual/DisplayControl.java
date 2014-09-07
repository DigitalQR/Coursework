package Control.Visual;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import Control.MainControl;
import Control.Visual.Stage.MenuStage;
import Control.Visual.Stage.Stage;
import Control.Visual.Stage.TestStage;
import RenderEngine.DisplayManager;
import RenderEngine.Renderer;

public class DisplayControl implements Runnable{
	
	private static void start(){
		DisplayManager.create(false);
		setupOpenGL();
		setupLighting();
		setupStages();
	}
	
	private static void setupOpenGL(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_POINT);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	private static void setupLighting(){
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		
		FloatBuffer Ambient = BufferUtils.createFloatBuffer(16);
		Ambient.put(new float[]{0.2f, 0.2f, 0.6f, 0.7f});
        Ambient.flip();
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, Ambient);
        
        FloatBuffer Location = BufferUtils.createFloatBuffer(16);
        Location.put(new float[]{0,0,-1f,1});
        Location.flip();
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, Location);
        
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE);
	}

	
	public static final int
	STAGE_TESTSTAGE = 0,
	STAGE_MENU = 1;

	private static Stage[] stage;
	private static int STAGE_Current = STAGE_MENU;
	
	public static void setStage(int i){
		STAGE_Current = i;
	}
	
	private static void setupStages(){
		stage = new Stage[2];
		
		stage[0] = new TestStage();
		stage[1] = new MenuStage();
	}

	
	public void run(){
		start();
		GLU.gluLookAt(0, 0, 0, 0.0f, 0.0f, -1.0f, 0, 1, 0);

		for(int i = 0; i < stage.length; i++){
			stage[i].prepare();
		}
		
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
