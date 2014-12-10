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
	
	public static float LERP(Vector2f pos0, Vector2f pos1, float value){
		return (float)(pos0.y + (pos1.y-pos0.y)*(value-pos0.x)/(pos1.x-pos0.x));
	}
	
	public static float LERPValue(Vector2f pos0, Vector2f pos1, float value){
		return (float)(pos0.y + (pos1.y-pos0.y)*(value-pos0.x)/(pos1.x-pos0.x));
	}
	
	public static Vector3f toCartesian(Vector3f p){
		float r = p.x;
		float theta = p.y;
		float psi = p.z;
		
		float x = (float) (r*Math.sin(Math.toRadians(theta))*Math.cos(Math.toRadians(psi)));
		float y = (float) (r*Math.sin(Math.toRadians(theta))*Math.sin(Math.toRadians(psi)));
		float z = (float) (r*Math.cos(Math.toRadians(theta)));
		
		return new Vector3f(x,y,z);
	}
	
	public static Vector3f toPolar(Vector3f p){
		float x = p.x;
		float y = p.y;
		float z = p.z;
		
		float r = (float) Math.sqrt(x*x + y*y + z*z);
		float theta = (float) Math.acos(z/r);
		float psi = (float) Math.atan(y/x);
		
		return new Vector3f(r,theta,psi);
	}
	
}
