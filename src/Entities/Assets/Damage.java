package Entities.Assets;

import java.util.ArrayList;

import Entities.Tools.Entity;
import Tools.Maths.Vector2f;

public class Damage extends Asset{

	private static ArrayList<Damage> damage = new ArrayList<Damage>();

	@SuppressWarnings("unchecked")
	public static ArrayList<Damage> getDamageInfo(){
		ArrayList<Damage> dam = (ArrayList<Damage>)damage.clone();
		return dam;
	}
	
	public static void updateDamage(){
		float currentTime = System.nanoTime();
		
		for(Damage d: getDamageInfo()){
			d.update();
			
			if(currentTime - d.birth >= d.life*1000000){
				damage.remove(d);
			}
		}
	}
	
	public static void add(Damage d){
		damage.add(d);
	}
	
	public static void remove(Damage d){
		damage.remove(d);
	}
	
	public static Damage damageTouching(Entity e){
		for(Damage d: getDamageInfo()){
			if(!d.parent.equals(e) && d.touching(new Vector2f(e.getLocation().x, e.getLocation().y), new Vector2f(e.getSize().x, e.getSize().y))){
				return d;
			}
		}
		return null;
	}
	
	public static boolean touchingDamage(Entity e){
		for(Damage d: getDamageInfo()){
			if(!d.parent.equals(e) && d.touching(new Vector2f(e.getLocation().x, e.getLocation().y), new Vector2f(e.getSize().x, e.getSize().y))){
				return true;
			}
		}
		return false;
	}
	
	public static boolean touchingDamage(Vector2f location){
		for(Damage d: getDamageInfo()){
			if(d.touching(location)){
				return true;
			}
		}
		return false;
	}

	public static boolean touchingDamage(Vector2f location, Vector2f size){
		for(Damage d: getDamageInfo()){
			if(d.touching(location, size)){
				return true;
			}
		}
		return false;
	}
		
	private Entity parent;
	private Vector2f location;
	private Vector2f size;
	private Vector2f velocity = new Vector2f(0,0);
	private float birth;
	private float life;
	private float damageValue;
	private boolean stuckToParent;
	
	public Damage(Vector2f location, Vector2f size, int life, float damageValue, Entity e, boolean stuckToParent){
		this.location = location;
		this.size = size;
		this.parent = e;
		birth = System.nanoTime();
		this.life = life;
		this.damageValue = damageValue;
		this.stuckToParent = stuckToParent;
	}
	
	public float getDamageValue(){
		return damageValue;
	}
	
	public Vector2f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
	}

	public Vector2f getSize() {
		return size;
	}
	
	public void setSize(Vector2f size) {
		this.size = size;
	}

	public Vector2f getLocation() {
		return location;
	}

	public void setLocation(Vector2f location) {
		this.location = location;
	}
	
	public Vector2f getToDamage(Entity e){
		return new Vector2f(getLocation().x-e.getLocation().x, getLocation().y-e.getLocation().y);
	}

	public void update(){
		if(stuckToParent){
			location.x = parent.getLocation().x;
			location.y = parent.getLocation().y;
		}
		location.x+=velocity.x;
		location.y+=velocity.y;
	}

	public boolean touching(Vector2f point){
		float x = Math.round(location.x*100);
		float y = Math.round(location.y*100);
		float width = Math.round(size.x*100);
		float height = Math.round(size.y*100);
		if(point.x >= x && point.x <= x+width && point.y >= y && point.y <= y+height){
			return true;
		}
		return false;
	}
	
	public boolean touching(Vector2f a, Vector2f s){
		float X = Math.round(a.x*100);
		float Y = Math.round(a.y*100);
		float WIDTH = Math.round(s.x*100);
		float HEIGHT = Math.round(s.y*100);
		float x = Math.round(location.x*100);
		float y = Math.round(location.y*100);
		float width = Math.round(size.x*100);
		float height = Math.round(size.y*100);
		
		if(touching(new Vector2f(X, Y)) || touching(new Vector2f(X+WIDTH, Y)) || touching(new Vector2f(X, Y+HEIGHT)) || touching(new Vector2f(X+WIDTH, Y+HEIGHT))){
			return true;
		}
		if((x >= X && x <= X+WIDTH && y >= Y && y <= Y+HEIGHT ) || 
		   (x+width >= X && x+width <= X+WIDTH && y >= Y && y <= Y+HEIGHT ) || 
		   (x >= X && x <= X+WIDTH && y+height >= Y && y+height <= Y+HEIGHT ) || 
		   (x+width >= X && x+width <= X+WIDTH && y+height >= Y && y+height <= Y+HEIGHT )){
			return true;
		}
		
		return false;
	}

}
