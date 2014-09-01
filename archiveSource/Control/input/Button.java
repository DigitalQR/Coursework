package Control.input;

public class Button {
	
	private int ID;
	private float State;
	
	public Button(int ID, float State){
		this.ID = ID;
		this.State = State;
	}
	
	public int getID(){
		return ID;
	}
	
	public float getState(){
		return State;
	}
}
