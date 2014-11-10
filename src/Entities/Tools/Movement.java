package Entities.Tools;

import Collision.Hitbox;
import Collision.MovingHitbox;
import Control.Settings;
import Entities.Entity;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public class Movement extends Component{

	private ControlScheme control;
	private Vector2f accelerationLimit = new Vector2f(0.2f, 40f);
	private boolean touchingGround = false;
	private int touchingWall = 0;
	private boolean bounce = true;
	
	public Movement(ControlScheme controlScheme){
		control = controlScheme;
	}
	
	public int getTouchingWall(){
		return touchingWall;
	}
	
	public boolean getTouchingGround(){
		return touchingGround;
	}
	
	public void update(Entity e){
		if(!Settings.toggles.get("s_noclip")){	
			
			float xVel = Toolkit.Sign(e.getVelocity().x)*e.getSize().x/2;
			float yVel = Toolkit.Sign(e.getVelocity().y)*e.getSize().y/2;
			if(touchingGround){
				yVel = -e.getSize().x/2;
			}
			Hitbox hb = getHitboxCollision(new Vector2f(e.getLocation().x+xVel, e.getLocation().y+yVel), new Vector2f(e.getSize().x, e.getSize().y)) ;
			
			if(hb != null){
				if(hb.getType() == Hitbox.TYPE_DYNAMIC){
					MovingHitbox mhb = (MovingHitbox) hb;
					e.getLocation().x += mhb.velocity.x;
					e.getLocation().y += mhb.velocity.y;
				}
			}
			
			updateX(e);
			updateY(e);
		}else{
			noclipProcess(e);
		}
		e.normaliseLocation();
		e.setVelocity(new Vector3f(e.getVelocity().x, e.getVelocity().y, 0));
	}
	
	private void noclipProcess(Entity e){
		//Input
		if(control.isKeyPressed(control.KEY_RIGHT) && e.getVelocity().x < accelerationLimit.x) e.getVelocity().x+=0.08f;
		if(control.isKeyPressed(control.KEY_LEFT) && e.getVelocity().x > -accelerationLimit.x) e.getVelocity().x-=0.08f;
		if(control.isKeyPressed(control.KEY_UP) && e.getVelocity().y < accelerationLimit.x) e.getVelocity().y+=0.08f;
		if(control.isKeyPressed(control.KEY_DOWN) && e.getVelocity().y > -accelerationLimit.x) e.getVelocity().y-=0.08f;
		
		//Normalisation
		Vector3f location = e.getLocation();
		location.x = Math.round(location.x*100);
		location.x/=100;
		location.y = Math.round(location.y*100);
		location.y/=100;

		e.getVelocity().x = Math.round(e.getVelocity().x*100);
		e.getVelocity().x/=100;
		e.getVelocity().y = Math.round(e.getVelocity().y*100);
		e.getVelocity().y/=100;

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
	
	private void updateX(Entity e){
		//Input
		if(!e.stunned()){
			if(control.isKeyPressed(control.KEY_RIGHT) && e.getVelocity().x < accelerationLimit.x) e.getVelocity().x+=0.08f;
			if(control.isKeyPressed(control.KEY_LEFT) && e.getVelocity().x > -accelerationLimit.x) e.getVelocity().x-=0.08f;
		}
		//Normalisation
		Vector3f location = e.getLocation();
		location.x = Math.round(location.x*100);
		location.x/=100;

		e.getVelocity().x = Math.round(e.getVelocity().x*100);
		e.getVelocity().x/=100;
		
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
			
			for(float x = 0; Toolkit.Modulus(x) <= Toolkit.Modulus(initialVelx); x+= Toolkit.Sign(initialVelx)*0.01f){
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
		//Input
		if(!e.stunned() && control.isKeyPressed(control.KEY_JUMP) && e.getVelocity().y < accelerationLimit.y && (touchingGround || touchingWall != 0)){
			e.getVelocity().y = 0.3f;
			touchingGround = false;
			if(touchingWall != 0){
				e.getVelocity().x+=0.6f*-touchingWall;
			}
		}

		if(control.isKeyPressed(control.KEY_DUCK)) e.getVelocity().y = -0.5f;
		
		if(Toolkit.Modulus(e.getVelocity().y) < accelerationLimit.y){
			e.getVelocity().y-=0.02f;
		}
		
		//Normalise
		Vector3f location = e.getLocation();
		location.y = Math.round(location.y*100);
		location.y/=100;

		e.getVelocity().y = Math.round(e.getVelocity().y*100);
		e.getVelocity().y/=100;
		
		if(Math.round(e.getVelocity().y*100) == 0){
			e.getVelocity().y = 0;
		}

		//Is the player grounded
		if(Toolkit.Sign(e.getVelocity().y) == -1 && insideHitbox(new Vector2f(e.getLocation().x, e.getLocation().y + e.getVelocity().y-0.04f), new Vector2f(e.getSize().x, e.getSize().y))){
			touchingGround = true;			
		}
		
		//Hitbox detection
		float afterY = 0;
		if(e.getVelocity().y != 0){
			float initialVely = e.getVelocity().y;
			
			for(float y = 0; Toolkit.Modulus(y) <= Toolkit.Modulus(initialVely); y+= Toolkit.Sign(initialVely)*0.01f){
				if(insideHitbox(new Vector2f(location.x, location.y + y), new Vector2f(e.getSize().x, e.getSize().y))){
					if(bounce && Toolkit.Modulus(y) > accelerationLimit.x/2 && e.stunned()){
						afterY = -y-initialVely*0.9f;
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

	
	private boolean insideHitbox(Vector2f location, Vector2f size){
		for(Hitbox hb:Settings.hb){
			if(hb.AreaIntersect(location, size)){
				return true;
			}
		}
		return false;
	}
	
	private Hitbox getHitboxCollision(Vector2f location, Vector2f size){
		for(Hitbox hb:Settings.hb){
			if(hb.AreaIntersect(location, size)){
				return hb;
			}
		}
		return null;
	}
	
}
