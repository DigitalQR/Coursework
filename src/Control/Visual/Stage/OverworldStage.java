package Control.Visual.Stage;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import Collision.Hitbox;
import Control.Camera;
import Control.Settings;
import Control.Visual.Menu.Assets.Core.Input;
import Control.Visual.Stage.Core.Stage;
import Entities.Player;
import Entities.Powerup;
import Entities.Assets.Damage;
import Entities.Assets.Shield;
import Entities.Tools.Health;
import Level.RandomWorld;
import RenderEngine.Renderer;
import RenderEngine.Stencil;
import RenderEngine.Model.Animation;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;

public class OverworldStage extends Stage{

	private Animation playerModel;
	private final int RESET_TIME = 5;
	
	public OverworldStage(){
		super();
		playerModel = new Animation("Cube/Spin", 500);
		restartTime = Math.round(System.nanoTime()/1000000000);
		Settings.setWorld(new RandomWorld());
	}	
	
	private int winnerID = -1;
	private boolean running = true;
	private float restartTime = 0;
	
	private boolean hasFinished(){
		if(running){
			winnerID = GamemodeStage.isGameOver();
			if(winnerID != -1){
				restartTime = Camera.getLERPTime();
				
				int n = 0;
				for(Player pl: Settings.User){
					if(n != winnerID){
						pl.kill(false);
					}
					n++;
				}
				
				running = false;
				return true;
			}else{
				running = true;
				return false;
			}
		}else{
			return true;
		}
	}
	
