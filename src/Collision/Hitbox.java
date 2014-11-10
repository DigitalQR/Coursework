package Collision;

import java.util.ArrayList;
import java.util.List;

import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;

public abstract class Hitbox{
	protected Vector2f location;
	protected Vector2f size;
	
	public static final int TYPE_STATIC = 0, TYPE_DYNAMIC = 1;
	private int type;
	
	public Hitbox(Vector2f location, Vector2f size, int type){
		this.location = location;
		this.size = size;
		this.type = type;
	}
	
	public int getType(){
		return type;
	}
	
	public abstract void update();
	
	
	public Vector2f getLocation(){
		return location;
	}
	
	public Vector2f getSize(){
		return size;
	}
	
	public boolean AreaIntersect(Vector2f a, Vector2f s){
		Vector2f at = new Vector2f(location.x, location.y);
		Vector2f ab = new Vector2f(location.x+size.x, location.y+size.y);
		Vector2f bt = new Vector2f(a.x, a.y);
		Vector2f bb = new Vector2f(a.x+s.x, a.y+s.y);
		
		return !(Math.round(ab.x*100) < Math.round(bt.x*100) || Math.round(bb.x*100)  < Math.round(at.x*100)  || Math.round(ab.y*100)  < Math.round(bt.y*100)  || Math.round(bb.y*100)  < Math.round(at.y*100) );
	}

	public static List<Hitbox> RandomGeneration(int n, int x, int y, int Width, int Height, int MinSize, int MaxSize){
		List<Hitbox> Temp = new ArrayList<Hitbox>();
		float divisor = 10;
		
		if(MaxSize == 0){
			MaxSize = 1;
		}
		
		for(int i = 0; i<n; i++){
			Vector2f size = new Vector2f(Toolkit.RandomInt(MinSize, MaxSize), Toolkit.RandomInt(MinSize, MaxSize));
			Vector2f location = new Vector2f(x+Toolkit.RandomInt(0, (int)((Width-size.x))), y+Toolkit.RandomInt(0, (int)((Height-size.y))));

			size.x/=divisor;
			size.y/=divisor;
			location.x/=divisor;
			location.y/=divisor;
			
			 Temp.add(new SquareHitbox(location, size));
		}
		
		return Temp;
	}
	
}
