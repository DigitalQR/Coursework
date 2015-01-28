package Entities.Powerups;

import RenderEngine.Model.Animation;
import RenderEngine.Model.Model;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;
import Entities.Player;
import Entities.Powerup;

public class PowerPowerUp extends Powerup{

	private static Animation animation = new Animation("Powerup/Power", 110);
	public static final int ID = 1;
	private static final int LIFE = STANDARD_LIFE;
	
	public PowerPowerUp() {
		super(new Vector2f(-10000, -10000), LIFE);
		Vector2f location = getSpawn();
		this.location.x = location.x;
		this.location.y = location.y;
	}
	
	public PowerPowerUp(Vector2f location){
		super(location, LIFE);
	}

	public Model getModel() {
		Model m = animation.getCurrentFrame();
		float life = this.getLifeLeft();
		
		if(life < 0.4f){
			life*=2.5f;
			m.setRGBA(1, 1, 1, life);
		}else{
			m.setRGBA(1, 1, 1, 1);
		}
		m.scaleBy(7f);
		m.setLocation(new Vector3f(location.x+size.x/2, location.y-size.y/3, 0.5f));
		
		return m;
	}

	public void updateInfo(){
	}

	protected void attachEffects(Player p){
		p.health.setDamageFactor(0.5f);
		p.getMovement().setAccelerationFactor(0.85f);
	}

	protected void dettachEffects(Player p){
		p.health.setDamageFactor(1);
		p.getMovement().setAccelerationFactor(1f);
	}
	
	public int getID(){
		return ID;
	}

	public String encode(){
		return ID + "," + location.x + "," + location.y;
	}
}
