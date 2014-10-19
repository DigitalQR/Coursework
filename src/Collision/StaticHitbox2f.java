package Collision;

import java.util.ArrayList;
import java.util.List;

import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;

public class StaticHitbox2f{
	private static int IDTrack = 0;
	public float x, y, width, height;
	int ID;
	
	public StaticHitbox2f(float X, float Y, float WIDTH, float HEIGHT){
		x = X;
		y = Y;
		width = WIDTH;
		height = HEIGHT;
		ID = IDTrack++;
	}
	
	public boolean PointIntersect(float X, float Y){
		float x = Math.round(this.x*100);
		float y = Math.round(this.y*100);
		float width = Math.round(this.width*100);
		float height = Math.round(this.height*100);
		if(X >= x && X <= x+width && Y >= y && Y <= y+height){
			return true;
		}
		return false;
	}
	
	public boolean AreaIntersect(Vector2f a, Vector2f s){
		float X = Math.round(a.x*100);
		float Y = Math.round(a.y*100);
		float WIDTH = Math.round(s.x*100);
		float HEIGHT = Math.round(s.y*100);
		float x = Math.round(this.x*100);
		float y = Math.round(this.y*100);
		float width = Math.round(this.width*100);
		float height = Math.round(this.height*100);
		
		if(PointIntersect(X, Y) || PointIntersect(X+WIDTH, Y) || PointIntersect(X, Y+HEIGHT) || PointIntersect(X+WIDTH, Y+HEIGHT)){
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

	public static List<StaticHitbox2f> RandomGeneration(int n, int x, int y, int Width, int Height, int MaxSize){
		List<StaticHitbox2f> Temp = new ArrayList<StaticHitbox2f>();
		float divisor = 20;
		
		if(MaxSize == 0){
			MaxSize = 1;
		}
		for(int i = 0; i<n; i++){
			 Temp.add(new StaticHitbox2f(Toolkit.RandomInt(x, Width)/divisor, Toolkit.RandomInt(y, Height)/divisor, Toolkit.RandomInt(1, MaxSize)/divisor, Toolkit.RandomInt(1, MaxSize)/divisor));
		}
		
		return Temp;
	}
	
}
