package Entities.Assets;

import java.util.ArrayList;

import Collision.SquareHitbox;
import Entities.Entity;
import RenderEngine.Model.Animation;
import RenderEngine.Model.Model;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public class Shield extends Asset{

	private static ArrayList<Shield> shields = new ArrayList<Shield>();
	public final static Animation SHIELD = new Animation("Shield/Spin",30);

	@SuppressWarnings("unchecked")
	public static ArrayList<Shield> getShieldInfo(){
		ArrayList<Shield> shield = (ArrayList<Shield>)shields.clone();
		return shield;
	}
	
	public static void updateShields(){
		for(Shield s: getShieldInfo()){
			s.update();
		}
	}
	
	public static void add(Shield s){
		shields.add(s);
	}
	
	public static void remove(Shield s){
		shields.remove(s);
	}
	
	private static final Vector2f SIZE = new Vector2f(0.5f,0.8f); 
	private float birth;
	private int life;
	private float[] RGBA;
	private Entity parent;

	
	public Shield(int life, float[] RGBA, Entity e){
		birth = System.nanoTime();
		this.life = life;
		this.RGBA = RGBA;
		this.parent = e;
	}
	
	public Entity getParent() {
		return parent;
	}

	public int getLife() {
		return life;
	}

	public float[] getRGBA() {
		return RGBA;
	}

	public Vector3f getLocation(){
		Vector3f loc = parent.getLERPLocation();
		
		return new Vector3f(loc.x-(SIZE.x-parent.getSize().x)/2, loc.y-(SIZE.y-parent.getSize().y)/2, loc.z);
	}
	
	public Vector3f getSize(){
		return new Vector3f(SIZE.x, SIZE.y, SIZE.x);
	}
	
	public void update(){
		float currentTime = System.nanoTime();

		if(currentTime - birth >= life*1000000){
			parent.stun(800);
			remove(this);
		}else{
		
			float x = parent.getLocation().x;
			float y = parent.getLocation().y;
			SquareHitbox hb = new SquareHitbox(new Vector2f(x,y), SIZE);
			
			for(Damage d: Damage.getDamageInfo()){
				if(hb.AreaIntersect(d.getLocation(), d.getSize()) && !d.getParent().equals(parent)){
					d.setParent(parent);
	
					Vector2f vel = d.getVelocity();
					d.setVelocity(new Vector2f(-vel.x*1.5f, -vel.y*1.5f));
	
					Vector2f dam = d.getDamageVelocity();
					d.setDamageVelocity(new Vector2f(-dam.x*1.5f, -dam.y*1.5f));
					
					d.resetLife();
					remove(this);
				}
			}
		}
	}
	
	public Model getModel(){
		Model m = SHIELD.getCurrentFrame();
		Vector3f loc = parent.getLERPLocation();
		m.setLocation(new Vector3f(loc.x+0.1f, loc.y-0.4f, 0.5f));
		m.setRGBA(RGBA[0], RGBA[1], RGBA[2], RGBA[3]);
		m.scaleBy(4);
		return m;
	}
	
	
}
