package Entities.Tools;

import Control.Audio.Sound;
import Entities.Entity;
import Entities.Assets.Damage;
import Entities.Assets.Shield;
import Tools.Maths.Vector2f;

public class Attack extends Component{

	ControlScheme control;
	private float lastAttack = 0;
	private float lastShield = 0;
	private int attackCoolDown = 800;
	private int blockCoolDown = 2000;
	
	public Attack(ControlScheme control){		
		this.control = control;
		
	}
	
	public void update(Entity e){
		float currentTime = System.nanoTime();
		boolean canAttack = currentTime - lastAttack >= attackCoolDown*1000000;
		boolean canBlock = currentTime - lastShield >= blockCoolDown*1000000;

		if(canAttack && !e.stunned()){
			if(control.isKeyPressed(control.KEY_PRIMARY)){
				Vector2f velocity = new Vector2f(0,0);
				Vector2f size = new Vector2f(0.3f, 0.6f);
	
				if(control.isKeyPressed(control.KEY_UP)){
					velocity.y+=0.5f;
				}if(control.isKeyPressed(control.KEY_DOWN)){
					velocity.y-=0.5f;
				}if(control.isKeyPressed(control.KEY_LEFT)){
					velocity.x-=0.25f;
				}if(control.isKeyPressed(control.KEY_RIGHT)){
					velocity.x+=0.25f;
				}

				Sound attackSound = new Sound("Effects/Attack");
				
				Damage d = new Damage(new Vector2f(0,0), size, 600, 0.2f, e, true, Damage.CUBE, attackSound);
				d.setVelocity(velocity);
				d.setDamageVelocity(new Vector2f(velocity.x*0.8f, velocity.y*0.8f));
				Damage.add(d);
				
				lastAttack = currentTime;
			
			
			}else if( control.isKeyPressed(control.KEY_SECONDARY)){
				Vector2f velocity = new Vector2f(0,0);
				Vector2f size = new Vector2f(0.3f, 0.6f);
				
				if(control.isKeyPressed(control.KEY_UP)){
					velocity.y+=size.y-0.3f;
					e.getVelocity().y-=0.125f;
				}if(control.isKeyPressed(control.KEY_DOWN)){
					velocity.y-=size.y-0.3f;
					e.getVelocity().y+=0.25f;
				}if(control.isKeyPressed(control.KEY_LEFT)){
					velocity.x-=size.x;
					e.getVelocity().x+=0.3f;
				}if(control.isKeyPressed(control.KEY_RIGHT)){
					velocity.x+=size.x;
					e.getVelocity().x-=0.3f;
				}
				
				Vector2f location = new Vector2f(e.getLocation().x+e.getVelocity().x,e.getLocation().y+e.getVelocity().y);
				
				Sound rangeSound = new Sound("Effects/Shoot");
				
				Damage d = new Damage(location, size, 500, 0.05f, e, false, Damage.CUBE, rangeSound);
				d.setVelocity(velocity);
				d.setDamageVelocity(new Vector2f(velocity.x*0.8f, velocity.y*0.8f));
				Damage.add(d);
				
				lastAttack = currentTime;
			
			
			}else if(canBlock && control.isKeyPressed(control.KEY_BLOCK)){
				
				Shield.add(new Shield(600, new float[]{0,1,0,1f}, e));
	
				lastAttack = currentTime;
				lastShield = currentTime;
			}
		}
	}
	

}
