package Collision;

import java.util.ArrayList;
import java.util.List;

import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;

public class SquareHitbox{
	private Vector2f location;
	private Vector2f size;
	
	public SquareHitbox(Vector2f location, Vector2f size){
		this.location = location;
		this.size = size;
	}
	
	public Vector2f getLocation(){
		return location;
	}
	
	public Vector2f getSize(){
		return size;
	}
	
	public boolean PointIntersect(Vector2f point){
		float x = Math.round(location.x*100);
		float y = Math.round(location.y*100);
		float width = Math.round(size.x*100);
		float height = Math.round(size.y*100);
		if(point.x >= x && point.x <= x+width && point.y >= y && point.y <= y+height){
			return true;
		}
		return false;
	}
	
	public boolean AreaIntersect(Vector2f a, Vector2f s){
		float X = Math.round(a.x*100);
		float Y = Math.round(a.y*100);
		float WIDTH = Math.round(s.x*100);
		float HEIGHT = Math.round(s.y*100);
		float x = Math.round(location.x*100);
		float y = Math.round(location.y*100);
		float width = Math.round(size.x*100);
		float height = Math.round(size.y*100);
		
		if(PointIntersect(new Vector2f(X, Y)) || PointIntersect(new Vector2f(X+WIDTH, Y)) || PointIntersect(new Vector2f(X, Y+HEIGHT)) || PointIntersect(new Vector2f(X+WIDTH, Y+HEIGHT))){
			return true;
		}
		if((x >= X && x <= X+WIDTH && y >= Y && y <= Y+HEIGHT ) || 
		   (x+width >= X && x+width <= X+WIDTH && y >= Y && y <= Y+HEIGHT ) || 
		   (x >= X && x <= X+WIDTH && y+height >= Y && y+height <= Y+HEIGHT ) || 
		   (x+width >= X && x+width <= X+WIDTH && y+height >= Y && y+height <= Y+HEIGHT )){
			return true;
		}
		
		return false;
	}

	public static List<SquareHitbox> RandomGeneration(int n, int x, int y, int Width, int Height, int MinSize, int MaxSize){
		List<SquareHitbox> Temp = new ArrayList<SquareHitbox>();
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
