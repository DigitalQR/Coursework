package Control.Visual.Entities;

import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;
import Entities.Player;

public class PlayerTemplate{
	
	long lastUpdate;
	private Vector3f location;
	private Vector3f size;
	
	public PlayerTemplate(Vector3f location){
		this.location = location;
		this.size = new Vector3f(0.2f, 0.6f, 0.2f);
		lastUpdate = System.nanoTime();
	}
	
	protected void setLastUpdate(long l){
		lastUpdate = l;
	}
	
	public Vector3f getSize(){
		return size;
	}

	public void setSize(Vector3f size){
		this.size = size;
	}

	public Vector3f getLocation(){
		return location;
	}
	
	public long getStandardTime(){
		return (long) lastUpdate;
	}
	
	public PlayerTemplate clone(){
		PlayerTemplate p = new PlayerTemplate(this.location);
		p.setLastUpdate(lastUpdate);
		
		return p;
	}
	
	public void updateData(Player p){
		lastUpdate = System.nanoTime();
		location = new Vector3f(p.getLocation().x, p.getLocation().y, p.getLocation().z);
	}
	
	public Vector3f LERPLocation(PlayerTemplate p){
		long firstTime = this.getStandardTime();
		long lastTime = p.getStandardTime();
		long time = p.getStandardTime()+System.nanoTime()-this.lastUpdate;

		float x = Toolkit.LERP(new Vector2f(lastTime, p.getLocation().x), new Vector2f(firstTime, this.getLocation().x), time);
		float y = Toolkit.LERP(new Vector2f(lastTime, p.getLocation().y), new Vector2f(firstTime, this.getLocation().y), time);
		
		return new Vector3f(x, y, location.z);
	}
}
