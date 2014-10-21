package Entities.Tools;

import Entities.Assets.Damage;
import Tools.Maths.Toolkit;
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
		
		basic: if(canAttack && control.isKeyPressed(control.KEY_PRIMARY)){
			Vector2f velocity = new Vector2f(0,0);
			Vector2f size = new Vector2f(0.3f, 0.3f);

			float xVel = 0.1f;
			float yVel = 0.2f;
			if(control.isKeyPressed(control.KEY_UP)){
				velocity.y+=yVel*1.1f;
			}else if(control.isKeyPressed(control.KEY_DOWN)){
				velocity.x=xVel*Toolkit.RandomInt(-1, 1);
				velocity.y-=yVel;
			}else if(control.isKeyPressed(control.KEY_LEFT)){
				velocity.x-=xVel;
				velocity.y+=yVel;
			}else if(control.isKeyPressed(control.KEY_RIGHT)){
				velocity.x+=xVel;
				velocity.y+=yVel;
			}else{
				break basic;
			}
			
			Vector2f location = new Vector2f(e.getLocation().x-e.getVelocity().x,e.getLocation().y-e.getVelocity().y);
			
			Damage d = new Damage(location, size, 200, 0.005f, e, true);
			d.setVelocity(velocity);
			Damage.add(d);
			lastAttack = currentTime;
		}
	}

}
