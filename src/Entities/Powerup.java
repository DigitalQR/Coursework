package Entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Collision.Hitbox;
import Collision.SquareHitbox;
import Control.Settings;
import Entities.Powerups.PowerPowerUp;
import Entities.Powerups.SpeedPowerUp;
import RenderEngine.Model.Model;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public abstract class Powerup extends Entity{
	
	protected static ArrayList<Powerup> powerups = new ArrayList<Powerup>();
	private static long cooldown = -1;
	protected static final int STANDARD_LIFE = 5000;
	
	public static ArrayList<Powerup> getPowerUps(){
		@SuppressWarnings("unchecked")
		ArrayList<Powerup> temp = (ArrayList<Powerup>) powerups.clone();
		return temp;
	}
	
	public static void updatePowerups(){
		for(Powerup p: getPowerUps()){
			p.update();
		}
		while(powerups.size() < 20 && cooldown < 0){
			if(Math.random() <= 0.5){
				powerups.add(new SpeedPowerUp());
			}else{
				powerups.add(new PowerPowerUp());
			}
			cooldown = Toolkit.RandomInt(0, 5)*30L;
		}
		cooldown--;
	}
	
	private float birth = System.nanoTime();
	private int life;
	
	public Powerup(Vector2f location, int life) {
		super(new Vector3f(location.x, location.y, 0.5f), new Vector3f(0.24f, 0.24f*1.5f, 0.2f));
		this.life = life;
		
		powerups.add(this);
	}
	
	public abstract Model getModel();

	public abstract void updateInfo();
	
	public void update(){
		updateInfo();
		if(this.getLifeLeft() < 0){
			powerups.remove(this);
		}
	}
	
	public float getLifeLeft(){
		final float currentTime = System.nanoTime();
		float f = Math.round((currentTime - birth)/1000000)*1000/life;
		f/=1000;
		return 1-f;
	}	
	
	public Vector2f getSpawn(){
		SquareHitbox bound = new SquareHitbox(new Vector2f(Settings.boundary.getLocation().x,Settings.boundary.getLocation().y), new Vector2f(Settings.boundary.getSize().x, Settings.boundary.getSize().y));
		List<Hitbox> hitbox = new ArrayList<Hitbox>();
		for(Hitbox hb: Settings.getWorld().getHitboxList()){
			hitbox.add(hb);
		}
		Collections.shuffle(hitbox);
		
		Main : for(Hitbox hb: hitbox){
			float x = hb.getLocation().x+hb.getSize().x/2;
			float y = hb.getLocation().y+this.getSize().y;
			for(;;y+=0.1f){
				if(!bound.AreaIntersect(new Vector2f(x,y), new Vector2f(this.getSize().x, this.getSize().y))){
					continue Main;
				}else{
					boolean intersects = false;
					for(Hitbox h: hitbox){
						if(h.AreaIntersect(new Vector2f(x,y), new Vector2f(this.getSize().x, this.getSize().y))){
							intersects = true;
						}
					}
					if(!intersects){
						return new Vector2f(x,y);
					}
				}
			}
		}
		return new Vector2f(-10000,-10000);
	}

	public void attach(Player p){
		powerups.remove(this);
		attachEffects(p);
	}
	
	public void dettach(Player p){
		dettachEffects(p);
	}
	
	protected abstract void attachEffects(Player p);
	protected abstract void dettachEffects(Player p);
}
