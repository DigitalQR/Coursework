package Entities.Tools;

import Entities.Assets.Damage;
import Tools.Maths.Vector2f;

public class Attack extends Component{

	ControlScheme control;
	private float lastAttack = 0;
	private int attackCoolDown = 800;
	private int blockCoolDown = 1600;
	
	public Attack(ControlScheme control){
		this.control = control;
	}
	
	public void update(Entity e){
		float currentTime = System.nanoTime();
		boolean canAttack = currentTime - lastAttack >= attackCoolDown*1000000;
		boolean canBlock = currentTime - lastAttack >= blockCoolDown*1000000;

		if(canAttack && !e.stunned()){
			if(control.isKeyPressed(control.KEY_PRIMARY)){
				Vector2f velocity = new Vector2f(0,0);
				Vector2f size = new Vector2f(0.25f, 0.5f);
	
				if(control.isKeyPressed(control.KEY_UP)){
					velocity.y+=size.y-0.2f;
				}if(control.isKeyPressed(control.KEY_DOWN)){
					velocity.y-=size.y;
				}if(control.isKeyPressed(control.KEY_LEFT)){
					velocity.x-=size.x;
				}if(control.isKeyPressed(control.KEY_RIGHT)){
					velocity.x+=size.x;
				}
				
				Damage d = new Damage(new Vector2f(0,0), size, 600, 0.2f, e, true, Damage.CUBE);
				d.setVelocity(velocity);
				d.setDamageVelocity(new Vector2f(velocity.x*0.9f, velocity.y*0.9f));
				Damage.add(d);
				lastAttack = currentTime;
			
			
			}else if( control.isKeyPressed(control.KEY_SECONDARY)){
				Vector2f velocity = new Vector2f(0,0);
				Vector2f size = new Vector2f(0.3f, 0.6f);
				
				if(control.isKeyPressed(control.KEY_UP)){
					velocity.y+=size.y-0.2f;
					e.getVelocity().y-=0.125f;
				}if(control.isKeyPressed(control.KEY_DOWN)){
					velocity.y-=size.y;
					e.getVelocity().y+=0.25f;
				}if(control.isKeyPressed(control.KEY_LEFT)){
					velocity.x-=size.x;
					e.getVelocity().x+=0.25f;
				}if(control.isKeyPressed(control.KEY_RIGHT)){
					velocity.x+=size.x;
					e.getVelocity().x-=0.25f;
				}
				
				Vector2f location = new Vector2f(e.getLocation().x+e.getVelocity().x,e.getLocation().y+e.getVelocity().y);
				
				Damage d = new Damage(location, size, 500, 0.05f, e, false, Damage.CUBE);
				d.setVelocity(velocity);
				d.setDamageVelocity(new Vector2f(velocity.x*0.75f, velocity.y*0.75f));
				Damage.add(d);
				lastAttack = currentTime;
			
			
			}else if(canBlock && control.isKeyPressed(control.KEY_BLOCK)){
				
				
				lastAttack = currentTime;
			}
		}
	}
	

}
