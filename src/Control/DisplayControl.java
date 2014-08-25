package Control;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import Collision.StaticHitbox2f;
import Entity.Player;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.RawModel;
import RenderEngine.Renderer;
import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;

public class DisplayControl implements Runnable{
	
	//Sets up everything the game needs
	private static void start(){
		DisplayManager.create();
		
		//Initial OpenGL setup 
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		setupLighting();
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 1000, 0, 600, 1, -1);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}
	
	private static void setupLighting(){
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		
		FloatBuffer Ambient = BufferUtils.createFloatBuffer(16);
		Ambient.put(new float[]{0.2f, 0.2f, 0.2f, 0.5f});
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


	public void run(){
		start();
		
		Renderer renderer = new Renderer();
		Loader loader = new Loader();

		GLU.gluLookAt(Camera.getLocation().x, Camera.getLocation().y, Camera.getLocation().z, 0.0f, 0.0f, -1.0f, 0, 1, 0);
		/*
		Cubef temp = new Cubef(new Vector3f(0f, 0f, -3f), new Vector3f(1f, 1f, -2f));
		RawModel model = loader.loadToVAO(temp.getVertices(), temp.getIndices());
		model.setRGBA(1f, 0.3f, 0.3f, 0f);
		*/
		Settings.hb = StaticHitbox2f.RandomGeneration(1000, -1000, -1000, 1000, 1000, 100);
		for(int i = 0; i<Settings.User.length; i++){
			System.out.println(i + " start.");
			Settings.User[i] = new Player(0,0);
			System.out.println(i + " end.\n");
			
		}
		Settings.User[0].setMovement(1);
		RawModel[] model = new RawModel[Settings.hb.length];
		
		for(int i = 0; i<model.length; i++){
			Cubef temp = new Cubef(new Vector3f(Settings.hb[i].x, Settings.hb[i].y, 0f), new Vector3f(Settings.hb[i].x+Settings.hb[i].width, Settings.hb[i].y+Settings.hb[i].height, 1f));
			model[i] = loader.loadToVAO(temp.getVertices(), temp.getIndices());
			model[i].setRGBA(1f, 0.3f, 0.3f, 0f);
		}
		
		
		while(!Display.isCloseRequested()){
			//Game logic
			for(int i = 0; i<Settings.User.length; i++){
				Settings.User[i].process();
			}
			
			Camera.process();
			GL11.glTranslated(Camera.getLocation().x, Camera.getLocation().y, Camera.getLocation().z);
			GL11.glRotatef(10, Camera.getRotation().x, Camera.getRotation().y, Camera.getRotation().z);
			
			renderer.prepare();
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			
			//Light position
			FloatBuffer Location = BufferUtils.createFloatBuffer(16);
	        Location.put(new float[]{-Camera.getLocation().x, -Camera.getLocation().y , 3,1});
	        Location.flip();
	        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, Location);
	        
			for(int i = 0; i<model.length; i++){
				renderer.render(model[i]);
			}
			
			for(int i = 0; i<Settings.User.length; i++){
				Cubef temp1 = new Cubef(new Vector3f(Settings.User[i].getLocation().x, Settings.User[i].getLocation().y, 0.2f), new Vector3f(Settings.User[i].getLocation().x+Settings.User[i].getSize().x, Settings.User[i].getLocation().y+Settings.User[i].getSize().y, 0.2f+Settings.User[i].getSize().x));
				RawModel player = loader.loadToVAO(temp1.getVertices(), temp1.getIndices());
				player.setRGBA(0, 0, 0, 0);
			
				renderer.render(player);
			}
			processCamera();
			DisplayManager.update();
		}
		
		loader.cleanUp();
		stop();
	}
	
	private static void processCamera(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		GLU.gluPerspective(70, Display.getWidth()/Display.getHeight(), 0.1f, 10000);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}
	
	//Closes everything down to prepare for the game's close
	private static void stop(){
		DisplayManager.close();
	}
}
