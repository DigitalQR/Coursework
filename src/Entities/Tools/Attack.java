package Entities.Tools;

import Entities.Assets.Damage;
import RenderEngine.Model.Animation;
import Tools.Maths.Vector2f;

public class Attack extends Component{

	ControlScheme control;
	private float lastAttack = 0;
	private int coolDown = 800;
	
	public Attack(ControlScheme control){
		this.control = control;
	}
	
	public void update(Entity e){
		float currentTime = System.nanoTime();
		boolean canAttack = currentTime - lastAttack >= coolDown*1000000;

		if(canAttack && control.isKeyPressed(control.KEY_PRIMARY)){
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
			
			Animation anime = Damage.CUBE;
			anime.scaleBy(1.1f);
			
			Damage d = new Damage(new Vector2f(0,0), size, 600, 0.4f, e, true, anime);
			d.setVelocity(velocity);
			Damage.add(d);
			lastAttack = currentTime;
		
		
		}else if(canAttack && control.isKeyPressed(control.KEY_SECONDARY)){
			Vector2f velocity = new Vector2f(0,0);
			Vector2f size = new Vector2f(0.3f, 0.3f);
			
			if(control.isKeyPressed(control.KEY_UP)){
				velocity.y+=size.y-0.2f;
			}if(control.isKeyPressed(control.KEY_DOWN)){
				velocity.y-=size.y;
				e.getVelocity().y+=0.25f;
			}if(control.isKeyPressed(control.KEY_LEFT)){
				velocity.x-=size.x;
			}if(control.isKeyPressed(control.KEY_RIGHT)){
				velocity.x+=size.x;
			}
			
			Vector2f location = new Vector2f(e.getLocation().x-e.getVelocity().x,e.getLocation().y-e.getVelocity().y);

			Animation anime = Damage.CUBE;
			anime.scaleBy(1.1f);
			
			Damage d = new Damage(location, size, 500, 0.05f, e, false, anime);
			d.setVelocity(velocity);
			Damage.add(d);
			lastAttack = currentTime;
		}
	}
	

}
