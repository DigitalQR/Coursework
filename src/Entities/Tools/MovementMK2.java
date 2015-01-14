package Entities.Tools;

import Control.Settings;
import Entities.Entity;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public class MovementMK2 extends Movement{
	
	protected static final int COLLISION_DEPTH = 10;
	protected static final int DP = 100000;

	public MovementMK2(ControlScheme controlScheme) {
		super(controlScheme);
	}
	
	public void update(Entity e){
		if(!Settings.toggles.get("s_noclip")){
			updateDimension(e);
		}else{
			noclipProcess(e);
		}
		e.normaliseLocation();
		e.setVelocity(new Vector3f(e.getVelocity().x, e.getVelocity().y, 0));
	}
	
	private void touchUpdate(Entity e){
		touchingGround = false;
		touchingWall = 0;
		
		if(!e.stunned()){
			for(int i = 0; i<COLLISION_DEPTH+1; i++){
				float y = Toolkit.LERP(new Vector2f(0,e.getLocation().y), new Vector2f(COLLISION_DEPTH,e.getLocation().y-e.getSize().y/2), i);
				if( insideHitbox(new Vector2f(e.getLocation().x+e.getSize().x/4,y+e.getSize().y/4), new Vector2f(e.getSize().x/2, e.getSize().y/2)) ){
					touchingGround = true;
					e.getVelocity().y = y-e.getLocation().y;
					break;
				}
			}
			
			for(int i = 0; i<COLLISION_DEPTH+1; i++){
				float x = Toolkit.LERP(new Vector2f(0,e.getLocation().x), new Vector2f(COLLISION_DEPTH,e.getLocation().x-e.getSize().x/2), i);
				if( insideHitbox(new Vector2f(x+e.getSize().x/4,e.getLocation().y+e.getSize().y/4), new Vector2f(e.getSize().x/2, e.getSize().y/2)) ){
					touchingWall = -1;
					break;
				}
			}
			for(int i = 0; i<COLLISION_DEPTH+1; i++){
				float x = Toolkit.LERP(new Vector2f(0,e.getLocation().x), new Vector2f(COLLISION_DEPTH,e.getLocation().x+e.getSize().x/2), i);
				if( insideHitbox(new Vector2f(x+e.getSize().x/4,e.getLocation().y+e.getSize().y/4), new Vector2f(e.getSize().x/2, e.getSize().y/2)) ){
					touchingWall = 1;
					break;
				}
			}
		}
	}
	
	private void updateDimension(Entity e){
		Vector3f location = e.getLocation().clone();
		Vector2f afterVel = new Vector2f(0,0);
		if(insideHitbox(new Vector2f(location.x, location.y), new Vector2f(e.getSize().x, e.getSize().x))){
			location.x-=e.getVelocity().x;
			location.y-=e.getVelocity().y;
		}
		
		touchUpdate(e);
		processInput(e);
		

		//Slowdown
		if(Math.round(e.getVelocity().x*10) == 0){
			e.getVelocity().x = 0;
		}else{
			e.getVelocity().x-=0.03*Toolkit.Sign(e.getVelocity().x);
		}
		
		//Slowdown
		if(Toolkit.Modulus(e.getVelocity().y) < accelerationLimit.y){
			e.getVelocity().y-=0.02f;
		}
		
		for(int i = 0; i<COLLISION_DEPTH+1; i++){
			float x = Toolkit.LERP(new Vector2f(0,e.getLocation().x), new Vector2f(COLLISION_DEPTH,e.getLocation().x+e.getVelocity().x), i);
			if( !insideHitbox(new Vector2f(x,location.y), new Vector2f(e.getSize().x, e.getSize().y)) ){
				location.x = x;
			}else{
				e.getVelocity().x = x-e.getLocation().x;
				if(e.stunned()){
					e.getVelocity().x*=-0.9f;
					if(e.getVelocity().x < 0.05f){
						e.getVelocity().x*=5;
					}
				}
				break;
			}
		}
		e.getLocation().x = location.x;
		
		for(int i = 0; i<COLLISION_DEPTH+1; i++){
			float y = Toolkit.LERP(new Vector2f(0,e.getLocation().y), new Vector2f(COLLISION_DEPTH,e.getLocation().y+e.getVelocity().y), i);
			if( !insideHitbox(new Vector2f(location.x,y), new Vector2f(e.getSize().x, e.getSize().y)) ){
				location.y =y;
			}else{
				e.getVelocity().y = y-e.getLocation().y;
				if(e.stunned()){
					e.getVelocity().y*=-0.9;
					if(e.getVelocity().y < 0.05f){
						e.getVelocity().y*=5;
					}
				}
				break;
			}
		}
		e.getLocation().y = location.y;

		e.getVelocity().x+=afterVel.x;
		e.getVelocity().y+=afterVel.y;
	}
	
	protected void processInput(Entity e){
		//X
		//Input
		if(!e.stunned()){
			if(control.isKeyPressed(control.KEY_RIGHT) && e.getVelocity().x < accelerationLimit.x) e.getVelocity().x+=0.08f;
			if(control.isKeyPressed(control.KEY_LEFT) && e.getVelocity().x > -accelerationLimit.x) e.getVelocity().x-=0.08f;
		}
		
		e.getVelocity().x = Math.round(e.getVelocity().x*DP);
		e.getVelocity().x/=DP;
		
		//Y
		if(!e.stunned() && control.isKeyPressed(control.KEY_JUMP) && e.getVelocity().y < accelerationLimit.y && (touchingGround || touchingWall != 0)){
			e.getVelocity().y = 0.3f;
			
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
	
}
