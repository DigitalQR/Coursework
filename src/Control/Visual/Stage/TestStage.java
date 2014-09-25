package Control.Visual.Stage;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import Control.Camera;
import Control.MainControl;
import Control.Settings;
import Control.Visual.DisplayControl;
import Entities.Player;
import RenderEngine.Loader;
import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;

public class TestStage extends Stage{
	private Texture BoxTexture;
	private Model[] hb;
	
	public void prepare(){
		//Load textures
		BoxTexture = Loader.loadTexture("Box");
	
		//Setup hitbox model data
		hb = new Model[Settings.hb.length];
		for(int i = 0; i<Settings.hb.length; i++){
			Cubef temp = new Cubef(new Vector3f(Settings.hb[i].x, Settings.hb[i].y, 0f), new Vector3f(Settings.hb[i].x+Settings.hb[i].width, Settings.hb[i].y+Settings.hb[i].height, 1f));
			hb[i] = new Model(temp);
			hb[i].setTexture(BoxTexture);
		}
		
		
	}
	
	public void update(){
		Player[] User = Settings.User.clone();
		
		for(Player p: User){
			if(p.isKeyPressed(p.getControlScheme().KEY_START)){
				MainControl.Paused = true;
				DisplayControl.setStage(DisplayControl.STAGE_MENU);
				break;
			}
		}
		
		Camera.process(User);
		GL11.glTranslatef(Camera.getLocation().x, Camera.getLocation().y, Camera.getLocation().z);
		
		//Draw players
		for(Player p: User){
			Renderer.render(p.getModel());
		}
		
		//Light position
		FloatBuffer Ambient = BufferUtils.createFloatBuffer(16);
		Ambient.put(new float[]{0.2f, 0.2f, 0.6f, 0.7f});
        Ambient.flip();
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, Ambient);
        
		FloatBuffer Location = BufferUtils.createFloatBuffer(16);
        Location.put(new float[]{-Camera.getLocation().x, -Camera.getLocation().y , 3,1});
        Location.flip();
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, Location);
        
        
        //Draw hitboxes
		for(Model Box:hb){
			Renderer.render(Box);
		}
		
	}

}
