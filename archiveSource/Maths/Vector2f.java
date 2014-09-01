package Tools.Maths;

public class Vector2f {
	
	public float x, y;
	
	public Vector2f(float f, float g){
		this.x = f;
		this.y = g;
	}
	
	public Vector2f add(Vector2f a){
		return new Vector2f(x+a.x, y+a.y);
	}
}
