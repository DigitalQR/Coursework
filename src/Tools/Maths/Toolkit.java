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
	
	public static float TendTo(float In, float Aim, float Factor){
		float Temp = 0;
		if(In < Aim){
			Temp = Math.round((In*(1+Factor))*100);
			Temp/=100;
			return Temp;
		}else{
			Temp = Math.round((In*(1-Factor))*100);
			Temp/=100;
			return Temp;
		}
	}
}
