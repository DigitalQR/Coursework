package Entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import Collision.SquareHitbox;
import Control.MainControl;
import Control.Settings;
import Control.Input.Gamepad;
import Entities.Tools.Attack;
import Entities.Tools.ControlScheme;
import Entities.Tools.Entity;
import Entities.Tools.Health;
import Entities.Tools.Movement;
import RenderEngine.textureLoader;
import RenderEngine.Model.Animation;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public class Player extends Entity{
	
	private ControlScheme control;
	
	private float LastUpdate = 0;
	private Vector3f LastLocation = new Vector3f(0,0,0);
	private static Texture PlaneTexture;
	public Health health;
	
	private float[] RGBA = new float[4];

	private static Animation spawn;
	
	public Player(float x, float y){
		super(new Vector3f(x,y,0f), new Vector3f(0.2f, 0.3f, 0.2f));
		spawn();
		
		Collections.shuffle(Settings.playerColourProfiles);
		Vector3f colour = Settings.playerColourProfiles.get(0);
		RGBA[0] = colour.x;
		RGBA[1] = colour.y;
		RGBA[2] = colour.z;
		RGBA[3] = 1;
		Settings.playerColourProfiles.remove(colour);
		
		control = new ControlScheme();
		this.addComponent(new Movement(control));
		this.addComponent(new Attack(control));
		health = new Health();
		this.addComponent(health);
	}	
	
	public float[] getRGBA(){
		return RGBA.clone();
	}
	
	public float getFactor(){
		return health.factor;
	}
	
	private float IDLETime = -1;
	public boolean isPlayerIDLE(){
		boolean x = LastLocation.x == this.getLocation().x;
		boolean y = LastLocation.y == this.getLocation().y;
		
		
		
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
		List<SquareHitbox> hitbox = new ArrayList<SquareHitbox>();
		for(SquareHitbox hb: Settings.hb){
			hitbox.add(hb);
		}
		Collections.shuffle(hitbox);
		
		Main : for(SquareHitbox hb: hitbox){
			float x = hb.getLocation().x+hb.getSize().x/2;
			float y = hb.getLocation().y+this.getSize().y;
			for(;;y+=0.1f){
				if(!bound.AreaIntersect(new Vector2f(x,y), new Vector2f(this.getSize().x, this.getSize().y))){
					continue Main;
				}else{
					boolean intersects = false;
					for(SquareHitbox h: hitbox){
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
		PlaneTexture = textureLoader.loadTexture("Model/TornTest");
		spawn = new Animation("Cube/Spin", 100);
	}
	
	public void setControlScheme(int up, int down, int left, int right, int jump, int duck, int primary, int secondary, int start){
		control.setControlScheme(up, down, left, right, jump, duck, primary, secondary, start);
	}
	
	public void setControlScheme(int GPID){
		control.setControlScheme(GPID);
	}
	
	public boolean isKeyPressed(int key){
		if(control.GPID == -1){
			return Keyboard.isKeyDown(key);
		}else{
			return Gamepad.getGamepad(control.GPID).isButtonPressed(key);
		}
	}
	
	public ControlScheme getControlScheme(){
		return control;
	}

	public void update(){
		Vector3f location = this.getLocation();
		LastLocation = new Vector3f(location.x, location.y, location.z);
		LastUpdate = System.nanoTime()-MainControl.UPS;
		
		if(!health.isDead){
			this.updateComponents();
		}else if(health.canRespawn()){
			spawn();
			LastLocation.x = this.getLocation().x;
			LastLocation.y = this.getLocation().y;
			health.reset();
		}
		
		SquareHitbox bound = new SquareHitbox(new Vector2f(Settings.boundary.getLocation().x,Settings.boundary.getLocation().y), new Vector2f(Settings.boundary.getSize().x, Settings.boundary.getSize().y));
		if(!bound.AreaIntersect(new Vector2f(this.getLocation().x, this.getLocation().y),  new Vector2f(this.getSize().x, this.getSize().y))){
			health.kill();
		}
	}
	
	private Vector3f LERPLocation = this.getLocation();
	
	public void processLERPLocation(){
		float LookupTime = System.nanoTime()-MainControl.UPS;
		float CurrentTime = System.nanoTime();
		Vector3f location = this.getLocation();
		
		float x = Toolkit.LERPValue(new Vector2f(LastUpdate, LastLocation.x), new Vector2f(CurrentTime, location.x), LookupTime);
		float y = Toolkit.LERP(new Vector2f(LastUpdate, LastLocation.y), new Vector2f(CurrentTime, location.y), LookupTime);
		LERPLocation = new Vector3f(x, y, location.z);
	}
	
	public Vector3f getLERPLocation(){
		return LERPLocation;
	}

	public Model getModel(){
		Model m = spawn.getCurrentFrame();
		Vector3f loc = getLERPLocation();
		m.setLocation(new Vector3f(loc.x+this.getSize().x/2, loc.y-0.25f, loc.z+this.getSize().z/2));
		m.setRGBA(1, 1, 1, 1);
		m.scaleBy(6);
		
		return m;
	}
	
	public Model getHitbox(){
		Cubef temp1 = new Cubef(new Vector3f(0,0,0), new Vector3f(this.getSize().x, this.getSize().y, 0.2f+this.getSize().x));
		Model m = new Model(temp1);
		m.setTexture(PlaneTexture);
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
	
	public void kill(){
		health.kill();
	}
	
	public float getRespawnTimeRemaining(){
		return health.getTimeRemaining();
	}
	
	public int getDeathCount(){
		return health.getDeathCount();
	}
}
