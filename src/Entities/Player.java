package Entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.input.Keyboard;

import Collision.Hitbox;
import Collision.SquareHitbox;
import Control.MainControl;
import Control.Settings;
import Entities.Tools.Attack;
import Entities.Tools.ControlScheme;
import Entities.Tools.Health;
import Entities.Tools.Movement;
import Entities.Tools.MovementMK2;
import RenderEngine.Model.Animation;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public class Player extends Entity{
	
	private ControlScheme control;
	private Movement movement;
	private Attack attack;
	public Health health;
	
	private float[] RGBA = new float[4];

	private static Animation spawn;
	
	public Player(float x, float y){
		super(new Vector3f(x,y,0f), new Vector3f(0.2f, 0.3f, 0.2f));
		if(Settings.doesWorldExist()){
			spawn();
		}		
		
		control = new ControlScheme();
		movement = new MovementMK2(control);
		attack = new Attack(control);
		
		this.addComponent(movement);
		this.addComponent(attack);
		health = new Health();
		this.addComponent(health);
	}	
	
	public void destroy(){
		Settings.User.remove(this);
	}
	
	public float[] getRGBA(){
		return RGBA.clone();
	}
	
	public float getFactor(){
		return health.factor;
	}
	
	private float IDLETime = -1;
	
	public boolean isPlayerIDLE(){
		boolean x = this.LastLocation.x == this.getLocation().x;
		boolean y = this.LastLocation.y == this.getLocation().y;
				
		if(IDLETime == -1){
			if(x && y){
				IDLETime = System.nanoTime();
			}
		}else if(!x || !y){
			IDLETime = -1;
		}else{
			if((System.nanoTime()-IDLETime)/1000000 >= 3000){
				return true;
			}
		}
		return false;
	}
	
	private void spawn(){
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
						this.setLocation(new Vector3f(x,y,this.getLocation().z));
						this.setVelocity(new Vector3f(0,0,0));
						break Main;
					}
				}
			}
		}
	}
	
	public static void loadResources(){
		spawn = new Animation("Cube/Spin", 100);
	}
	
	public void setControlScheme(int GPID){
		control.setControlScheme(GPID);
	}
	
	public void setControlScheme(ControlScheme control){
		this.control.setDefaultControls();
		
		this.control = control;
		
		this.removeComponent(attack);
		this.removeComponent(movement);

		this.attack = new Attack(control);
		this.movement = new MovementMK2(control);
		this.addComponent(attack);
		this.addComponent(movement);
	}
	
	public boolean isKeyPressed(int key){
		if(control.getGPID() == -1){
			try{
				return Keyboard.isKeyDown(key);
			}catch(IllegalStateException e){
				return false;
			}
		}else{
			return control.isKeyPressed(key);
		}
	}
	
	public ControlScheme getControlScheme(){
		return control;
	}
	
	public void update(){
		LastLocation =this.getLocation().clone();
		this.LastUpdate = System.nanoTime()-MainControl.UPS;
			
		if(!health.isDead){

			if(!Settings.isClientActive()){
				this.updateComponents();
			}else{
				health.clientUpdate(this);
			}
		
		}else if(health.canRespawn()){
			spawn();
			LastLocation.x = this.getLocation().x;
			LastLocation.y = this.getLocation().y;
			health.spawn();
		}
			
		SquareHitbox bound = new SquareHitbox(new Vector2f(Settings.boundary.getLocation().x,Settings.boundary.getLocation().y), new Vector2f(Settings.boundary.getSize().x, Settings.boundary.getSize().y));
		if(!bound.AreaIntersect(new Vector2f(this.getLocation().x, this.getLocation().y),  new Vector2f(this.getSize().x, this.getSize().y)) && !health.isDead){
			health.kill(true);
		}
	}

	public Model getModel(){
		Model m = spawn.getCurrentFrame();
		Vector3f loc = getLERPLocation();
		m.setLocation(new Vector3f(loc.x+this.getSize().x/2, loc.y-0.25f, 0.5f));
		m.setRGBA(1, 1, 1, 1);
		m.scaleBy(6);
		
		return m;
	}
	
	public Model getHitbox(){
		Cubef temp1 = new Cubef(new Vector3f(0,0,0), new Vector3f(this.getSize().x, this.getSize().y, 0.2f+this.getSize().x));
		Model m = new Model(temp1);
		m.setLocation(getLERPLocation());
		m.setRGBA(1, 1, 1, 0.3f);
		return m;
	}
	
	public boolean isDead(){
		try{
			return health.isDead;
		}catch(NullPointerException e){
			return true;
		}
	}
	
	public void kill(boolean increaseKills){
		health.kill(increaseKills);
	}
	
	public int getStock(){
		return health.stock;
	}
	
	public void reset(){
		health.reset();
		killCount = 0;
		health.factor = 0;
		kill(false);
	}
	
	public void setRGBA(float[] rGBA) {
		RGBA = rGBA;
	}

	public float getRespawnTimeRemaining(){
		return health.getTimeRemaining();
	}
	
	public int getDeathCount(){
		return health.getDeathCount();
	}
}
