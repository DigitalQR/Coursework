package Entities;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import Collision.StaticHitbox2f;
import Control.MainControl;
import Control.Settings;
import Control.Input.Gamepad;
import Entities.Tools.ControlScheme;
import Entities.Tools.Entity;
import Entities.Tools.Movement;
import RenderEngine.Loader;
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

	private static Animation spawn;
	
	public Player(float x, float y){
		super(new Vector3f(x,y,0), new Vector3f(0.2f, 0.6f, 0.2f));
		checkSpawn(x,y);
		
		control = new ControlScheme(Keyboard.KEY_W, Keyboard.KEY_S, Keyboard.KEY_A, Keyboard.KEY_D, Keyboard.KEY_G, Keyboard.KEY_H, Keyboard.KEY_ESCAPE, Keyboard.KEY_L);
		this.addComponent(new Movement(control));
	}	
	
	private void checkSpawn(float x, float y){
		Main: while(true){
			boolean Escape = true;
			Check: for(StaticHitbox2f hb:Settings.hb){
				if(hb.AreaIntersect(new Vector2f(x, y), new Vector2f(this.getSize().x, this.getSize().y))){
					Escape = false;
					break Check;
				}
			}
			
			if(Escape){
				break Main;
			}else{
				x+=Toolkit.RandomInt(-1,1)/1;
				y+=Toolkit.RandomInt(-1,1)/1;
			}
		}
		this.setLocation(new Vector3f(x,y,0));
	}
	
	public static void loadResources(){
		PlaneTexture = Loader.loadTexture("Model/TornTest");
		spawn = new Animation("Cube/Spawn", 100);
	}
	
	public void setControlScheme(int up, int down, int left, int right, int primary, int secondary, int start, int select){
		control.setControlScheme(up, down, left, right, primary, secondary, start, select);
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
		
		this.updateComponents();
	}
	
	public Vector3f getLERPLocation(){
		float LookupTime = System.nanoTime()-MainControl.UPS;
		float CurrentTime = System.nanoTime();
		Vector3f location = this.getLocation();
		
		float x = Toolkit.LERP(new Vector2f(LastUpdate, LastLocation.x), new Vector2f(CurrentTime, location.x), LookupTime);
		float y = Toolkit.LERP(new Vector2f(LastUpdate, LastLocation.y), new Vector2f(CurrentTime, location.y), LookupTime);
	
		return new Vector3f(x, y, location.z);
	}
	
	float track = 0;
	public Model getModel(){
		Model m = spawn.getCurrentFrame();
		Vector3f loc = getLERPLocation();
		m.setLocation(new Vector3f(loc.x+this.getSize().x/2, loc.y, loc.z+this.getSize().z/2));
		m.setRGBA(1, 1, 1, 1);
		m.setTexture(PlaneTexture);
		m.scaleBy(2);
		
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
}
