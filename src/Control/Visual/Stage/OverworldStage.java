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
import RenderEngine.Model.Animation;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;

public class OverworldStage extends Stage{

	private List<Model> hb;
	private Animation playerModel;
	
	public OverworldStage(){
		super();
		playerModel = new Animation("Cube/Spin", 500);
		restartTime = Math.round(System.nanoTime()/1000000000);
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
	
	
	/*
	 TODO - Make gamemode hierachy 
	 */
	
	private int winnerID = -1;
	private boolean running = true;
	private float restartTime = 0;
	
	private boolean hasFinished(){
		if(running){
			int i = 0;
			for(Player p: Settings.User){
				if(p.killCount >= 3){
					winnerID = i;
					restartTime = Camera.getLERPTime();
					
					int n = 0;
					for(Player pl: Settings.User){
						if(n != winnerID){
							pl.kill();
						}
						n++;
					}
					
					running = false;
					return true;
				}
				i++;
			}
			running = true;
			return false;
		}else{
			return true;
		}
	}
	
	protected void updateInfo(){
		for(Hitbox hb: Settings.hb){
			hb.update();
		}
		Damage.updateDamage();
		Shield.updateShields();
		
		//Has game ended
		if(!hasFinished()){
			for(Player p: Settings.User){
				p.update();
			}
		}else{
			int i = 0;
			for(Player p: Settings.User){
				if(i == winnerID){
					float[] RGBA = p.getRGBA();
					p.update();
					Camera.setRGB(RGBA[0], RGBA[1], RGBA[2]);
					
				}else{
					p.kill();
				}
				i++;
			}
			int Dif = 10 + (int) Math.round((restartTime-Camera.getLERPTime())/1000000000);
			
			//Reset
			if(Dif < 0){
				running = true;
				winnerID = -1;
				for(Player p: Settings.User){
					p.killCount = 0;
					p.kill();
				}
				Settings.issueCommand("reset_stage");
			}
			
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
		
		//Draw IDLE player name
		int playerTrack = 0;
		for(Player p: player){
			playerTrack++;
				if(!p.isDead() && p.isPlayerIDLE()){
					Vector3f location = new Vector3f(p.getLERPLocation().x-0.8f, p.getLERPLocation().y+0.3f, p.getLERPLocation().z+1.5f);
					
					float[] RGBA = Camera.getInverseRGBA();
					text.setRGBA(RGBA[0], RGBA[1], RGBA[2], 1);
					text.drawText("PLAYER " + playerTrack, location, 0.04f, 8f);

			}
		}
		
		//Game ended
		if(!running){
			int Dif = 10 + (int) Math.round((restartTime-Camera.getLERPTime())/1000000000);

			float[] RGBA = Camera.getInverseRGBA();
			Vector3f location = new Vector3f(-0.5f,0,-1.5f);
			location.x -= Camera.getLERPLocation().x;
			location.y -= Camera.getLERPLocation().y;
			location.z -= Camera.getLERPLocation().z;
			
			text.setRGBA(RGBA[0], RGBA[1], RGBA[2], 1);
			text.drawText("Restart in:\n     " + Dif, location, 0.01f, 8f);
		}
		
		
		//Draw HUD
		Stencil.enable();
		GL11.glDisable(GL11.GL_LIGHTING);
		int ID = 0;
		for(Player p: player){
			float scale =0.3f;
			Model core = playerModel.getCurrentFrame().clone();
			
			Vector3f location = getHUDLocation(ID);
			core.setLocation(location);
			
			Model outline = core.clone();
			outline.getLocation().y-=0.24f*scale;
			outline.scaleBy(1.6f*scale*6);
			outline.setRGBA(0,0,0,1);
			Renderer.render(outline);

			float[] RGB = p.getRGBA();
			
			Model colour = core.clone();
			colour.getLocation().y-=0.2f*scale;
			colour.scaleBy(1.5f*scale*6);
			colour.setRGBA(RGB[0], RGB[1], RGB[2], 1);
			Renderer.render(colour);
			
			GL11.glEnable(GL11.GL_LIGHTING);
			core.scaleBy(scale*6);
			Renderer.render(core);
			GL11.glDisable(GL11.GL_LIGHTING);
			
			location.x+=0.08f;
			location.y+=0.1f;

			float[] RGBA = Camera.getInverseRGBA();
			String data = "" + Math.round(p.getFactor()*100) + "%\n" + p.killCount + " kills";
			float textSize = 4000/player.size();
			textSize/=1000;
			
			text.setRGBA(RGBA[0], RGBA[1], RGBA[2], 1);
			text.drawText(data, location, 0.008f*textSize, 8f);
			
			ID++;
		}

		Stencil.cylce();
		GL11.glEnable(GL11.GL_LIGHTING);
		Stencil.disable();
		
	}
	
	private Vector3f getHUDLocation(int ID){	
		Vector3f location = new Vector3f(-2f,-2.0f, -2.5f);
		location.x -= Camera.getLERPLocation().x;
		location.y -= Camera.getLERPLocation().y;
		location.z -= Camera.getLERPLocation().z;
		
		float width = 4*10000/(Settings.User.size()+2);
		width/=10000;
		location.x+=width*(ID+1);
		return location;
	}
	


}
