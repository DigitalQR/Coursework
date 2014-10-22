package Entities.Tools;

import Tools.Maths.Vector3f;

public abstract class Entity{
	
	private Component[] component = new Component[0];
	private Vector3f location;
	private Vector3f velocity;
	public int killCount = 0;
	
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
		loc.z = Math.round(loc.z*100);
		loc.z/=100;
	}
}
