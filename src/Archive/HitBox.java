package Archive;

public class HitBox {
	private static int IDTrack = 0;
	int x, y, width, height, ID;
	
	
	public HitBox(int X, int Y, int WIDTH, int HEIGHT){
		x = X;
		y = Y;
		width = WIDTH;
		height = HEIGHT;
		ID = IDTrack++;
	}
	
	//Does the point lie inside the hitbox's region?
	public boolean PointIntersect(int X, int Y){
		if(X >= x && X <= x+width && Y >= y && Y <= y+height){
			return true;
		}
		return false;
	}
	
	//Does any point of the rectangle lie inside the hitbox's region?
	public boolean AreaIntersect(int X, int Y, int WIDTH, int HEIGHT){
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

	//Adds an entry to an array with data type HitBox
	public static HitBox[] Add(HitBox[] Array, HitBox Entry){
		HitBox[] Temp = Array.clone();
		Array = new HitBox[Temp.length+1];
		
		for(int i = 0; i<Temp.length; i++){
			Array[i] = Temp[i];
		}
		Array[Array.length-1] = Entry;
		
		return Array;
	}

	//Removes an entry to an array with data type HitBox
	public static HitBox[] Remove(HitBox[] Array, int id){
		HitBox[] Temp = Array.clone();
		Array = new HitBox[Temp.length-1];
		
		int Track = 0;
		for(int i = 0; i<Temp.length; i++){
			if(Temp[i].ID != id){
				Array[Track] = Temp[i];
			}
		}
		return Array;
	}
	
	//Generates an array of "random" hitbox locations
	public static HitBox[] RandomGeneration(int n, int x, int y, int Width, int Height, int MaxSize){
		HitBox[] Temp = new HitBox[n];
		
		if(MaxSize == 0){
			MaxSize = 1;
		}
		for(int i = 0; i<n; i++){
			 Temp[i] = new HitBox(Maths.RandInt(x, Width), Maths.RandInt(y, Height), Maths.RandInt(1, MaxSize), Maths.RandInt(1, MaxSize));
		}
		
		return Temp;
	}
	
	//Batch checking
	public static boolean BatchCheckArea(HitBox[] HB, int x, int y, int width, int height){
		for(int i = 0; i<HB.length; i++){
			if(HB[i].AreaIntersect(x,y,width,height)){
				return true;
			}
		}
		
		return false;
	}
	
}
