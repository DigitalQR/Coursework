package Control.Visual.Stage;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import Collision.Hitbox;
import Control.Camera;
import Control.MainControl;
import Control.Settings;
import Control.Audio.Sound;
import Control.Visual.DisplayControl;
import Control.Visual.Font;
import Control.Visual.Menu.Assets.TextBox;
import Entities.Player;
import Entities.Assets.Damage;
import Entities.Assets.Shield;
import RenderEngine.Renderer;
import RenderEngine.Stencil;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector3f;

public class OverworldStage extends Stage{
	private List<Model> hb;
	private List<Integer> dynamicHb = new ArrayList<Integer>();
	private Font font;
	private int winner = -1;
	private int resetTime = 10;
	private float endTime = 0;
	private static float LERPTime = 0;
	
	private int winKillCount = 3;
	
	private Sound music;
	
	public void prepare(){	
		font = new Font("Font/Default");
		winner = -1;
		endTime = 0;
		
		if(music == null){
			String[] files = {"Music/Open Hexagon - Starship Showdown", "Music/Open Hexagon - Pointless - Jack Russel", "Music/Savant - Melody Circus"};
			int index = Toolkit.RandomInt(0, files.length-1);
			
			music = new Sound(files[index]);
			music.setLoop(true);
			music.doNotDestroyOnFinish();
			music.play();
		}
		
		//Setup hitbox model data
		int track = 0;
		hb = new ArrayList<Model>();
		for(Hitbox h: Settings.hb){
			if(h.getType() == Hitbox.TYPE_STATIC){
				Cubef temp = new Cubef(new Vector3f(h.getLocation().x, h.getLocation().y, 0f), new Vector3f(h.getLocation().x+h.getSize().x, h.getLocation().y+h.getSize().y, 1f));
				Model m = new Model(temp);
				hb.add(m);
			}else{
				dynamicHb.add(track);
			}
			track++;
		}
	}
	
	public static float getLERPTime(){
		return LERPTime;
	}
	
