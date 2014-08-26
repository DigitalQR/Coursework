package Control;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.glu.GLU;

import Entity.Player;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.Renderer;
import RenderEngine.Menu.Button2f;
import RenderEngine.Model.ModelTexture;
import RenderEngine.Model.RawModel;
import RenderEngine.Model.TexturedModel;
import Tools.Maths.Cubef;
import Tools.Maths.Vector2f;
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

	private int SelectedButton = 1;
	private static int CURRENT_STAGE = 1;
	public static final int 
	STAGE_OVERWORLD = 0,
	STAGE_MENU = 1;

	public void run(){
		start();
		Renderer renderer = new Renderer();
		Loader loader = new Loader();
		GLU.gluLookAt(0, 0, 0, 0.0f, 0.0f, -1.0f, 0, 1, 0);
		
		RawModel[] model = new RawModel[Settings.hb.length];
		
		//Load hitboxes as models
		for(int i = 0; i<model.length; i++){
			Cubef temp = new Cubef(new Vector3f(Settings.hb[i].x, Settings.hb[i].y, 0f), new Vector3f(Settings.hb[i].x+Settings.hb[i].width, Settings.hb[i].y+Settings.hb[i].height, 1f));
			model[i] = loader.loadToVAO(temp.getVertices(), temp.getIndices());
			model[i].setRGBA(1f, 0.3f, 0.3f, 0f);
		}
		
		long MenuLastInput = System.currentTimeMillis();
		while(!Display.isCloseRequested()){
			renderer.prepare();
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			
			switch(CURRENT_STAGE){
			case STAGE_OVERWORLD:
				MainControl.Paused = false;
				Camera.process();
				GL11.glTranslatef(Camera.getLocation().x, Camera.getLocation().y, Camera.getLocation().z);
				GL11.glRotatef(10, Camera.getRotation().x, Camera.getRotation().y, Camera.getRotation().z);
				
				//Light position
				FloatBuffer Location = BufferUtils.createFloatBuffer(16);
		        Location.put(new float[]{-Camera.getLocation().x, -Camera.getLocation().y , 3,1});
		        Location.flip();
		        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, Location);
		        
		        //Draw hitboxes
				for(int i = 0; i<model.length; i++){
					renderer.render(model[i]);
				}
				
				
				//Draw players
				Player[] User = Settings.User.clone();
				for(int i = 0; i<User.length; i++){
					Cubef temp1 = new Cubef(new Vector3f(User[i].getLocation().x, User[i].getLocation().y, 0.2f), new Vector3f(User[i].getLocation().x+User[i].getSize().x, User[i].getLocation().y+User[i].getSize().y, 0.2f+User[i].getSize().x));
					RawModel player = loader.loadToVAO(temp1.getVertices(), temp1.getIndices());
					player.setRGBA(0, 0, 0, 0);
				
					renderer.render(player);
				}
				
				break;
			case STAGE_MENU:
				MainControl.Paused = true;
				//Light position
				FloatBuffer Location1 = BufferUtils.createFloatBuffer(16);
		        Location1.put(new float[]{6.3f, -5.3f , 0f,1});
		        Location1.flip();
		        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, Location1);
				
				//GL11.glTranslatef(-6.3f,5.3f,-10f);
				GLU.gluLookAt(0, 0, 10, 0f, 0.0f, -10.0f, 0, 1, 0);
				Button2f.cycle();
				
				Button2f[] Button = new Button2f[3];

				Button[0] = new Button2f(new Vector2f(0,0), new Vector2f(30,10));
				Button[1] = new Button2f(new Vector2f(0,12), new Vector2f(30,10));
				Button[2] = new Button2f(new Vector2f(0,24), new Vector2f(30,10));
				
				if(Keyboard.isKeyDown(Keyboard.KEY_W) && System.currentTimeMillis()-MenuLastInput > 100){
					SelectedButton++;
					MenuLastInput = System.currentTimeMillis();
				}
				
				if(SelectedButton >= Button.length){
					SelectedButton = 0;
				}
				
				for(Button2f b:Button){
					Cubef temp = b.getCubef();
					ModelTexture texture = new ModelTexture(loader.loadTexture("test"));
					float[] vet = {
							0, 0, -10f,
							0,3, -10f,
							3,3, -10f,
							3,0, -10f
					};
					int[] ind = {
							0,3,1, 3,2,1 
					};
					float[] tex = {
							0,0,
							0,1,
							1,1,
							1,0
					};
					
					
					//This is me just testing 
					//GL13.glActiveTexture(GL13.GL_TEXTURE0);
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
					
					GL11.glBegin(GL11.GL_QUADS);
					GL11.glColor4f(0, 1, 0, 0);
					GL11.glNormal3f(0.0f, 0.0f, 1.0f);
					
					GL11.glTexCoord2d(0, 0);
					GL11.glVertex3f(0f, 0f, -10f);
					
					GL11.glTexCoord2d(0, 1);
					GL11.glVertex3f(0f, 3f, -10f);
					
					GL11.glTexCoord2d(1, 1);
					GL11.glVertex3f(3f, 3f, -10f);
					
					GL11.glTexCoord2d(1, 0);
					GL11.glVertex3f(3f, 0f, -10f);
					
					GL11.glEnd();
					
					RawModel m = loader.loadToVAO(temp.getVertices(), temp.getIndices());
					
					if(SelectedButton == b.getID()){
						//m.setRGBA(1, 0.1f, 0.1f. 0);
						TexturedModel texturedModel = new TexturedModel(loader.loadToVAO(vet, tex, ind), texture);
						//renderer.render(texturedModel);
						
						//renderer.render(loader.loadToVAO(vet, ind));
					}else{
						m.setRGBA(1, 0, 0, 0);
						renderer.render(m);
					}
				}
				break;
			}
			

			processCamera();
			DisplayManager.update();
		}
		
		MainControl.CloseRequest = true;
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
