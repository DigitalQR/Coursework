package Control.Visual.Stage;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import Collision.Hitbox;
import Control.Camera;
import Control.Settings;
import Control.Visual.Menu.Assets.Core.Input;
import Control.Visual.Stage.Core.Stage;
import Entities.Player;
import Entities.Assets.Damage;
import Entities.Assets.Shield;
import RenderEngine.Renderer;
import RenderEngine.Stencil;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;

public class OverworldStage extends Stage{

	private List<Model> hb;
	
	public OverworldStage(){
		super();
		generateHitboxModels();
	}
	
	public void generateHitboxModels(){
		hb = new ArrayList<Model>();
		for(Hitbox h: Settings.hb){
			if(h.getType() == Hitbox.TYPE_STATIC){
				Cubef temp = new Cubef(new Vector3f(h.getLocation().x, h.getLocation().y, 0f), new Vector3f(h.getLocation().x+h.getSize().x, h.getLocation().y+h.getSize().y, 1f));
				Model m = new Model(temp);
				hb.add(m);
			}
		}
	}
	
	protected void updateInfo(){
		for(Hitbox hb: Settings.hb){
			hb.update();
		}
		Damage.updateDamage();
		Shield.updateShields();
		
		for(Player p: Settings.User){
			p.update();
		}
		Camera.process();
		
		if(Input.isKeyPressed(Input.KEY_PAUSE)){
			Stage.setStage(Stage.getStage("menu"));
			Input.recieved();
		}
	}

	protected void updateUI(){
		//Gather player data
		@SuppressWarnings("unchecked")
		ArrayList<Player> player = (ArrayList<Player>) Settings.User.clone();
		
		//Draw damage
		for(Damage d: Damage.getDamageInfo()){
			Renderer.render(d.getModel());
			
			if(Settings.toggles.get("d_hitbox")){
				Cubef temp = new Cubef(new Vector3f(d.getLocation().x, d.getLocation().y, 0f), new Vector3f(d.getLocation().x+d.getSize().x, d.getLocation().y+d.getSize().y, 1f));
				Model m = new Model(temp);
				m.setRGBA(1, 0, 0, 0.7f);
				Renderer.render(m);
			}
		}
		
		//Draw shield
		for(Shield s: Shield.getShieldInfo()){
			Renderer.render(s.getModel());
			if(Settings.toggles.get("d_hitbox")){
				Cubef temp = new Cubef(new Vector3f(s.getLocation().x, s.getLocation().y, 0f), new Vector3f(s.getLocation().x+s.getSize().x, s.getLocation().y+s.getSize().y, 1f));
				Model m = new Model(temp);
				m.setRGBA(1, 0, 0, 0.7f);
				Renderer.render(m);
			}
		}
		
		//Player outline
		Stencil.enable();
		GL11.glDisable(GL11.GL_LIGHTING);
		for(Player p: player){
			if(!p.isDead()){
				Model m = p.getModel();
				m.getLocation().y-=0.24f;
				m.scaleBy(1.6f);
				if(p.stunned()){
					m.setRGBA(1, 1, 1, 1);
				}else{
					m.setRGBA(0, 0, 0, 1);
				}
				Renderer.render(m);

				float[] RGBA = p.getRGBA();
				Model m1 = p.getModel();
				m1.getLocation().y-=0.2f;
				m1.scaleBy(1.5f);
				m1.setRGBA(RGBA[0], RGBA[1], RGBA[2], RGBA[3]);
				Renderer.render(m1);
			}
		}
		GL11.glEnable(GL11.GL_LIGHTING);
		
		//Draw player
		Stencil.cylce();
		for(Player p: player){
			if(!p.isDead()){
				Renderer.render(p.getModel());
				if(Settings.toggles.get("d_hitbox")){
					Renderer.render(p.getHitbox());
				}
			}
		}
		Stencil.disable();

		//Draw hitboxes
		for(Model Box:hb){
			Renderer.render(Box);
		}
		
		//Draw IDLE player name
		if(Settings.toggles.get("s_afk_names")){
			int playerTrack = 0;
			for(Player p: player){
				playerTrack++;
					if(!p.isDead() && p.isPlayerIDLE()){
						Vector3f location = new Vector3f(p.getLERPLocation().x-0.8f, p.getLERPLocation().y+0.3f, p.getLERPLocation().z+1f);
						Vector3f location2 = new Vector3f(location.x-0.006f, location.y-0.006f, location.z-0.001f);
						float[] colour = Camera.getInverseRGBA();
						text.setRGBA(colour[0], colour[1], colour[2], 1f);
						text.drawText("PLAYER " + playerTrack, location, 0.04f, 8f);
	
						text.setRGBA(0, 0, 0, 1f);
						text.drawText("PLAYER " + playerTrack, location2, 0.04f, 8f);
				}
			}
		}		

		//Draw boundary
		Cubef[] sides = {
				new Cubef(new Vector3f(-1000,-1000,0.3f), new Vector3f(Settings.boundary.getLocation().x,1000,0.3f)),
				new Cubef(new Vector3f(-1000,-1000,0.2f), new Vector3f(1000,Settings.boundary.getLocation().y,0.2f)),
				new Cubef(new Vector3f(Settings.boundary.getLocation().x+Settings.boundary.getSize().x, -1000, 0.1f), new Vector3f(1000,1000,0.1f)),
				new Cubef(new Vector3f(-1000,Settings.boundary.getLocation().y+Settings.boundary.getSize().y,0f), new Vector3f(1000,1000,0f))
		};
		
		for(Cubef c: sides){
			Model m = new Model(c);
			m.setRGBA(0, 0, 0, 0.6f);
			Renderer.render(m);
		}
		
	}
	

}
