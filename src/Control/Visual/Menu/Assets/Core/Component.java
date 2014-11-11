package Control.Visual.Menu.Assets.Core;

import Tools.Maths.Vector3f;
import Control.Visual.Font;

public abstract class Component {

	private static int IDTrack = 0;
	protected static Font text = new Font("Font/Default");
	private int globalFocus = -1;
	
	private int ID;
	private boolean privateFocus = false;
	private Action action;
	protected Vector3f location;
	protected Vector3f size;
	
	public Component(Vector3f location, Vector3f size){
		this.location = location;
		this.size = size;
		this.ID = IDTrack++;
	}
	
	public void setGlobalFocus(){
		globalFocus = ID;
	}
	
	public void unfocusGlobal(){
		if(globalFocus == ID){
			globalFocus = -1;
		}
	}
	
	public boolean hasGlobalFocus(){
		return globalFocus == ID;
	}
	
	public int getID(){
		return ID;
	}
	
	public void setPrivateFocus(boolean focus){
		this.privateFocus = focus;
	}
	
	public boolean getPrivateFocus(){
		return privateFocus;
	}
	
	public void setAction(Action action){
		this.action = action;
	}
	
	protected void runAction(){
		if(action != null){
			action.run();
		}
	}
	
	protected abstract void update();
	public abstract void draw();
}
