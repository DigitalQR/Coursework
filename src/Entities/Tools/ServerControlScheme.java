package Entities.Tools;

public class ServerControlScheme extends ControlScheme{
	
	private boolean[] keys = new boolean[14];
	public static final int KEY_COUNT = 14;
	
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
		KEY_LEFT = 7;
		KEY_RIGHT = 8;
		
		KEY_SELECT = 9;
		KEY_BACK = 10;
		
		KEY_PRIMARY = 11; 
		KEY_SECONDARY = 12;
		
		KEY_START = 13;
	}
	
	public boolean isKeyPressed(int key){
		return keys[key];
	}
	
	
	public void setKeyState(int key, boolean val){
		keys[key] = val;
	}
}
