package RenderEngine.Menu;

import Tools.Maths.Cubef;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public class Button2f{

	private static int IDTrack = 0;
	private static final float DEFAULT_DEPTH = 0.3f;
	private Vector2f Size = new Vector2f(0,0);
	private Vector2f Location = new Vector2f(0,0);
	private int ID;
	
	public Button2f(Vector2f location, Vector2f size){
		Size = size;
		Location = location;
		ID = IDTrack++;
	}
	
	public int getID(){
		return ID;
	}

	public Cubef getCubef(){
		Cubef temp = new Cubef(new Vector3f(Location.x/10, -Location.y/10, -DEFAULT_DEPTH), new Vector3f((Location.x+Size.x)/10, (-Location.y+Size.y)/10, 0f));
		return temp;
	}
	
	public static void cycle(){
		IDTrack = 0;
	}

}


