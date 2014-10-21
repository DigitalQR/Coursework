package Entities.Tools;

import Tools.Maths.Vector3f;
import Entities.Assets.Damage;

public class Health extends Component{

	public float scale = 0.7f;
	public float factor = 0.0f;
	private float lastHit = 0;
	private  float immuneTime = 200;
	
	public void update(Entity e) {
		float currentTime = System.nanoTime();
		
		if(currentTime - lastHit >= immuneTime*1000000){
			Damage damage = Damage.damageTouching(e);
			
			if(damage != null){				
				factor+=damage.getDamageValue();
				lastHit = currentTime;
				Damage.remove(damage);

				float x = e.getVelocity().x+damage.getVelocity().x*(5f+factor)*scale;
				float y = e.getVelocity().y+damage.getVelocity().y*(2f+factor)*scale;
				
				e.setVelocity(new Vector3f(x, y, 0));
			}
		}
	}

}
