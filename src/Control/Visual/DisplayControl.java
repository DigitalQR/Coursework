package Control.Visual;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import Control.Camera;
import Control.MainControl;
import Control.Settings;
import Control.Visual.Stage.MenuStage;
import Control.Visual.Stage.OverworldStage;
import Control.Visual.Stage.Stage;
import Entities.Player;
import RenderEngine.Renderer;
import Tools.Maths.Toolkit;

public class DisplayControl implements Runnable{
	
	public static boolean exists = false;
	
	private static void start(){
		DisplayManager.create();
		setupOpenGL();
		setupLighting();
		setupStages();
	}
	
	public static float r = 0.1f, g = 0.1f, b = 0.3f;
	private static float[] RGBA = {0f,0f,0f,1f};
	
	public static float[] getRGBA(){
		return RGBA;
	}

	public static float[] getInverseRGBA(){
		float[] RGBA = getRGBA().clone();
		for(int i = 0; i<3; i++){
			RGBA[i] = 1f - RGBA[i];

		}
		return RGBA;
	}
	
	private static void incrementRandomLighting(){
		FloatBuffer Ambient = BufferUtils.createFloatBuffer(16);
		float speed = Settings.floats.get("s_light_deviation");
		r+=0.01f*speed;
		g+=0.015f*speed;
		b+=0.02f*speed;
		
		RGBA[0] = Toolkit.Modulus((float)Math.sin(r));
		RGBA[1] = Toolkit.Modulus((float)Math.sin(g));
		RGBA[2] = Toolkit.Modulus((float)Math.sin(b));
		
		for(int i = 0; i<3; i++){
			if(RGBA[i] > 0.7f){
				RGBA[i] = 0.7f;
			}
		}
		
		Ambient.put(new float[]{RGBA[0], RGBA[1], RGBA[2], RGBA[3]});
        Ambient.flip();
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, Ambient);
	}
	
	private static void setupLighting(){
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		
		FloatBuffer Ambient = BufferUtils.createFloatBuffer(16);
		Ambient.put(new float[]{0.1f, 0.1f, 0.3f, 1f});
        Ambient.flip();
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, Ambient);
        
        FloatBuffer Location = BufferUtils.createFloatBuffer(16);
        Location.put(new float[]{0,0,-5f,1});
        Location.flip();
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, Location);
        
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE);

        r = Toolkit.RandomInt(30, 100);
        r /= 100;
        g = Toolkit.RandomInt(30, 100);
        g /= 100;
        b = Toolkit.RandomInt(30, 100);
        b /= 100;
	}
	
	private static void setupOpenGL(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
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

	public static Stage[] stage;
	private static int STAGE_Current = STAGE_MENU;
	
	public static void setStage(int i){
		stage[STAGE_Current].switchFromUpdate();
		stage[i].switchToUpdate();
		STAGE_Current = i;
	}
	
	public static boolean isCurrentStage(int i){
		return STAGE_Current == i;
	}
	
	private static void setupStages(){
		stage = new Stage[2];
		
		stage[0] = new OverworldStage();
		stage[1] = new MenuStage();
	}

	
	public void run(){
		start();
		exists = true;
		GLU.gluLookAt(0, 0, 0, 0.0f, 0.0f, -1.0f, 0, 1, 0);

		for(int i = 0; i < stage.length; i++){
			stage[i].prepare();
		}
		
		Player.loadResources();
		
		while(!Display.isCloseRequested() && !MainControl.CloseRequest){
			Renderer.prepare();
			
			if(!Settings.toggles.get("d_wireframe")){
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			}else{
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			}
			
			if(STAGE_Current == STAGE_OVERWORLD){
				GL11.glEnable(GL11.GL_LIGHTING);
				FloatBuffer Location = BufferUtils.createFloatBuffer(16);
		    	Location.put(new float[]{Camera.getLocation().x,Camera.getLocation().y,10,1});
		    	Location.flip();
		    	GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, Location);
			}else{
				//GL11.glDisable(GL11.GL_LIGHTING);
			}
			
			stage[STAGE_Current].update();
			incrementRandomLighting();
			
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
		System.out.println("Closing down visual thread.");
		exists = false;
		System.out.println("exist: " + exists);
	}
}
