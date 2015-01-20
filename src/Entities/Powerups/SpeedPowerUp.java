package Entities.Powerups;

import RenderEngine.Model.Animation;
import RenderEngine.Model.Model;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;
import Entities.Player;
import Entities.Powerup;

public class SpeedPowerUp extends Powerup{

	private static Animation animation = new Animation("Powerup/Speed", 100);
	public static final int ID = 0;
	private static final int LIFE = STANDARD_LIFE;
	
	public SpeedPowerUp(){
		super(new Vector2f(-10000, -10000), LIFE);
		Vector2f location = getSpawn();
		this.location.x = location.x;
		this.location.y = location.y;
	}
	
	public SpeedPowerUp(Vector2f location){
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
		p.health.setDamageFactor(3);
		p.getMovement().setAccelerationFactor(1.5f);
		p.getMovement().setJumpCap(2);
	}

	protected void dettachEffects(Player p){
		p.health.setDamageFactor(1);
		p.getMovement().setAccelerationFactor(1f);
		p.getMovement().setJumpCap(1);
	}
	
	public int getID(){
		return ID;
	}

	public String encode(){
		return ID + "," + location.x + "," + location.y;
	}

}
