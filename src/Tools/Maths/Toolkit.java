package Tools.Maths;

public class Toolkit {
	
	public static float Modulus(float a){
		return a*Sign(a);
	}
	
	public static int Sign(double a){
		if(a<0){
			return -1;
		}else if(a>0){
			return 1;
		}else{
			return 0;
		}
	}
	
	public static int RandomInt(int Min, int Max){
		return (int) (Max - Math.round(Math.random()*(Max-Min)));
	}
	
	public static float Difference2f(Vector2f a, Vector2f b){
		float dif = (float) Math.sqrt((b.x-a.x)*(b.x-a.x) + (b.x-a.x)*(b.x-a.x));
		return dif;
	}
	
	public static float Differencef(float a, float b){
		return (float)b-a;
	}
	
	public static void main(String[] args){
		Vector2f pos0 = new Vector2f(16.5f,83.2f);
		Vector2f pos1 = new Vector2f(304,96);
		float value = 504f;
		
		System.out.println(LERP(pos0, pos1, value));
	}
	
	public static float LERP(Vector2f pos0, Vector2f pos1, float value){
		return (float)(pos0.y + (pos1.y-pos0.y)*(value-pos0.x)/(pos1.x-pos0.x));
	}
}
