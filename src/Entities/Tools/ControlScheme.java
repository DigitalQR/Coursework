package Entities.Tools;

import org.lwjgl.input.Keyboard;

import Control.Input.Gamepad;

public class ControlScheme extends Component{
	
	public int KEY_JUMP, KEY_UP, KEY_DOWN, KEY_DUCK, KEY_LEFT, KEY_RIGHT,  KEY_PRIMARY, KEY_SECONDARY, KEY_SELECT,	KEY_BACK, KEY_START;
	public int GPID;
	
	public ControlScheme(int up, int down, int left, int right, int primary, int secondary, int start){
		setControlScheme(up, down, left, right, primary, secondary, start);
	}
	
	public void setControlScheme(int up, int down, int left, int right, int primary, int secondary, int start){
		KEY_JUMP = up; 
		KEY_DUCK = down; 
		KEY_LEFT = left; 
		KEY_RIGHT = right; 
		
		KEY_UP = up; 
		KEY_DOWN = down;
		KEY_LEFT = left;
		KEY_RIGHT = right;
		KEY_SELECT = primary;
		KEY_BACK = secondary;
		
		KEY_PRIMARY = primary; 
		KEY_SECONDARY = secondary;
		
		KEY_START = start;
		
		GPID = -1;
	}

	public ControlScheme(int GPID){
		setControlScheme(GPID);
	}
	
	public void setControlScheme(int GPID){
		this.GPID = GPID;
		KEY_JUMP = Gamepad.BUTTON_JUMP; 
		KEY_DUCK = Gamepad.BUTTON_DUCK; 
		KEY_LEFT = Gamepad.BUTTON_LEFT; 
		KEY_RIGHT = Gamepad.BUTTON_RIGHT; 
		
		KEY_UP = Gamepad.BUTTON_UP; 
		KEY_DOWN = Gamepad.BUTTON_DOWN;
		KEY_LEFT = Gamepad.BUTTON_LEFT;
		KEY_RIGHT = Gamepad.BUTTON_RIGHT;
		
		KEY_SELECT = Gamepad.BUTTON_JUMP;
		KEY_BACK = Gamepad.BUTTON_DUCK;
		
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
		}else{
			return Gamepad.getGamepad(GPID).isButtonPressed(key);
		}
	}

	public void update(Entity e){
		
	}
}
