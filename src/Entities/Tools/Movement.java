package Entities.Tools;

import Collision.Hitbox;
import Control.Settings;
import Entities.Entity;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public class Movement extends Component{

	protected ControlScheme control;
	protected Vector2f accelerationLimit = new Vector2f(0.2f, 40f);
	protected float accelerationFactor = 1;
	protected boolean touchingGround = false;
	protected int touchingWall = 0;
	protected boolean bounce = true;
	protected static final float COLLISION_DEPTH = 0.001f;
	protected static final int DP = 1000;

	protected int jumpCount = 0;
	protected int jumpCap = 1;
	
	public void setJumpCap(int i){
		jumpCap = i;
	}
	
	public Movement(ControlScheme controlScheme){
		control = controlScheme;
	}
	
	public int getTouchingWall(){
		return touchingWall;
	}
	
	public void setAccelerationFactor(float accelerationFactor) {
		this.accelerationFactor = accelerationFactor;
	}

	public boolean getTouchingGround(){
		return touchingGround;
	}
	
	public void update(Entity e){
		if(!Settings.toggles.get("s_noclip")){
			processInput(e);
			updateY(e);
			updateX(e);
		}else{
			noclipProcess(e);
		}
		e.normaliseLocation();
		e.setVelocity(new Vector3f(e.getVelocity().x, e.getVelocity().y, 0));
	}
	
	protected void noclipProcess(Entity e){
		//Input
		if(control.isKeyPressed(control.KEY_RIGHT) && e.getVelocity().x < accelerationLimit.x) e.getVelocity().x+=0.08f;
		if(control.isKeyPressed(control.KEY_LEFT) && e.getVelocity().x > -accelerationLimit.x) e.getVelocity().x-=0.08f;
		if(control.isKeyPressed(control.KEY_UP) && e.getVelocity().y < accelerationLimit.x) e.getVelocity().y+=0.08f;
		if(control.isKeyPressed(control.KEY_DOWN) && e.getVelocity().y > -accelerationLimit.x) e.getVelocity().y-=0.08f;
		
		//Normalisation
		Vector3f location = e.getLocation();
		location.x = Math.round(location.x*DP);
		location.x/=DP;
		location.y = Math.round(location.y*DP);
		location.y/=DP;

		e.getVelocity().x = Math.round(e.getVelocity().x*DP);
		e.getVelocity().x/=DP;
		e.getVelocity().y = Math.round(e.getVelocity().y*DP);
		e.getVelocity().y/=DP;

		//Slowdown
		if(Math.round(e.getVelocity().x*10) == 0){
			e.getVelocity().x = 0;
		}else{
			e.getVelocity().x-=0.04*Toolkit.Sign(e.getVelocity().x);
		}
		
		if(Math.round(e.getVelocity().y*10) == 0){
			e.getVelocity().y = 0;
		}else{
			e.getVelocity().y-=0.04*Toolkit.Sign(e.getVelocity().y);
		}
		
		

		location.x+=e.getVelocity().x;
		location.y+=e.getVelocity().y;
	}
	
	protected void processInput(Entity e){
		//X
		if(!e.stunned()){
			if(control.isKeyPressed(control.KEY_RIGHT) && e.getVelocity().x < accelerationLimit.x) e.getVelocity().x+=0.08f;
			if(control.isKeyPressed(control.KEY_LEFT) && e.getVelocity().x > -accelerationLimit.x) e.getVelocity().x-=0.08f;
		}
		
		e.getVelocity().x = Math.round(e.getVelocity().x*DP);
		e.getVelocity().x/=DP;
		
		//Y
		if(!e.stunned() && control.isKeyPressed(control.KEY_JUMP) && e.getVelocity().y < accelerationLimit.y && (touchingGround || touchingWall != 0)){
			e.getVelocity().y = 1f;
			touchingGround = false;
			if(touchingWall != 0){
				e.getVelocity().x+=0.6f*-touchingWall;
			}
		}

		if(control.isKeyPressed(control.KEY_DUCK)) e.getVelocity().y = -0.5f;
		
		e.getVelocity().y = Math.round(e.getVelocity().y*DP);
		e.getVelocity().y/=DP;
		
		if(Math.round(e.getVelocity().y*DP) == 0){
			e.getVelocity().y = 0;
		}
		
	}
	
	private void updateX(Entity e){
		Vector3f location = e.getLocation();
		
		//Slowdown
		if(Math.round(e.getVelocity().x*10) == 0){
			e.getVelocity().x = 0;
		}else{
			e.getVelocity().x-=0.04*Toolkit.Sign(e.getVelocity().x);
		}
		
		//Hitbox detection
		float afterX = 0;
		int touchingWall = 0;
		if(e.getVelocity().x != 0){
			float initialVelx = e.getVelocity().x;
			
			for(float x = -Toolkit.Sign(initialVelx)*COLLISION_DEPTH; Toolkit.Modulus(x) <= Toolkit.Modulus(initialVelx); x+= Toolkit.Sign(initialVelx)*COLLISION_DEPTH){
				if(insideHitbox(new Vector2f(location.x + x, location.y), new Vector2f(e.getSize().x, e.getSize().y))){
					
					//Can the player wall jump?
					if(!e.stunned()){
						if(control.isKeyPressed(control.KEY_LEFT) && Toolkit.Sign(x) == -1 && !touchingGround){
							touchingWall = -1;
						}
						if(control.isKeyPressed(control.KEY_RIGHT) && Toolkit.Sign(x) == 1 && !touchingGround){
							touchingWall = 1;
						}
					}
					if(bounce && Toolkit.Modulus(x) > accelerationLimit.x/2 && e.stunned()){
						afterX = -x-initialVelx*0.9f;
					}
					break;
				}else{
					e.getVelocity().x = x;
				}
			}
		}
		
		location.x+=e.getVelocity().x;
		this.touchingWall = touchingWall;
		e.setLocation(location);
		e.getVelocity().x+=afterX;
	}
	
	private void updateY(Entity e){
		Vector3f location = e.getLocation();

		//Is the player grounded
		if(Toolkit.Sign(e.getVelocity().y) == -1 && insideHitbox(new Vector2f(e.getLocation().x, e.getLocation().y + e.getVelocity().y-0.04f), new Vector2f(e.getSize().x, e.getSize().y))){
			touchingGround = true;			
		}
		
		//Slowdown
		if(Toolkit.Modulus(e.getVelocity().y) < accelerationLimit.y){
			e.getVelocity().y-=0.02f;
		}
		
		//Hitbox detection
		float afterY = 0;
		if(e.getVelocity().y != 0){
			float initialVely = e.getVelocity().y;
			
			for(float y = -Toolkit.Sign(initialVely)*COLLISION_DEPTH; Toolkit.Modulus(y) <= Toolkit.Modulus(initialVely); y+= Toolkit.Sign(initialVely)*COLLISION_DEPTH){
				if(insideHitbox(new Vector2f(location.x, location.y + y), new Vector2f(e.getSize().x, e.getSize().y))){
					if(bounce && Toolkit.Modulus(y) > accelerationLimit.x/2 && e.stunned()){
						afterY = -y-initialVely*0.3f;
					}
					
				}else{
					e.getVelocity().y = y;
				}
			}
		}
		
		location.y+=e.getVelocity().y;
		e.setLocation(location);
		e.getVelocity().y+=afterY;
	}

	protected boolean insideHitbox(Vector2f location, Vector2f size){
		for(Hitbox hb:Settings.getWorld().getHitboxList()){
			if(hb.AreaIntersect(location, size)){
				return true;
			}
		}
		return false;
	}
	
	protected Hitbox getHitboxCollision(Vector2f location, Vector2f size){
		for(Hitbox hb:Settings.getWorld().getHitboxList()){
			if(hb.AreaIntersect(location, size)){
				return hb;
			}
		}
		return null;
	}
	
}
