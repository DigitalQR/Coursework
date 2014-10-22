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
import RenderEngine.Stencil;
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
			p.processLERPLocation();
			if(p.isKeyPressed(p.getControlScheme().KEY_START)){
				MainControl.Paused = true;
				DisplayControl.setStage(DisplayControl.STAGE_MENU);
				break;
			}
		}
		
		Camera.process(User);
		GL11.glTranslatef(Camera.getLocation().x, Camera.getLocation().y, Camera.getLocation().z);
		
		//Draw HUD
		String player = "\n\n";
		for(int i = 0; i<User.size(); i++){
			int factor = (int) Math.round((double)(User.get(i).getFactor()*100));
			if(User.get(i).isDead()){
				player += (int)Math.round(User.get(i).getRespawnTimeRemaining()/1000000000) + "<DEAD>";
			}
			player += "Player " + (i+1) + ": " + factor + "\nDeaths: " + User.get(i).getDeathCount() + "\n\n\n";
		}

		font.setRGBA(1-DisplayControl.getRGBA()[0], 1-DisplayControl.getRGBA()[1], 1-DisplayControl.getRGBA()[2], 1);
		font.drawText(player, new Vector3f(-Camera.getLocation().x-1.6f, -Camera.getLocation().y+1.5f, -Camera.getLocation().z-2f), 0.01f, 9f);
		font.setRGBA(0, 0, 0, 1);
		font.drawText(player, new Vector3f(-Camera.getLocation().x-1.606f, -Camera.getLocation().y+1.506f, -Camera.getLocation().z-2.0001f), 0.01f, 9f);
		
		//Light position
		FloatBuffer Location = BufferUtils.createFloatBuffer(16);
        Location.put(new float[]{-Camera.getLocation().x, -Camera.getLocation().y , 3,1});
        Location.flip();
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, Location);
		
		//Draw damage
		if(Settings.toggles.get("d_damage")){
			try{
				for(Damage d: Damage.getDamageInfo()){
						Renderer.render(d.getModel());
						
						if(Settings.toggles.get("d_hitbox")){
							Cubef temp = new Cubef(new Vector3f(d.getLocation().x, d.getLocation().y, 0f), new Vector3f(d.getLocation().x+d.getSize().x, d.getLocation().y+d.getSize().y, 1f));
							Model m = new Model(temp);
							m.setRGBA(1, 0, 0, 0.7f);
							Renderer.render(m);
						}
				}
			}catch(NullPointerException | ConcurrentModificationException e){
				e.printStackTrace();
			}
		    
			//Draw boundary
			Cubef[] sides = {
					new Cubef(new Vector3f(-1000,-1000,1.3f), new Vector3f(Settings.boundary.getLocation().x,1000,1.3f)),
					new Cubef(new Vector3f(-1000,-1000,1.2f), new Vector3f(1000,Settings.boundary.getLocation().y,1.2f)),
					new Cubef(new Vector3f(Settings.boundary.getLocation().x+Settings.boundary.getSize().x, -1000, 1.1f), new Vector3f(1000,1000,1.1f)),
					new Cubef(new Vector3f(-1000,Settings.boundary.getLocation().y+Settings.boundary.getSize().y,1f), new Vector3f(1000,1000,1f))
			};
			
			for(Cubef c: sides){
				Model m = new Model(c);
				m.setRGBA(0, 0, 0, 0.6f);
				Renderer.render(m);
			}
		}

		
		//Draw player
		for(Player p: User){
			if(!p.isDead()){
				Renderer.render(p.getModel());
				if(Settings.toggles.get("d_hitbox")){
					Renderer.render(p.getHitbox());
				}
			}
		}
	     Stencil.enable();
        //Draw hitboxes
		for(Model Box:hb){
			Renderer.render(Box);
		}
	
		//Draw player outline
		Stencil.cylce();
		int playerTrack = 1;
		for(Player p: User){
				if(!p.isDead()){
					Vector3f location = new Vector3f(p.getLERPLocation().x-0.35f, p.getLERPLocation().y+0.3f, p.getLERPLocation().z);
					Vector3f location2 = new Vector3f(location.x-0.006f, location.y-0.006f, location.z-0.0001f);
					float[] colour = DisplayControl.getInverseRGBA();
					font.setRGBA(colour[0], colour[1], colour[2], 1f);
					font.drawText("PLAYER " + playerTrack++, location, 0.02f, 8f);

					font.setRGBA(0, 0, 0, 1f);
					font.drawText("PLAYER " + (playerTrack-1), location2, 0.02f, 8f);
			}
		}
		Stencil.disable();
		
		
	
	}

}
