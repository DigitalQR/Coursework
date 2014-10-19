package Control.Visual.Stage;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import Collision.SquareHitbox;
import Control.Camera;
import Control.MainControl;
import Control.Settings;
import Control.Visual.DisplayControl;
import Entities.Player;
import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;

public class OverworldStage extends Stage{
	private Texture BoxTexture;
	private List<Model> hb;
	
	public void prepare(){
		//Load textures
		//BoxTexture = Loader.loadTexture("Box");
	
		//Setup hitbox model data
		hb = new ArrayList<Model>();
		for(SquareHitbox h: Settings.hb){
			Cubef temp = new Cubef(new Vector3f(h.getLocation().x, h.getLocation().y, 0f), new Vector3f(h.getLocation().x+h.getSize().x, h.getLocation().y+h.getSize().y, 1f));
			Model m = new Model(temp);
			m.setTexture(BoxTexture);
			hb.add(m);
		}
	}
	
	public void update(){
		@SuppressWarnings("unchecked")
		ArrayList<Player> User = (ArrayList<Player>) Settings.User.clone();
		
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
			if(Settings.toggles.get("d_hitbox")){
				Renderer.render(p.getHitbox());
			}
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
