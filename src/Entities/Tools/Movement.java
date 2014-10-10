package Entities.Tools;

import Collision.StaticHitbox2f;
import Control.Settings;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public class Movement extends Component{

	private ControlScheme control;
	private Vector2f velocity;
	private Vector2f accelerationLimit = new Vector2f(0.2f, 40f);
	boolean touchingGround = false;
	int touchingWall = 0;
	
	public Movement(ControlScheme controlScheme){
		control = controlScheme;
	}
	
	public void setVelocity(Vector2f m){
		velocity = m;
	}
	
	public void addVelocity(Vector2f m){
		velocity = velocity.add(m);
	}
	
	public int getTouchingWall(){
		return touchingWall;
	}
	
	public boolean getTouchingGround(){
		return touchingGround;
	}
	
	public void update(Entity e){
		velocity = new Vector2f(e.getVelocity().x, e.getVelocity().y);
		
		updateX(e);
		updateY(e);
		
		e.normaliseLocation();
		e.setVelocity(new Vector3f(velocity.x, velocity.y, 0));
		hitboxCheck(e);
	}
	
	private void hitboxCheck(Entity e){
		if(insideHitbox(new Vector2f(e.getLocation().x, e.getLocation().y), new Vector2f(e.getSize().x,e.getSize().y))){
			System.err.println("Player inside hitbox");
			System.exit(-1);
		}
	}
	
	private void updateX(Entity e){
		//Input
		if(control.isKeyPressed(control.KEY_RIGHT) && velocity.x < accelerationLimit.x) velocity.x+=0.08f;
		if(control.isKeyPressed(control.KEY_LEFT) && velocity.x > -accelerationLimit.x) velocity.x-=0.08f;
		
		//Normalisation
		Vector3f location = e.getLocation();
		location.x = Math.round(location.x*100);
		location.x/=100;

		velocity.x = Math.round(velocity.x*100);
		velocity.x/=100;
		
		//Slowdown
		if(Math.round(velocity.x*100) == 0){
			velocity.x = 0;
		}else{
			velocity.x-=0.04*Toolkit.Sign(velocity.x);
		}
		
		//Hitbox detection
		int touchingWall = 0;
		if(velocity.x != 0){
			float initialVelx = velocity.x;
			
			for(float x = 0; Toolkit.Modulus(x) <= Toolkit.Modulus(initialVelx); x+= Toolkit.Sign(initialVelx)*0.01f){
				if(insideHitbox(new Vector2f(location.x + x, location.y), new Vector2f(e.getSize().x, e.getSize().y))){
					
					//Can the player wall jump?
					if(control.isKeyPressed(control.KEY_LEFT) && Toolkit.Sign(x) == -1 && !touchingGround){
						touchingWall = -1;
					}
					if(control.isKeyPressed(control.KEY_RIGHT) && Toolkit.Sign(x) == 1 && !touchingGround){
						touchingWall = 1;
					}
					
					break;
				}else{
					velocity.x = x;
				}
			}
		}
		
		location.x+=velocity.x;
		this.touchingWall = touchingWall;
		e.setLocation(location);
	}
	
	private void updateY(Entity e){
		//Input
		if(control.isKeyPressed(control.KEY_UP) && velocity.y < accelerationLimit.y && (touchingGround || touchingWall != 0)){
			velocity.y = 0.3f;
			touchingGround = false;
			if(touchingWall != 0){
				velocity.x+=0.6f*-touchingWall;
			}
		}
		if(control.isKeyPressed(control.KEY_DOWN)) velocity.y = -0.5f;
		
		if(Toolkit.Modulus(velocity.y) < accelerationLimit.y){
			velocity.y-=0.02f;
		}
		
		//Normalise
		Vector3f location = e.getLocation();
		location.y = Math.round(location.y*100);
		location.y/=100;

		velocity.y = Math.round(velocity.y*100);
		velocity.y/=100;
		
		if(Math.round(velocity.y*100) == 0){
			velocity.y = 0;
		}
		
		//Is the player grounded
		if(Toolkit.Sign(velocity.y) == -1 && insideHitbox(new Vector2f(location.x, location.y + velocity.y-0.03f), new Vector2f(e.getSize().x, e.getSize().y))){
			touchingGround = true;
		}
		
		//Hitbox detection
		if(velocity.y != 0){
			float initialVely = velocity.y;
			
			for(float y = 0; Toolkit.Modulus(y) <= Toolkit.Modulus(initialVely); y+= Toolkit.Sign(initialVely)*0.01f){
				if(insideHitbox(new Vector2f(location.x, location.y + y), new Vector2f(e.getSize().x, e.getSize().y))){
					break;
				}else{
					velocity.y = y;
				}
			}
		}
		
		location.y+=velocity.y;
		e.setLocation(location);
	}
	
	private boolean insideHitbox(Vector2f location, Vector2f size){
		for(StaticHitbox2f hb:Settings.hb){
			if(hb.AreaIntersect(location, size)){
				return true;
			}
		}
		return false;
	}
	
}
