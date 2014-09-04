package Control;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;

import Entity.Player;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;

public class DisplayControl implements Runnable{
	
	private static void start(){
		DisplayManager.create();
		setupOpenGL();
		setupLighting();
	}
	
	private static void setupOpenGL(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		
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

	public void run(){
		start();
		Renderer renderer = new Renderer();
		GLU.gluLookAt(0, 0, 0, 0.0f, 0.0f, -1.0f, 0, 1, 0);

		Texture BoxTexture = Loader.loadTexture("Box");
		Texture PlaneTexture = Loader.loadTexture("Plane");
		Model[] hb = new Model[Settings.hb.length];
		
		//Draw hitboxes
		for(int i = 0; i<Settings.hb.length; i++){
			Cubef temp = new Cubef(new Vector3f(Settings.hb[i].x, Settings.hb[i].y, 0f), new Vector3f(Settings.hb[i].x+Settings.hb[i].width, Settings.hb[i].y+Settings.hb[i].height, 1f));
			hb[i] = new Model(temp);
			hb[i].setTexture(BoxTexture);
		}
		
		while(!Display.isCloseRequested()){
			renderer.prepare();
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			
				MainControl.Paused = false;
				Camera.process();
				GL11.glTranslatef(Camera.getLocation().x, Camera.getLocation().y, Camera.getLocation().z);
				
				//Light position
				FloatBuffer Location = BufferUtils.createFloatBuffer(16);
		        Location.put(new float[]{-Camera.getLocation().x, -Camera.getLocation().y , 3,1});
		        Location.flip();
		        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, Location);
		        
		        //Draw hitboxes
				for(Model Box:hb){
					renderer.render(Box);
				}
				
				//Draw players
				Player[] User = Settings.User.clone();
				for(int i = 0; i<User.length; i++){
					Cubef temp1 = new Cubef(new Vector3f(User[i].getLocation().x, User[i].getLocation().y, 0.2f), new Vector3f(User[i].getLocation().x+User[i].getSize().x, User[i].getLocation().y+User[i].getSize().y, 0.2f+User[i].getSize().x));
					Model m = new Model(temp1);
					m.setTexture(PlaneTexture);
					renderer.render(m);
				}

			processCamera();
			DisplayManager.update();
		}
		
		MainControl.CloseRequest = true;
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
