package Collision;

import Tools.Maths.Vector2f;

public class ColourHitbox extends SquareHitbox{

	public ColourHitbox(Vector2f location, Vector2f size) {
		super(location, size);
	}
	
	private float[] RGBA = {1,1,1,1};
	
	public float[] getRGBA(){
		return RGBA;
	}
	
	public void setRGBA(float[] RGBA){
		this.RGBA = RGBA;
	}

}