	protected void updateInfo(){
		for(Hitbox hb: Settings.getWorld().getHitboxList()){
			hb.update();
		}
		Damage.updateDamage();
		Shield.updateShields();
		Powerup.updatePowerups();
		
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
					p.kill(false);
				}
				i++;
			}
			int Dif = RESET_TIME + (int) Math.round((restartTime-Camera.getLERPTime())/1000000000);
			
			//Reset
			if(Dif < 0){
				reset();
			}
			
		}
		Camera.process();
		
		if(Input.isKeyPressed(Input.KEY_PAUSE) && !Settings.isClientActive()){
			if(!Settings.isHostActive()){
				Stage.setStage(Stage.getStage("menu"));
			}else{
				Settings.host.addCommand("Sst" + Stage.getStageID("gamemode") + ";");
				Stage.setStage(Stage.getStage("gamemode"));
				
			}
			Input.recieved();
		}
	}

	public void reset(){
		running = true;
		winnerID = -1;
		
		if(!Settings.isClientActive()){
			GamemodeStage gm = (GamemodeStage) Stage.getStage("gamemode");
			gm.cycleWorldQueue();
		}

		for(Player p: Settings.User){
			p.reset();
		}
		Health.startTime = Camera.getLERPTime();
	}
	
	float track = 0;
	
	protected void updateUI(){
		//Gather player data
		@SuppressWarnings("unchecked")
		ArrayList<Player> player = (ArrayList<Player>) Settings.User.clone();

		//Draw shield
		for(Shield s: Shield.getShieldInfo()){
			Player p = (Player) s.getParent();
			float[] RGBA = p.getRGBA();
			Model m = s.getModel();
			m.setRGBA(RGBA[0], RGBA[1], RGBA[2], RGBA[3]);
			Renderer.render(m);
			
			
			if(Settings.toggles.get("d_hitbox")){
				Cubef temp = new Cubef(new Vector3f(s.getLocation().x, s.getLocation().y, 0f), new Vector3f(s.getLocation().x+s.getSize().x, s.getLocation().y+s.getSize().y, 1f));
				Model m1 = new Model(temp);
				m1.setRGBA(1, 0, 0, 0.7f);
				Renderer.render(m1);
			}
		}

		//Draw damage
		for(Damage d: Damage.getDamageInfo()){
			Player p = (Player) d.getParent();
			float[] RGBA = p.getRGBA();
			Model m = d.getModel();
			m.setRGBA(RGBA[0], RGBA[1], RGBA[2], RGBA[3]);
			Renderer.render(m);
			
			if(Settings.toggles.get("d_hitbox")){
				Cubef temp = new Cubef(new Vector3f(d.getLocation().x, d.getLocation().y, 0f), new Vector3f(d.getLocation().x+d.getSize().x, d.getLocation().y+d.getSize().y, 1f));
				Model m1 = new Model(temp);
				m1.setRGBA(1, 0, 0, 0.7f);
				Renderer.render(m1);
			}
		}

		//Draw hitboxes
		for(Model Box: Settings.getWorld().getBackRenderList()){
			Renderer.render(Box);
		}
		
		//Draw powerups
		for(Powerup p: Powerup.getPowerUps()){
			Model m = p.getModel();
			Renderer.render(m);
			
			if(Settings.toggles.get("d_hitbox")){
				Cubef temp = new Cubef(new Vector3f(p.getLocation().x, p.getLocation().y, 0f), new Vector3f(p.getLocation().x+p.getSize().x, p.getLocation().y+p.getSize().y, 1f));
				Model m1 = new Model(temp);
				m1.setRGBA(1, 0, 0, 0.7f);
				Renderer.render(m1);
			}
		}
		
		//Player outline
		Stencil.enable();
		GL11.glDisable(GL11.GL_LIGHTING);
		for(Player p: player){
			if(!p.isDead()){
				//Black outline
				Model m = p.getModel();
				m.getLocation().y-=0.24f;
				m.scaleBy(1.6f);
				if(p.stunned()){
					m.setRGBA(1, 1, 1, 1);
				}else{
					m.setRGBA(0, 0, 0, 1);
				}
				Renderer.render(m);
				
				Model pow = p.getPowerUpModel();
				if(pow != null){
					pow.scaleBy(1.6f);
					pow.getLocation().y-=0.24f;
					if(p.stunned()){
						pow.setRGBA(1, 1, 1, 1);
					}else{
						pow.setRGBA(0, 0, 0, 1);
					}
					Renderer.render(pow);
				}
				
				//RGBA outline
				float[] RGBA = p.getRGBA();
				Model m1 = p.getModel();
				m1.getLocation().y-=0.2f;
				m1.scaleBy(1.5f);
				m1.setRGBA(RGBA[0], RGBA[1], RGBA[2], RGBA[3]);
				Renderer.render(m1);

				Model pow1 = p.getPowerUpModel();
				if(pow1 != null){
					pow1.scaleBy(1.5f);
					pow1.getLocation().y-=0.2f;
					pow1.setRGBA(RGBA[0], RGBA[1], RGBA[2], RGBA[3]);
					Renderer.render(pow1);
				}
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

		
		//Draw IDLE player name
		int playerTrack = 0;
		for(Player p: player){
			playerTrack++;
				if(!p.isDead() && p.isPlayerIDLE()){
					Vector3f location = new Vector3f(p.getLERPLocation().x-0.8f, p.getLERPLocation().y+0.3f, 1.5f);
					
					float[] RGBA = Camera.getInverseRGBA();
					text.setRGBA(RGBA[0], RGBA[1], RGBA[2], 1);
					text.drawText("PLAYER " + playerTrack, location, 0.04f, 8f);

			}
		}
		//Draw hitboxes
		for(Model Box:Settings.getWorld().getFrontRenderList()){
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
		
		//Game ended
		if(!running){
			int Dif = RESET_TIME + (int) Math.round((restartTime-Camera.getLERPTime())/1000000000);

			float[] RGBA = Camera.getInverseRGBA();
			Vector3f location = new Vector3f(-0.5f,0,-1.5f);
			location.x -= Camera.getLERPLocation().x;
			location.y -= Camera.getLERPLocation().y;
			location.z -= Camera.getLERPLocation().z;
			
			text.setRGBA(RGBA[0], RGBA[1], RGBA[2], 1);
			text.drawText("Next Round in:\n     " + Dif, location, 0.01f, 8f);
		}
		
		
		//Draw HUD
		Stencil.enable();
		GL11.glDisable(GL11.GL_LIGHTING);
		
		if(Health.timeCap != -1){
			Vector3f location = new Vector3f(-0.3f,1.5f,-2.5f);
			location.x -= Camera.getLERPLocation().x;
			location.y -= Camera.getLERPLocation().y;
			location.z -= Camera.getLERPLocation().z;
			
			int dif = Health.timeCap*60+7 - (int) Math.round((Camera.getLERPTime()-Health.startTime)/1000000000);
			if(dif < 0){
				dif = 0;
			}
			
			int seconds = 0;
			while(dif >= 60){
				seconds++;
				dif-=60;
			}
			
			text.setRGBA(0, 0, 0, 1);
			text.drawText("\n" + String.format("%02d:%02d", seconds, dif), location, 0.015f, 8f);
		}
		
		if(Settings.isClientActive()){
			Vector3f location = new Vector3f(-0.3f,1.5f,-2.5f);
			location.x -= Camera.getLERPLocation().x;
			location.y -= Camera.getLERPLocation().y;
			location.z -= Camera.getLERPLocation().z;
			
			text.setRGBA(0, 0, 0, 1);
			text.drawText("Ping: " + Settings.client.getPing(), location, 0.01f, 8f);
		}
		
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
			
			String data = "" + Math.round(p.getFactor()*100) + "%\n";
			if(Health.stockCap != -1){
				if(p.getStock() < 10){
					data += p.getStock() + " lives";
				}else{
					data += p.getStock() + "lives";
				}
			}else{
				if(p.killCount < 10){
					data += p.killCount + " kills";
				}else{
					data += p.killCount + "kills";
				}
			}
			
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