	public void update(){
		LERPTime = System.nanoTime()-MainControl.UPS;
		
		@SuppressWarnings("unchecked")
		ArrayList<Player> User = (ArrayList<Player>) Settings.User.clone();

		for(Player p: User){
			p.processLERPLocation();
		}
		
		Camera.processLERP();
		Vector3f camLocation = new Vector3f(Camera.getLERPLocation().x, Camera.getLERPLocation().y, Camera.getLERPLocation().z);
		Camera.focus();
		

		TextBox tb = new TextBox(new Vector3f(0,0,-10), new Vector3f(7,7,0.5f), "Test", "This is a test description that\nI am going to have to manage.");
		tb.draw();
		
		//Draw HUD
		if(winner == -1){
			//Exit to menu
			if(Settings.User.get(0).isKeyPressed(Settings.User.get(0).getControlScheme().KEY_START)){
				MainControl.Paused = true;
				DisplayControl.setStage(DisplayControl.STAGE_MENU);
			}
			
			
			String[] playerInfo = new String[Settings.User.size()];
			
			for(int i = 0; i<User.size(); i++){
				int factor = (int) Math.round((double)(User.get(i).getFactor()*100));
				playerInfo[i] = "";
				if(User.get(i).isDead()){
					playerInfo[i] += (int)Math.round(User.get(i).getRespawnTimeRemaining()/1000000000) + "<DEAD>";
				}
				playerInfo[i] += "Player " + (i+1) + ": " + factor + "\nKills: " + User.get(i).killCount;
				
				//Detecting winner
				if(User.get(i).killCount >= winKillCount){
					winner = i+1;
					endTime = System.nanoTime();
					
					for(int n = 0; n<Settings.User.size(); n++){
						if(n+1 != winner){
							Settings.User.get(n).health.lastHit = null;
							Settings.User.get(n).kill();
							Settings.User.get(n).killCount = 0;
						}
					}
					 
				}
			}
			
			int loops = 0;
			float xOffset = 0;
			float yOffset = 0;

			//Position of info on screen
			for(int i = 0; i<Settings.User.size(); i++){
				switch(loops){
				case 1:
					xOffset = 2.2f;
					yOffset = 0;
					break;
				case 2:
					xOffset = 2.2f;
					yOffset = -3;
					break;
				case 3:
					xOffset = 0;
					yOffset = -3;
					break;
				case 4:
					xOffset = 0;
					yOffset = -0.2f;
					break;
				case 5:
					xOffset = 2.2f;
					yOffset = -0.2f;
					break;
				case 6:
					xOffset = 2.2f;
					yOffset = -2.8f;
					break;
				case 7:
					xOffset = 0;
					yOffset = -2.8f;
					break;
				default:
					xOffset = 0;
					yOffset = 0;
					break;
				}

				Vector3f location = new Vector3f(-camLocation.x-1.6f+xOffset, -camLocation.y+1.5f+yOffset, -camLocation.z-2f);
				Vector3f shadowLocation = new Vector3f(-camLocation.x-1.606f+xOffset, -camLocation.y+1.506f+yOffset, -camLocation.z-2.0001f);
				
				
				float[] RGBA = Settings.User.get(i).getRGBA();
				font.setRGBA(RGBA[0], RGBA[1], RGBA[2], 1);
				font.drawText(playerInfo[i], location, 0.008f, 9f);
				
				font.setRGBA(1-DisplayControl.getRGBA()[0], 1-DisplayControl.getRGBA()[1], 1-DisplayControl.getRGBA()[2], 1);
				font.drawText(playerInfo[i], shadowLocation, 0.008f, 9f);
				loops++;
			}
		
		//If there is a winner
		}else{
			int reset = resetTime-Math.round((System.nanoTime()-endTime)/1000000000);
			
			font.setRGBA(1-DisplayControl.getRGBA()[0], 1-DisplayControl.getRGBA()[1], 1-DisplayControl.getRGBA()[2], 1);
			font.drawText("Player " + winner + " Wins!\nReset in " + reset, new Vector3f(-camLocation.x-0.6f, -camLocation.y, -camLocation.z-2f), 0.01f, 9f);
			font.setRGBA(0, 0, 0, 1);
			font.drawText("Player " + winner + " Wins!\nReset in " + reset, new Vector3f(-camLocation.x-0.606f, -camLocation.y+0.006f, -camLocation.z-2.0001f), 0.01f, 9f);
			
			for(int i = 0; i<Settings.User.size(); i++){
				if(i != winner-1){
					Settings.User.get(i).health.lastHit = null;
					Settings.User.get(i).kill();
					Settings.User.get(i).killCount = 0;
				}else{
					DisplayControl.r = Settings.User.get(i).getRGBA()[0];
					DisplayControl.g = Settings.User.get(i).getRGBA()[1];
					DisplayControl.b = Settings.User.get(i).getRGBA()[2];
				}
			}
			
			if(reset == 0){
				Settings.randomHitboxGen();
				
				for(int i = 0; i<Settings.User.size(); i++){
					Settings.User.get(i).health.lastHit = null;
					Settings.User.get(i).kill();
					Settings.User.get(i).killCount = 0;
				}
				prepare();
			}
		}
		//Light position
		FloatBuffer Location = BufferUtils.createFloatBuffer(16);
        Location.put(new float[]{-camLocation.x, -camLocation.y , 3,1});
        Location.flip();
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, Location);
		

    	try{
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
		}catch(NullPointerException | ConcurrentModificationException e){
			e.printStackTrace();
		}


		//Player outline
		Stencil.enable();
		GL11.glDisable(GL11.GL_LIGHTING);
		for(Player p: User){
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
		for(Player p: User){
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
		for(int i: dynamicHb){
			Hitbox h = Settings.hb.get(i);
			Cubef temp = new Cubef(new Vector3f(h.getLocation().x, h.getLocation().y, 0f), new Vector3f(h.getLocation().x+h.getSize().x, h.getLocation().y+h.getSize().y, 1f));
			Model m = new Model(temp);
			Renderer.render(m);
		}

		//Draw IDLE player name
		int playerTrack = 0;
		for(Player p: User){
			playerTrack++;
				if(!p.isDead() && p.isPlayerIDLE()){
					Vector3f location = new Vector3f(p.getLERPLocation().x-0.8f, p.getLERPLocation().y+0.3f, p.getLERPLocation().z+1f);
					Vector3f location2 = new Vector3f(location.x-0.006f, location.y-0.006f, location.z-0.001f);
					float[] colour = DisplayControl.getInverseRGBA();
					font.setRGBA(colour[0], colour[1], colour[2], 1f);
					font.drawText("PLAYER " + playerTrack, location, 0.04f, 8f);

					font.setRGBA(0, 0, 0, 1f);
					font.drawText("PLAYER " + playerTrack, location2, 0.04f, 8f);
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

	public void switchToUpdate(){
	}
	
	public void switchFromUpdate(){
	}

}
