package Entities.Tools;

import Collision.SquareHitbox;
import Control.Settings;
import Entities.Entity;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public class Movement extends Component{

	private ControlScheme control;
	private Vector2f accelerationLimit = new Vector2f(0.2f, 40f);
	boolean touchingGround = false;
	int touchingWall = 0;
	
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
		if(control.isKeyPressed(control.KEY_UP) && e.getVelocity().y < accelerationLimit.y) e.getVelocity().y+=0.08f;
		if(control.isKeyPressed(control.KEY_DOWN) && e.getVelocity().y > -accelerationLimit.y) e.getVelocity().y-=0.08f;
		
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
		//e.setLocation(location);
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
					break;
				}else{
					e.getVelocity().x = x;
				}
			}
		}
		
		location.x+=e.getVelocity().x;
		this.touchingWall = touchingWall;
		e.setLocation(location);
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
		if(Toolkit.Sign(e.getVelocity().y) == -1 && insideHitbox(new Vector2f(location.x, location.y + e.getVelocity().y-0.03f), new Vector2f(e.getSize().x, e.getSize().y))){
			touchingGround = true;
		}
		
		//Hitbox detection
		if(e.getVelocity().y != 0){
			float initialVely = e.getVelocity().y;
			
			for(float y = 0; Toolkit.Modulus(y) <= Toolkit.Modulus(initialVely); y+= Toolkit.Sign(initialVely)*0.01f){
				if(insideHitbox(new Vector2f(location.x, location.y + y), new Vector2f(e.getSize().x, e.getSize().y))){
					break;
				}else{
					e.getVelocity().y = y;
				}
			}
		}
		
		location.y+=e.getVelocity().y;
		e.setLocation(location);
	}
	
	private boolean insideHitbox(Vector2f location, Vector2f size){
		for(SquareHitbox hb:Settings.hb){
			if(hb.AreaIntersect(location, size)){
				return true;
			}
		}
		return false;
	}
	
}
