package Entities.Assets;

import java.util.ArrayList;

import Collision.Hitbox;
import Control.Settings;
import Control.Audio.Sound;
import Entities.Entity;
import RenderEngine.Model.Animation;
import RenderEngine.Model.Model;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public class Damage extends Asset{

	private static ArrayList<Damage> damage = new ArrayList<Damage>();
	public final static Animation CUBE = new Animation("Cube/Damage", 10);

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
			try{
				if(!d.parent.equals(e) && d.touching(new Vector2f(e.getLocation().x, e.getLocation().y), new Vector2f(e.getSize().x, e.getSize().y))){
					return d;
				}
			}catch(NullPointerException ex){}
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
	private Vector2f damageVelocity = new Vector2f(0,0);
	private float birth;
	private float life;
	private float damageValue;
	private boolean stuckToParent;
	private Animation animation;
	
	public Damage(Vector2f location, Vector2f size, int life, float damageValue, Entity e, boolean stuckToParent, Animation animation, Sound sound){
		this.location = location;
		this.size = size;
		this.parent = e;
		birth = System.nanoTime();
		this.life = life;
		this.damageValue = damageValue;
		this.stuckToParent = stuckToParent;
		this.animation = animation;
		sound.play();
		update();
		
		if(!stuckToParent){
			location.x = parent.getLocation().x-size.x/2+parent.getSize().x/2+velocity.x;
			location.y = parent.getLocation().y-size.y/2+parent.getSize().y/2+velocity.y;
		}
	}
	
	public boolean isStuckToParent() {
		return stuckToParent;
	}

	public void resetLife(){
		this.birth = System.nanoTime();
	}
	
	public float getLife() {
		return life;
	}

	public void setParent(Entity e){
		this.parent = e;
	}
	
	public Model getModel(){
		Model m = animation.getCurrentFrame();
		m.setLocation(new Vector3f(location.x+size.x/2, location.y-size.y, 0.5f));
		float[] colour = {1,1,1};
		m.setRGBA(colour[0], colour[1], colour[2], 1);
		m.scaleBy(30*((size.x+size.y)/2));
		
		return m;
	}
	
	public Entity getParent(){
		return parent;
	}
	
	public float getDamageValue(){
		return damageValue;
	}
	
	public Vector2f getDamageVelocity() {
		return damageVelocity;
	}

	public void setDamageVelocity(Vector2f velocity) {
		this.damageVelocity = velocity;
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
			location.x = parent.getLocation().x-size.x/2+parent.getSize().x/2+velocity.x;
			location.y = parent.getLocation().y-size.y/2+parent.getSize().y/2+velocity.y;
		}else{
			location.x+=velocity.x;
			location.y+=velocity.y;
		}
		for(Hitbox hb: Settings.getWorld().getHitboxList()){
			if(hb.AreaIntersect(new Vector2f(location.x+size.x/2, location.y+size.y/2), new Vector2f(0,0))){
				damage.remove(this);
				break;
			}
		}
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
