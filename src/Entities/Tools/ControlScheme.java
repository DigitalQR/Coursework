package Entities.Tools;

import org.lwjgl.input.Keyboard;

import Control.Input.Gamepad;
import Entities.Entity;

public class ControlScheme extends Component{
	
	public int KEY_JUMP, KEY_DUCK, KEY_UP, KEY_DOWN, KEY_LEFT, KEY_RIGHT,  KEY_PRIMARY, KEY_SECONDARY, KEY_SELECT, KEY_BACK, KEY_START, KEY_BLOCK;
	public static final int KEY_COUNT = 12;
	protected int GPID;
	
	public ControlScheme(){
		setDefaultControls();
	}
	
	public int getGPID(){
		return GPID;
	}
	
	public void setDefaultControls(){		
		this.GPID = -1;
		KEY_JUMP = 0; 
		KEY_DUCK = 0;
		KEY_BLOCK = 0; 
		KEY_LEFT = 0; 
		KEY_RIGHT = 0; 
		
		KEY_UP = 0; 
		KEY_DOWN = 0;
		KEY_LEFT = 0;
		KEY_RIGHT = 0;
		
		KEY_SELECT = 0;
		KEY_BACK = 0;
		
		KEY_PRIMARY = 0; 
		KEY_SECONDARY = 0;
		
		KEY_START = 0;
	}
	
	public void setControlScheme(int GPID){
		this.GPID = GPID;
		KEY_JUMP = Gamepad.BUTTON_JUMP; 
		KEY_DUCK = Gamepad.BUTTON_DUCK;
		KEY_BLOCK = Gamepad.BUTTON_BLOCK; 
		KEY_LEFT = Gamepad.BUTTON_LEFT; 
		KEY_RIGHT = Gamepad.BUTTON_RIGHT; 
		
		KEY_UP = Gamepad.BUTTON_UP; 
		KEY_DOWN = Gamepad.BUTTON_DOWN;
		KEY_LEFT = Gamepad.BUTTON_LEFT;
		KEY_RIGHT = Gamepad.BUTTON_RIGHT;
		
		KEY_SELECT = Gamepad.BUTTON_MENU_FORWARD;
		KEY_BACK = Gamepad.BUTTON_MENU_BACK;
		
		KEY_PRIMARY = Gamepad.BUTTON_PRIMARY; 
		KEY_SECONDARY = Gamepad.BUTTON_SECONDARY;
		
		KEY_START = Gamepad.BUTTON_PAUSE;
	}
	
	public boolean isKeyPressed(int key){		
		if(GPID == -1){
			try{
				return Keyboard.isKeyDown(key);
			}catch(IllegalStateException e){
				return false;
			}
		}else if(GPID == -2){
			try {
				throw new Exception("SSSS");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}else{
			return Gamepad.getGamepad(GPID).isButtonPressed(key);
		}
	}

	public void update(Entity e){
		
	}
}
