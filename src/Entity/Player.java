package Entity;

import org.lwjgl.input.Keyboard;

import Collision.StaticHitbox2f;
import Control.Settings;
import Control.Input.Gamepad;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;

public class Player{
	
	private Vector2f Location;
	Vector2f Size = new Vector2f(0.2f, 0.6f);
	
	Vector2f Velocity = new Vector2f(0,0);
	Vector2f AccelerationLimit = new Vector2f(0.1f, 2f);
	boolean TouchingGround = false;
	int TouchingWall = 0;
	
	public Player(float x, float y){
		setControlScheme(TYPE_KEYBOARD, Keyboard.KEY_W, Keyboard.KEY_S, Keyboard.KEY_A, Keyboard.KEY_D, Keyboard.KEY_F, Keyboard.KEY_G, Keyboard.KEY_ESCAPE, Keyboard.KEY_L);
		
		//Checking spawn
		Main: while(true){
			boolean Escape = true;
			Check: for(StaticHitbox2f hb:Settings.hb){
				if(hb.AreaIntersect(new Vector2f(x, y), Size)){
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
		Location = new Vector2f(x,y);
	}
	
	public void setLocation(float x, float y){
		Location.x = x;
		Location.y = y;
	}
	
	public Vector2f getLocation(){
		return Location;
	}
	
	public Vector2f getSize(){
		return Size;
	}
	
	public static final int TYPE_KEYBOARD = -1, TYPE_CONTROL= 0;
	public int 
	TYPE, KEY_UP, 
	KEY_DOWN, 
	KEY_LEFT, 
	KEY_RIGHT, 
	KEY_PRIMARY, 
	KEY_SECONDARY, 
	KEY_MENU_UP, 
	KEY_MENU_DOWN, 
	KEY_MENU_LEFT, 
	KEY_MENU_RIGHT,
	KEY_START,
	KEY_SELECT,
	GPID;

	public void setControlScheme(int type, int up, int down, int left, int right, int primary, int secondary, int start, int select){
		TYPE = type;
		KEY_UP = up; 
		KEY_DOWN = down; 
		KEY_LEFT = left; 
		KEY_RIGHT = right; 
		
		KEY_MENU_UP = up; 
		KEY_MENU_DOWN = down;
		KEY_MENU_LEFT = left;
		KEY_MENU_RIGHT = right;
		
		KEY_PRIMARY = primary; 
		KEY_SECONDARY = secondary;
		
		KEY_START = start;
		KEY_SELECT = select;
	}

	public void setControlScheme(int GPID){
		TYPE = TYPE_CONTROL;
		this.GPID = GPID;
		KEY_UP = Gamepad.A; 
		KEY_DOWN = Gamepad.B; 
		KEY_LEFT = Gamepad.LEFT_STICK_LEFT; 
		KEY_RIGHT = Gamepad.LEFT_STICK_RIGHT; 
		
		KEY_MENU_UP = Gamepad.DPAD_UP; 
		KEY_MENU_DOWN = Gamepad.DPAD_DOWN;
		KEY_MENU_LEFT = Gamepad.DPAD_LEFT;
		KEY_MENU_RIGHT = Gamepad.DPAD_RIGHT;
		
		KEY_PRIMARY = Gamepad.RIGHT_TRIGGER; 
		KEY_SECONDARY = Gamepad.LEFT_TRIGGER;
		
		KEY_START = Gamepad.START;
		KEY_SELECT = Gamepad.SELECT;
	}
	
	public boolean isKeyPressed(int key){
		if(TYPE == TYPE_KEYBOARD){
			return Keyboard.isKeyDown(key);
		}else{
			return Gamepad.getGamepad(GPID).isButtonPressed(key);
		}
	}
	
	public void process(){
		DebugHitboxPlayer();
		//X
			//Input
				if(isKeyPressed(KEY_RIGHT) && Velocity.x < AccelerationLimit.x) Velocity.x+=0.02f;
				if(isKeyPressed(KEY_LEFT) && Velocity.x > -AccelerationLimit.x) Velocity.x-=0.02f;
				
				if(Math.round(Velocity.x*100) == 0){
					Velocity.x = 0;
				}else{
					Velocity.x-=0.01*Toolkit.Sign(Velocity.x);
				}
			//Hitbox detection
				TouchingWall = 0;
				Normalise();
				if(Velocity.x != 0){
					int InitialVelx = Math.round(Velocity.x*1000); 
					Velocity.x = 0;
					for(float x = 0; Toolkit.Modulus(Math.round(x*1000)) <= Toolkit.Modulus(InitialVelx); x+=0.01f*Toolkit.Sign(InitialVelx)){
						x = Math.round(x*1000);
						x/=1000;
						
						if(insideHitbox(new Vector2f(x, 0))){
							if(!TouchingGround){
								TouchingWall = Toolkit.Sign(InitialVelx);
							}
							break;
						}else{
							Velocity.x = x;
						}
					}
				}
				Location.x+=Velocity.x;
				
		//Y
			//Input
				if(isKeyPressed(KEY_UP) && Velocity.y < AccelerationLimit.y && (TouchingGround || TouchingWall != 0)){
					Velocity.y = 0.2f;
					TouchingGround = false;
					if(TouchingWall != 0){
						Velocity.x+=0.3f*-TouchingWall;
					}
				}
				if(isKeyPressed(KEY_DOWN)) Velocity.y = -0.5f;
				
				if(Toolkit.Modulus(Velocity.y) < AccelerationLimit.y){
					Velocity.y-=0.01f;
				}
				if(Math.round(Velocity.y*100) == 0){
					Velocity.y = 0;
				}
				
				if(Toolkit.Sign(Velocity.y) == -1 && insideHitbox(new Vector2f(0,Velocity.y-0.03f))){
					TouchingGround = true;
				}
				
				//Hitbox detection
				Normalise();
				if(Velocity.y != 0){
					int InitialVely = Math.round(Velocity.y*1000); 
					Velocity.y = 0;
					for(float y = 0; Toolkit.Modulus(Math.round(y*1000)) <= Toolkit.Modulus(InitialVely); y+=0.01f*Toolkit.Sign(InitialVely)){
						y = Math.round(y*1000);
						y/=1000;
						
						if(insideHitbox(new Vector2f(0, y))){
							break;
						}else{
							Velocity.y = y;
						}
					}
				}
				
				Location.y+=Velocity.y;
				
	}
	
	private void Normalise(){
		Location.x = Math.round(Location.x*100);
		Location.x/=100;
		Location.y = Math.round(Location.y*100);
		Location.y/=100;

		Velocity.x = Math.round(Velocity.x*100);
		Velocity.x/=100;
		Velocity.y = Math.round(Velocity.y*100);
		Velocity.y/=100;
	}
	
	private void DebugHitboxPlayer(){
		for(StaticHitbox2f hb:Settings.hb){
			if(hb.AreaIntersect(Location, Size)){
				System.err.println("Location: \n x: " + Location.x + "\n y: " + Location.y + "\n");
				System.err.println("Velocity: \n x: " + Velocity.x + "\n y: " + Velocity.y + "\n");
				System.err.println("Touching Ground: " + TouchingGround + "\n");
				System.exit(0);
			}
		}
	}
	
	private boolean insideHitbox(Vector2f v){
		for(StaticHitbox2f hb:Settings.hb){
			if(hb.AreaIntersect(Location.add(v), Size)){
				return true;
			}
		}
		return false;
	}
	
}
