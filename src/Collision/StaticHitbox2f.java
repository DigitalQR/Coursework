package Collision;

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
	
	//Does the point lie inside the hitbox's region?
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
	
	//Does any point of the rectangle lie inside the hitbox's region?
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

	
	//Generates an array of "random" hitbox locations
	public static StaticHitbox2f[] RandomGeneration(int n, int x, int y, int Width, int Height, int MaxSize){
		StaticHitbox2f[] Temp = new StaticHitbox2f[n];
		float divisor = 20;
		
		if(MaxSize == 0){
			MaxSize = 1;
		}
		for(int i = 0; i<n; i++){
			 Temp[i] = new StaticHitbox2f(Toolkit.RandomInt(x, Width)/divisor, Toolkit.RandomInt(y, Height)/divisor, Toolkit.RandomInt(1, MaxSize)/divisor, Toolkit.RandomInt(1, MaxSize)/divisor);
		}
		
		return Temp;
	}
	
}
