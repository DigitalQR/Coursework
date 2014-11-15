package Control.Visual.Menu.Assets.Core;

import Tools.Maths.Vector3f;
import Control.Camera;

public abstract class Component extends FocusableItem{

	private Action action;
	private boolean drawn = true;
	protected Vector3f location;
	protected Vector3f size;
	
	public Component(Vector3f location, Vector3f size){
		this.location = location;
		this.size = size;
	}
	
	public void setAction(Action action){
		this.action = action;
	}

	public Vector3f getLERPHUDLocation(){
		Vector3f location = new Vector3f(this.location.x, this.location.y, this.location.z);
		location.x -= Camera.getLERPLocation().x;
		location.y -= Camera.getLERPLocation().y;
		location.z -= Camera.getLERPLocation().z;
		return location;
	}

	public Vector3f getHUDLocation(){
		Vector3f location = new Vector3f(this.location.x, this.location.y, this.location.z);
		location.x -= Camera.getLocation().x;
		location.y -= Camera.getLocation().y;
		location.z -= Camera.getLocation().z;
		return location;
	}
	
	public void setDrawn(boolean drawn){
		this.drawn = drawn;
	}
	
	protected void runAction(){
		if(action != null){
			action.run(this.getID());
		}
	} 
	
	public void update(){
		runAction();
		process();
	}
	
	public void draw(){
		if(drawn){
			updateUI();
		}
	}
	
	protected abstract void process();
	protected abstract void updateUI();
}
