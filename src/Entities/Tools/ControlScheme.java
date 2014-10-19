package Entities.Tools;

import org.lwjgl.input.Keyboard;

import Control.Input.Gamepad;

public class ControlScheme extends Component{
	
	public int KEY_UP, KEY_DOWN, KEY_LEFT, KEY_RIGHT, KEY_PRIMARY, KEY_SECONDARY, KEY_MENU_UP, KEY_MENU_DOWN,	KEY_MENU_LEFT,	KEY_MENU_RIGHT, KEY_MENU_SELECT,	KEY_MENU_BACK, KEY_START,	KEY_SELECT;
	public int GPID;
	
	public ControlScheme(int up, int down, int left, int right, int primary, int secondary, int start, int select){
		setControlScheme(up, down, left, right, primary, secondary, start, select);
	}
	
	public void setControlScheme(int up, int down, int left, int right, int primary, int secondary, int start, int select){
		KEY_UP = up; 
		KEY_DOWN = down; 
		KEY_LEFT = left; 
		KEY_RIGHT = right; 
		
		KEY_MENU_UP = up; 
		KEY_MENU_DOWN = down;
		KEY_MENU_LEFT = left;
		KEY_MENU_RIGHT = right;
		KEY_MENU_SELECT = primary;
		KEY_MENU_BACK = secondary;
		
		KEY_PRIMARY = primary; 
		KEY_SECONDARY = secondary;
		
		KEY_START = start;
		KEY_SELECT = select;
		
		GPID = -1;
	}

	public ControlScheme(int GPID){
		setControlScheme(GPID);
	}
	
	public void setControlScheme(int GPID){
		this.GPID = GPID;
		KEY_UP = Gamepad.A; 
		KEY_DOWN = Gamepad.B; 
		KEY_LEFT = Gamepad.LEFT_STICK_LEFT; 
		KEY_RIGHT = Gamepad.LEFT_STICK_RIGHT; 
		
		KEY_MENU_UP = Gamepad.DPAD_UP; 
		KEY_MENU_DOWN = Gamepad.DPAD_DOWN;
		KEY_MENU_LEFT = Gamepad.DPAD_LEFT;
		KEY_MENU_RIGHT = Gamepad.DPAD_RIGHT;
		KEY_MENU_SELECT = Gamepad.A;
		KEY_MENU_BACK = Gamepad.B;
		
		KEY_PRIMARY = Gamepad.RIGHT_TRIGGER; 
		KEY_SECONDARY = Gamepad.LEFT_TRIGGER;
		
		KEY_START = Gamepad.START;
		KEY_SELECT = Gamepad.SELECT;
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
