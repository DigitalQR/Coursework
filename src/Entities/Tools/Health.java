package Entities.Tools;

import Tools.Maths.Vector3f;
import Entities.Assets.Damage;

public class Health extends Component{

	public float scale = 0.6f;
	public float factor = 0.0f;
	private float lastHit = 0;
	private  float immuneTime = 100;
	
	public void reset(){
		factor = 0.0f;
		lastHit = 0;
		immuneTime = 100;
		isDead = false;
	}
	
	public void update(Entity e) {
		float currentTime = System.nanoTime();
		
		if(currentTime - lastHit >= immuneTime*1000000){
			Damage damage = Damage.damageTouching(e);
			
			if(damage != null){				
				factor+=damage.getDamageValue();
				lastHit = currentTime;
				Damage.remove(damage);

				float x = damage.getVelocity().x*(5f+factor)*scale;
				float y = damage.getVelocity().y*(2f+factor)*scale;
				
				e.setVelocity(new Vector3f(x, y, 0));
			}
		}
	}
	
	public boolean isDead = false;
	private float deathTime = 0;
	private  float respawnTime = 1000;
	
	public void kill(){
		if(!isDead){
			isDead = true;
			deathTime = System.nanoTime();
		}
	}
	
	public boolean canRespawn(){
		if(System.nanoTime() - deathTime >= respawnTime*1000000){
			return true;
		}else{
			return false;
		}
	}

}
