package Control.Visual.DepricatedStage;

import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public class Button2f{

	private Vector2f Location, Size;
	public float Depth = 0;
	private String Message;
	
	public Button2f(Vector2f Location, Vector2f Size, String Message){
		this.Location = Location;
		this.Size = Size;
		this.Message = Message;
	}
	
	public String getMessage(){
		return Message;
	}

	public void setMessage(String message){
		Message = message;
	}

	public Model getModel(){
		Model m = new Model(getCube());
		return m;
	}
	
	public Vector2f getLocation(){
		return Location;
	}
	
	public Cubef getCube(){
		Cubef cube = new Cubef(new Vector3f(Location.x/10, Location.y/10, Depth), new Vector3f((Location.x+Size.x)/10, (Location.y+Size.y)/10, Depth+0.5f));
		return cube;
	}
	
	public Vector3f getTextLocation(){
		return new Vector3f((Location.x+1)/10, (Location.y+Size.y/3)/10, Depth+0.6f);
	}
	

}


