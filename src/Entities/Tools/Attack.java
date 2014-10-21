package Entities.Tools;

import Entities.Assets.Damage;
import Tools.Maths.Vector2f;

public class Attack extends Component{

	ControlScheme control;
	private float lastAttack = 0;
	private int coolDown = 500;
	
	public Attack(ControlScheme control){
		this.control = control;
	}
	
	public void update(Entity e){
		float currentTime = System.nanoTime();
		boolean canAttack = currentTime - lastAttack >= coolDown*1000000;

		if(canAttack && control.isKeyPressed(control.KEY_PRIMARY)){
			Vector2f velocity = new Vector2f(0,0);
			Vector2f size = new Vector2f(0.25f, 0.5f);

			float xVel = 0.25f;
			float yVel = 0.3f;
			if(control.isKeyPressed(control.KEY_UP)){
				velocity.y+=yVel*1.1f;
			}if(control.isKeyPressed(control.KEY_DOWN)){
				velocity.y-=yVel;
			}if(control.isKeyPressed(control.KEY_LEFT)){
				velocity.x-=xVel;
			}if(control.isKeyPressed(control.KEY_RIGHT)){
				velocity.x+=xVel;
			}
			
			Vector2f location = new Vector2f(e.getLocation().x-e.getVelocity().x,e.getLocation().y-e.getVelocity().y);
			
			Damage d = new Damage(location, size, 200, 0.04f, e, true);
			d.setVelocity(velocity);
			Damage.add(d);
			lastAttack = currentTime;
		}

		if(canAttack && control.isKeyPressed(control.KEY_SECONDARY)){
			Vector2f velocity = new Vector2f(0,0);
			Vector2f size = new Vector2f(0.2f, 0.4f);

			float xVel = 0.2f;
			float yVel = 0.2f;
			if(control.isKeyPressed(control.KEY_UP)){
				velocity.y+=yVel*1.1f;
			}if(control.isKeyPressed(control.KEY_DOWN)){
				velocity.y-=yVel;
				e.getVelocity().y+=0.25f;
			}if(control.isKeyPressed(control.KEY_LEFT)){
				velocity.x-=xVel;
			}if(control.isKeyPressed(control.KEY_RIGHT)){
				velocity.x+=xVel;
			}
			
			Vector2f location = new Vector2f(e.getLocation().x-e.getVelocity().x,e.getLocation().y-e.getVelocity().y);
			
			Damage d = new Damage(location, size, 1000, 0.02f, e, false);
			d.setVelocity(velocity);
			Damage.add(d);
			lastAttack = currentTime;
		}
	}
	

}
