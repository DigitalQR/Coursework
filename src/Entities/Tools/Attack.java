package Entities.Tools;

import Collision.SquareHitbox;
import Control.Audio.Sound;
import Entities.Entity;
import Entities.Player;
import Entities.Powerup;
import Entities.Assets.Damage;
import Entities.Assets.Shield;
import Tools.Maths.Vector2f;

public class Attack extends Component{

	ControlScheme control;
	private float lastAttack = 0;
	private float lastShield = 0;
	private int attackCoolDown = 800;
	private int blockCoolDown = 2000;
	
	public Attack(ControlScheme control){		
		this.control = control;
	}
	
	public void update(Entity e){
		float currentTime = System.nanoTime();
		boolean canAttack = currentTime - lastAttack >= attackCoolDown*1000000;
		boolean canBlock = currentTime - lastShield >= blockCoolDown*1000000;
		boolean canPickup = currentTime - lastAttack >= 500*1000000;

		if(canAttack && !e.stunned()){
			if(control.isKeyPressed(control.KEY_PRIMARY)){
				Vector2f velocity = new Vector2f(0,0);
				Vector2f size = new Vector2f(0.3f, 0.6f);
	
				if(control.isKeyPressed(control.KEY_UP)){
					velocity.y+=0.4f;
				}if(control.isKeyPressed(control.KEY_DOWN)){
					velocity.y-=0.4f;
				}if(control.isKeyPressed(control.KEY_LEFT)){
					velocity.x-=0.2f;
				}if(control.isKeyPressed(control.KEY_RIGHT)){
					velocity.x+=0.2f;
				}

				Sound attackSound = new Sound("Effects/Attack");
				
				Damage d = new Damage(new Vector2f(0,0), size, 600, 0.2f, e, true, Damage.CUBE, attackSound);
				d.setVelocity(velocity);
				d.setDamageVelocity(new Vector2f(velocity.x*0.8f, velocity.y*0.8f));
				Damage.add(d);
				
				lastAttack = currentTime;
			
			
			}else if( control.isKeyPressed(control.KEY_SECONDARY)){
				Vector2f velocity = new Vector2f(0,0);
				Vector2f size = new Vector2f(0.3f, 0.6f);
				
				if(control.isKeyPressed(control.KEY_UP)){
					velocity.y+=0.5f;
					e.getVelocity().y-=0.125f;
				}if(control.isKeyPressed(control.KEY_DOWN)){
					velocity.y-=0.5f;
					e.getVelocity().y+=0.25f;
				}if(control.isKeyPressed(control.KEY_LEFT)){
					velocity.x-=0.25f;
					e.getVelocity().x+=0.3f;
				}if(control.isKeyPressed(control.KEY_RIGHT)){
					velocity.x+=0.25f;
					e.getVelocity().x-=0.3f;
				}
				
				Vector2f location = new Vector2f(e.getLocation().x+e.getVelocity().x,e.getLocation().y+e.getVelocity().y);
				
				Sound rangeSound = new Sound("Effects/Shoot");
				
				Damage d = new Damage(location, size, 500, 0.05f, e, false, Damage.CUBE, rangeSound);
				d.setVelocity(velocity);
				d.setDamageVelocity(new Vector2f(velocity.x*0.8f, velocity.y*0.8f));
				Damage.add(d);
				
				lastAttack = currentTime;
			
			
			}else if(canBlock && control.isKeyPressed(control.KEY_BLOCK)){
				
				Shield.add(new Shield(600, new float[]{0,1,0,1f}, e));
	
				lastAttack = currentTime;
				lastShield = currentTime;
			}else if(control.isKeyPressed(control.KEY_GRAB) && canPickup){
				
				boolean pickedUp = false;
				for(Powerup p: Powerup.getPowerUps()){
					SquareHitbox hb = new SquareHitbox(new Vector2f(p.getLocation().x, p.getLocation().y), new Vector2f(p.getSize().x, p.getSize().y));
					if(hb.AreaIntersect(new Vector2f(e.getLocation().x, e.getLocation().y), new Vector2f(e.getSize().x, e.getSize().y))){
						Player pl = (Player) e;
						pl.setPowerUp(p);
						pickedUp = true;
						break;
					}
				}
				
				if(!pickedUp){
					Player pl = (Player) e;
					Powerup p = pl.getPowerup();
					if(p != null){
						System.out.println("Det");
						p.dettach(pl);
						pl.setPowerUp(null);
					}
				}
				lastAttack = currentTime;
			}
		}
	}
	

}
