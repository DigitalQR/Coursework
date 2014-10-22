package Entities.Tools;

import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;
import Entities.Assets.Damage;

public class Health extends Component{

	public Vector2f scale = new Vector2f(0.3f, 0.4f);
	public float factor = 0.0f;
	private float lastHitTime = 0;
	private Entity lastHit = null;
	private  float immuneTime = 100;
	
	public void reset(){
		factor = 0.0f;
		lastHitTime = 0;
		immuneTime = 100;
		isDead = false;
		lastHit = null;
	}
	
	public void update(Entity e) {
		float currentTime = System.nanoTime();
		
		if(currentTime - lastHitTime >= immuneTime*1000000){
			Damage damage = Damage.damageTouching(e);
			
			if(damage != null){				
				factor+=damage.getDamageValue();
				lastHitTime = currentTime;
				lastHit = damage.getParent();
				Damage.remove(damage);

				float x = damage.getVelocity().x*(5f+factor)*scale.x;
				float y = damage.getVelocity().y*(2f+factor)*scale.y;
				
				e.setVelocity(new Vector3f(x, y, 0));
			}
		}
	}
	
	public boolean isDead = false;
	private float deathTime = 0;
	private  float respawnTime = 3500;
	private int deathCount = 0;
	
	
	public void kill(){
		if(!isDead){
			isDead = true;
			deathCount++;
			deathTime = System.nanoTime();
			if(lastHit != null){
				lastHit.killCount++;
			}
		}
	}
	
	public int getDeathCount(){
		return deathCount;
	}
	
	public boolean canRespawn(){
		if(System.nanoTime() - deathTime >= respawnTime*1000000){
			return true;
		}else{
			return false;
		}
	}
	
	public float getTimeRemaining(){
		return respawnTime*1000000 - (System.nanoTime() - deathTime);
	}

}
