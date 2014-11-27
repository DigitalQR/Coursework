package Control.Visual.Menu.Assets;

import org.newdawn.slick.opengl.Texture;

import Control.Visual.Menu.Assets.Core.Component;
import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;

public class Button extends Component{

	private float[] colour = {1,1,1,1};
	private String message;
	private Texture texture;
	private float textSize = 0.06f;
	
	public Button(Vector3f location, Vector3f size, String message){
		super(location, size);
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	
	public Vector3f getLocation() {
		return location;
	}

	public void setLocation(Vector3f location) {
		this.location = location;
	}

	public Vector3f getSize() {
		return size;
	}

	public void setSize(Vector3f size) {
		this.size = size;
	}

	public float[] getColour() {
		return colour;
	}
	
	public void setTexture(Texture texture){
		this.texture = texture;
	}

	public void setColour(float[] colour) {
		this.colour = colour;
	}
	
	public void setTextSize(float textSize){
		this.textSize = textSize;
	}

	private Cubef getCube(){
		Vector3f location = this.getLERPHUDLocation();
		Cubef cube = new Cubef(new Vector3f(location.x, location.y, location.z), new Vector3f(location.x+size.x, location.y+size.y, location.z+size.z));
		return cube;
	}
	
	public void updateUI(){
		Model m = new Model(getCube());
		m.setRGBA(colour[0], colour[1], colour[2], colour[3]);
		
		if(texture != null){
			m.setTexture(texture);
		}
		Renderer.render(m);
		
		Vector3f location = this.getLERPHUDLocation();
		text.setRGBA(0, 0, 0, 1);
		text.drawText(message, new Vector3f(location.x, location.y+size.y/2, location.z+size.z+0.001f), size.y*(textSize/7), 7f);
	}

	protected void process(){
	}

}
