package Entities.Tools;

import org.lwjgl.input.Keyboard;

import Control.Input.Gamepad;

public class ControlScheme extends Component{
	
	public int KEY_JUMP, KEY_DUCK, KEY_UP, KEY_DOWN, KEY_LEFT, KEY_RIGHT,  KEY_PRIMARY, KEY_SECONDARY, KEY_SELECT, KEY_BACK, KEY_START, KEY_BLOCK;
	public static final int KEY_COUNT = 12;
	public int GPID;
	
	public ControlScheme(){
		setDefaultControls();
	}
	
	public void setDefaultControls(){
		setControlScheme(Keyboard.KEY_W, Keyboard.KEY_S, Keyboard.KEY_A, Keyboard.KEY_D, Keyboard.KEY_W, Keyboard.KEY_G, Keyboard.KEY_T, Keyboard.KEY_F, Keyboard.KEY_H, Keyboard.KEY_ESCAPE);
	}
	
	public ControlScheme(int up, int down, int left, int right, int jump, int duck, int block, int primary, int secondary, int start){
		setControlScheme(up, down, left, right, jump, duck, block, primary, secondary, start);
	}
	
	public void setControlScheme(int up, int down, int left, int right, int jump, int duck, int block, int primary, int secondary, int start){
		KEY_JUMP = jump; 
		KEY_DUCK = duck;
		KEY_BLOCK = block; 
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
		KEY_BLOCK = Gamepad.BUTTON_BLOCK; 
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
