package Entities.Tools;

public class ServerControlScheme extends ControlScheme{

	public boolean[] keys = new boolean[14];
	public static final int KEY_COUNT = 13;
	
	public ServerControlScheme(){
		super();
	}
	
	public void setDefaultControls(){
		this.GPID = -2;
		
		KEY_JUMP = 0; 
		KEY_DUCK = 1;
		KEY_BLOCK = 2; 
		KEY_LEFT = 3; 
		KEY_RIGHT = 4; 
		
		KEY_UP = 5; 
		KEY_DOWN = 6;
		
		KEY_SELECT = 7;
		KEY_BACK = 8;
		
		KEY_PRIMARY = 9; 
		KEY_SECONDARY = 10;
		KEY_GRAB = 11;
		
		KEY_START = 12;
	}
	
	public boolean isKeyPressed(int key){
		return keys[key];
	}
	
	
	public void setKeyState(int key, boolean val){
		keys[key] = val;
	}
	
	public boolean areKeysEqual(ServerControlScheme s){
		for(int i = 0 ; i<KEY_COUNT; i++){
			if(s.keys[i] != keys[i]){
				return false;
			}
		}
		return true;
	}
	
}
