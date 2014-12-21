package Control.Visual.Menu.Assets;

import Control.Visual.Menu.Assets.Core.Component;
import Tools.Maths.Vector3f;

public class Slider extends Component{

	private float value = 0f;
	
	private float[] RGBA = {1,1,1,1};
	
	public Slider(Vector3f location, Vector3f size) {
		super(location, size);
	}

	protected void process(){
		
	}
	
	public void setRGBA(float[] rgba){
		RGBA = rgba;
	}

	protected void updateUI(){
		Button backdrop = new Button(new Vector3f(location.x, location.y-size.y*0f, location.z), new Vector3f(size.x, 0.05f, size.z), "");
		backdrop.setColour(new float[]{1,1,1,0.5f});
		backdrop.draw();

		Button pointer = new Button(new Vector3f(location.x+size.x*value-0.01f, location.y, location.z), new Vector3f(0.02f, 0.1f, size.z), "");
		pointer.setColour(RGBA);
		pointer.draw();
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}
	
}

