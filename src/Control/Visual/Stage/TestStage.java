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
import Entities.Tools.ControlScheme;
import RenderEngine.Loader;
import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;

public class TestStage extends Stage{
	private Texture BoxTexture, PlaneTexture;
	private Model[] hb;
	
	public void prepare(){
		//Load textures
		BoxTexture = Loader.loadTexture("Box");
		PlaneTexture = Loader.loadTexture("Plane");
		
		//Setup hitbox model data
		hb = new Model[Settings.hb.length];
		for(int i = 0; i<Settings.hb.length; i++){
			Cubef temp = new Cubef(new Vector3f(Settings.hb[i].x, Settings.hb[i].y, 0f), new Vector3f(Settings.hb[i].x+Settings.hb[i].width, Settings.hb[i].y+Settings.hb[i].height, 1f));
			hb[i] = new Model(temp);
			hb[i].setTexture(BoxTexture);
		}
		
	}
	
	public void update(){
		for(Player p: Settings.User){
			if(p.isKeyPressed(ControlScheme.KEY_START)){
				MainControl.Paused = true;
				DisplayControl.setStage(DisplayControl.STAGE_MENU);
				break;
			}
		}
		
		Camera.process();
		GL11.glTranslatef(Camera.getLocation().x, Camera.getLocation().y, Camera.getLocation().z);
		
		//Light position
		FloatBuffer Location = BufferUtils.createFloatBuffer(16);
        Location.put(new float[]{-Camera.getLocation().x, -Camera.getLocation().y , 3,1});
        Location.flip();
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, Location);
        
        //Draw hitboxes
		for(Model Box:hb){
			Renderer.render(Box);
		}
		//Draw players
		Player[] User = Settings.User.clone();
		for(int i = 0; i<User.length; i++){
			Cubef temp1 = new Cubef(new Vector3f(User[i].getLocation().x, User[i].getLocation().y, 0.2f), new Vector3f(User[i].getLocation().x+User[i].getSize().x, User[i].getLocation().y+User[i].getSize().y, 0.2f+User[i].getSize().x));
			Model m = new Model(temp1);
			m.setTexture(PlaneTexture);
			Renderer.render(m);
		}
	}

}
