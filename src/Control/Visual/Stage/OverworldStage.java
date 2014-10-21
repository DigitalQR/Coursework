package Control.Visual.Stage;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import Collision.SquareHitbox;
import Control.Camera;
import Control.MainControl;
import Control.Settings;
import Control.Visual.DisplayControl;
import Control.Visual.Font;
import Entities.Player;
import Entities.Assets.Damage;
import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;

public class OverworldStage extends Stage{
	private List<Model> hb;
	private Font font;
	
	public void prepare(){	
		font = new Font("Font/Default");
		
		//Setup hitbox model data
		hb = new ArrayList<Model>();
		for(SquareHitbox h: Settings.hb){
			Cubef temp = new Cubef(new Vector3f(h.getLocation().x, h.getLocation().y, 0f), new Vector3f(h.getLocation().x+h.getSize().x, h.getLocation().y+h.getSize().y, 1f));
			Model m = new Model(temp);
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
		
		font.setRGBA(1, 1, 1, 1);
		String player = "";
		for(int i = 0; i<User.size(); i++){
			int factor = (int) Math.round((double)(User.get(i).getFactor()*1000));
			player += "Player " + (i+1) + ": " + factor + "\n";
		}
		
		font.drawText(player, new Vector3f(-Camera.getLocation().x-1.6f, -Camera.getLocation().y+1.5f, -Camera.getLocation().z-2f), 0.01f, 10);
		
		
		
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
		
		//Draw damage
		if(Settings.toggles.get("d_damage")){
			try{
				for(Damage d: Damage.getDamageInfo()){
						Cubef temp = new Cubef(new Vector3f(d.getLocation().x, d.getLocation().y, 0f), new Vector3f(d.getLocation().x+d.getSize().x, d.getLocation().y+d.getSize().y, 1f));
						Model m = new Model(temp);
						m.setRGBA(1, 0, 0, 0.7f);
						Renderer.render(m);
				}
			}catch(NullPointerException | ConcurrentModificationException e){
				e.printStackTrace();
			}
		}
		
	}

}
