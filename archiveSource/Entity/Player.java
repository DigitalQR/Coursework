package Entity;

import org.lwjgl.input.Keyboard;

import Collision.StaticHitbox2f;
import Control.Settings;
import Control.input.Gamepad;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;

public class Player{
	
	private Vector2f Location;
	Vector2f Size = new Vector2f(0.2f, 0.6f);
	
	Vector2f Velocity = new Vector2f(0,0);
	Vector2f AccelerationLimit = new Vector2f(0.1f, 2f);
	boolean TouchingGround = false;
	int TouchingWall = 0;
	private float 
	COLOUR_RED = (float) Math.random(),
	COLOUR_GREEN = (float) Math.random(),
	COLOUR_BLUE = (float) Math.random();
	
	public Player(float x, float y){
		setControlScheme(TYPE_KEYBOARD, Keyboard.KEY_W, Keyboard.KEY_S, Keyboard.KEY_A, Keyboard.KEY_D, Keyboard.KEY_F, Keyboard.KEY_G);
		
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
	
	public void setColour(float r, float g, float b){
		COLOUR_RED = r;
		COLOUR_GREEN = g;
		COLOUR_BLUE = b;
	}
	
	public float getRed(){
		return COLOUR_RED;
	}
	
	public float getGreen(){
		return COLOUR_GREEN;
	}
	
	public float getBlue(){
		return COLOUR_BLUE;
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
	@SuppressWarnings("unused")
	private int TYPE, UP, DOWN, LEFT, RIGHT, PRIMARY, SECONDARY, GPID;

	public void setControlScheme(int type, int up, int down, int left, int right, int primary, int secondary){
		TYPE = type;
		UP = up; DOWN = down; LEFT = left; RIGHT = right; PRIMARY = primary; SECONDARY = secondary;
	}

	public void setControlScheme(int GPID){
		TYPE = TYPE_CONTROL;
		this.GPID = GPID;
		UP = Gamepad.A; DOWN = Gamepad.B; LEFT = Gamepad.LEFT_STICK_LEFT; RIGHT = Gamepad.LEFT_STICK_RIGHT; PRIMARY = Gamepad.LEFT_TRIGGER; SECONDARY = Gamepad.RIGHT_TRIGGER;
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
				if(isKeyPressed(RIGHT) && Velocity.x < AccelerationLimit.x) Velocity.x+=0.02f;
				if(isKeyPressed(LEFT) && Velocity.x > -AccelerationLimit.x) Velocity.x-=0.02f;
				
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
				if(isKeyPressed(UP) && Velocity.y < AccelerationLimit.y && (TouchingGround || TouchingWall != 0)){
					Velocity.y = 0.2f;
					TouchingGround = false;
					if(TouchingWall != 0){
						Velocity.x+=0.3f*-TouchingWall;
					}
				}
				if(isKeyPressed(DOWN)) Velocity.y = -0.5f;
				
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
