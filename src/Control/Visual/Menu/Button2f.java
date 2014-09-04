package Control.Visual.Menu;

import org.newdawn.slick.opengl.Texture;

import RenderEngine.Loader;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public class Button2f{

	private Vector2f Location, Size;
	public float Depth = 0;
	private String Message;
	private Texture fontTexture;
	
	public Button2f(Vector2f Location, Vector2f Size, String Message){
		this.Location = Location;
		this.Size = Size;
		this.Message = Message;
		fontTexture = Loader.loadTexture("Font/BLACK");
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
	
	public Cubef getCube(){
		Cubef cube = new Cubef(new Vector3f(Location.x/10, Location.y/10, Depth), new Vector3f((Location.x+Size.x)/10, (Location.y+Size.y)/10, Depth+0.5f));
		return cube;
	}
	
	Model[] model;
	
	public Model[] getText(){
		return model;
	}
	
	public void generateText(Font font, float size, float divisor){
		Vector3f v = getCube().getLocation();
		Cubef[] text = font.generateString(new Vector3f(v.x, v.y-0.1f, v.z+Depth+0.5f), Message, size, divisor);
		
		model = new Model[text.length];
		for(int i = 0; i<model.length; i++){
			model[i] = new Model(text[i]);
			model[i].setTexture(fontTexture);
		}
	}

}


