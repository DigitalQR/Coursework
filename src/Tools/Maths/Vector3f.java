package Tools.Maths;

public class Vector3f {
	
	public float x, y, z;
	
	public Vector3f(float f, float g, float h){
		this.x = f;
		this.y = g;
		this.z = h;
	}
	
	public Vector3f clone(){
		return new Vector3f(x,y,z);
	}
}
