package Entities.Powerups;

import RenderEngine.Model.Animation;
import RenderEngine.Model.Model;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;
import Entities.Powerup;

public class SpeedPowerUp extends Powerup{

	private static Animation animation = new Animation("Powerup/Speed", 100);
	private static final int LIFE = 3000;
	
	public SpeedPowerUp() {
		super(new Vector2f(-10000, -10000), LIFE);
		Vector2f location = getSpawn();
		this.location.x = location.x;
		this.location.y = location.y;
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

	public void update(){
		if(this.getLifeLeft() < 0){
			powerups.remove(this);
		}
	}

}
