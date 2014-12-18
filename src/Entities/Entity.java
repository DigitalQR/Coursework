package Entities;

import Control.Camera;
import Control.Settings;
import Control.Audio.Sound;
import Entities.Tools.Component;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public abstract class Entity{
	
	private Component[] component = new Component[0];
	protected Vector3f location;
	protected float LastUpdate = 0;
	protected Vector3f LastLocation = new Vector3f(0,0,0);
	private Vector3f velocity;
	
	public int killCount = 0;
	private float stunTime = 0;
	private static float stunDuration;
	
	public boolean stunned(){
		if(System.nanoTime() - stunTime >= stunDuration*1000000){
			return false;
		}else{
			return true;
		}
	}
	
	public void stun(int duration){
		Sound hitSound = new Sound("Effects/Hit");
		hitSound.play();
		
		stunDuration = duration;
		stunTime = System.nanoTime();
	}
	
	public Vector3f getVelocity(){
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}

	private Vector3f size;
	
	public Entity(Vector3f location, Vector3f size){
		this.setLocation(location);
		this.setVelocity(new Vector3f(0,0,0));
		this.setSize(size);
	}

	public void addComponent(Component c){
		Component[] temp = new Component[component.length+1];
		
		for(int i = 0; i<component.length; i++){
			temp[i] = component[i];
		}
		temp[component.length] = c;
		
		component = temp;
	}
	
	public void removeComponent(Component c){
		Component[] temp = new Component[component.length-1];
		
		int track = 0;
		for(int i = 0; i<component.length; i++){
			if(!component[i].equals(c)){
				temp[track] = component[i];
				track++;
			}
		}
		component = temp;
	}
	
	
	protected void updateComponents(){
		for(Component c: component){
			c.update(this);
		}
	}
	
	public abstract void update();
	
	public Vector3f getSize() {
		return size;
	}

	public void setSize(Vector3f size) {
		this.size = size;
	}

	public Vector3f getLocation(){
		return location;
	}

	public void setLocation(Vector3f location){
		this.location = location;
	}
	
	public void normaliseLocation(){
		Vector3f loc = getLocation();
		loc.x = Math.round(loc.x*100);
		loc.x/=100;
		loc.y = Math.round(loc.y*100);
		loc.y/=100;
	}

	private Vector3f LERPLocation = new Vector3f(0,0,0);
	
	public void processLERPLocation(){

		if(Settings.toggles.get("s_lerp")){
			float LookupTime = Camera.getLERPTime();
			float CurrentTime = System.nanoTime();
			Vector3f location = this.getLocation();
			
			float x = Toolkit.LERPValue(new Vector2f(LastUpdate, LastLocation.x), new Vector2f(CurrentTime, location.x), LookupTime);
			float y = Toolkit.LERP(new Vector2f(LastUpdate, LastLocation.y), new Vector2f(CurrentTime, location.y), LookupTime);
			LERPLocation = new Vector3f(x, y, 0);
		}else{
			LERPLocation = new Vector3f(location.x, location.y, 0);
		}
		
	}
	
	public Vector3f getLERPLocation(){
		return LERPLocation;
	}
}
