package Entities.Tools;

import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;
import Control.Settings;
import Control.Visual.Stage.GamemodeStage;
import Entities.Entity;
import Entities.Player;
import Entities.Assets.Damage;

public class Health extends Component{

	public static int killCap = -1;
	public static int stockCap = -1;
	public static int timeCap = -1;
	public static float startTime = 0;
	
	public Vector2f scale = new Vector2f(0.3f, 0.4f);
	public float factor = 0.0f;
	private float factorFactor = 1;
	private float lastHitTime = 0;
	public Entity lastHit = null;
	public Player parent = null;
	private  float immuneTime = 100;

	public Health(Player p){
		parent = p;
	}
	
	public void reset(){
		if(stockCap != -1){
			stock = stockCap;
		}else{
			stock = -1;
		}
	}
	
	public void spawn(){
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
				factor+=damage.getDamageValue()*factorFactor;
				lastHitTime = currentTime;
				lastHit = damage.getParent();
				Damage.remove(damage);
				
				float x = damage.getDamageVelocity().x*(5f+factor)*scale.x;
				float y = damage.getDamageVelocity().y*(2f+factor)*scale.y;
				
				e.stun(500+Math.round(factor*75));
				e.setVelocity(new Vector3f(x, y, 0));
			}
		}
	}
	
	public void setDamageFactor(float f){
		factorFactor = f;
	}
	
	public void clientUpdate(Entity e){
		float currentTime = System.nanoTime();
		
		if(currentTime - lastHitTime >= immuneTime*1000000){
			Damage damage = Damage.damageTouching(e);
			
			if(damage != null){
				Damage.remove(damage);
				e.stun(500+Math.round(factor*75));
			}
		}
	}
	
	public boolean isDead = false;
	public int stock = -1;
	
	private float deathTime = 0;
	private  float respawnTime = 3500;
	private int deathCount = 0;
		
	public void kill(boolean increaseKills){
		isDead = true;
		deathCount++;
		deathTime = System.nanoTime();
		
		if(!Settings.isClientActive() && stockCap != -1 && increaseKills){
			stock--;
		}
		
		if(increaseKills && GamemodeStage.isGameOver() == -1){
			parent.incrementTotalDeaths();
			
			if(lastHit != null){
				lastHit.killCount++;
				Player p1 = (Player) lastHit;
				p1.incrementTotalKills();
				lastHit = null;
			}
		}
		
	}
	
	public int getDeathCount(){
		return deathCount;
	}
	
	public boolean canRespawn(){
		if(stockCap != -1 && stock <= 0){
			return false;
		}else if(System.nanoTime() - deathTime >= respawnTime*1000000){
			return true;
		}else{
			return false;
		}
	}
	
	public float getTimeRemaining(){
		return respawnTime*1000000 - (System.nanoTime() - deathTime);
	}

}
