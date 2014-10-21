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
	
	public boolean equalsNull(){
		return (x == 0 && y == 0);
	}
	
	public void normalise(){
		float angle = (float) Math.atan(y/x);
		x = (float) Math.cos(angle);
		y = (float) Math.sin(angle);
	}
}
