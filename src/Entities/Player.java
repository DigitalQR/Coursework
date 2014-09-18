package Entities;

import org.lwjgl.input.Keyboard;

import Collision.StaticHitbox2f;
import Control.Settings;
import Control.Input.Gamepad;
import Entities.Tools.ControlScheme;
import Entities.Tools.Entity;
import Entities.Tools.Movement;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public class Player extends Entity{
	
	private ControlScheme control;
	
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
}
