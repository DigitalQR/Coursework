package Archive;


public class Maths{

	static final long LaunchTime = System.currentTimeMillis();
	
	//Splits a string up into an array of it's content
	public static String[] getParameters(String input, char Separator){
		input+=Separator;
		String[] Str = new String[0];
		String Temp = "";
		for(int i = 0; i<input.length(); i++){
			if(input.charAt(i) != Separator){
				Temp+=input.charAt(i);
			}else{
				if(!Temp.replace(Separator, ' ').isEmpty()){
					Str = append(Str, Temp);
					Temp = "";
				}
			}
		}
		
		return Str;
	}
	
	//Appends to a string array
	private static String[] append(String[] Str, String s){
		String[] Temp = Str.clone();
		Str = new String[Temp.length+1];
		
		for(int i = 0; i<Temp.length; i++){
			Str[i] = Temp[i];
		}
		Str[Temp.length] = s;
		return Str;
	}
	
	
	
	public static String getTime(){
		int Dif = (int) (System.currentTimeMillis()-LaunchTime);
		int Seconds = (int) Math.round(Dif/1000);
		int Minutes = 0;
		int Hours = 0;
		while(Seconds >= 60){
			Minutes++;
			Seconds-=60;
		}
		while(Minutes >= 60){
			Hours++;
			Minutes-=60;
		}
		return String.format("%02d:%02d:%02d", Hours, Minutes, Seconds);
	}
	
	
	
	public static double Modulus(double a){
		return (double) a*Sign(a);
	}
	
	public static int Sign(double a){
		if(a<0){
			return -1;
		}else{
			return 1;
		}
	}
	
	public static double Round(double a, int b){
		a = Math.round(a*Math.pow(10, b));
		a = a/Math.pow(10, b);
		return a;
	}
	
	public static int RandInt(int Min, int Max){
		return (int) (Max - Math.round(Math.random()*(Max-Min)));
	}
	
	
	//x,y-Origin,x,y
	public static double Direction(int xo, int yo, int x, int y){
		int dx = xo-x;
		int dy = yo-y;
		double Angle = 0;
		if(dx == 0){
			dx = 1;
		}
		if(dy == 0){
			dy = 1;
		}
		Angle = (long) Modulus(Math.toDegrees(Math.atan(dy/dx)));
		if(Angle == 0){
			Angle = (long) 90-Modulus(Math.toDegrees(Math.atan(dx/dy)));
		}
		
		if(x < xo){
			Angle = 90+(90-Angle);
		}
		if(y > yo){
			Angle*=-1;
		}
		return Angle;
	}
	
	public static double[] Matrix4by4x2by1(double[][] a, double[] b){
		if(a[0].length == b.length){
			double[] c = new double[2];
			c[0] = a[0][0]*b[0]+a[0][1]*b[1];
			c[1] = a[1][0]*b[0]+a[1][1]*b[1];
			return c;
		}else{
			System.err.println("Matrix multiplication impossible.");
		}
		return null;
	}
	
	public static long getGradient(int x1, int y1, int x2, int y2){
		int dx = x2-x1;
		int dy = y2-y1;
		if(dx == 0){
			dx = 1;
		}
		if(dy == 0){
			dy = 1;
		}
		return dy/dx;
	}
	
	public static double interpret(int xs, int ys, int xe, int ye, String s, int Speed){
		double Angle = Math.atan2((ye-ys), (xe-xs));
		
		if(s.equals("dx")){
			return (Speed * Math.cos(Angle));
		}else{
			return (Speed * Math.sin(Angle));
		}
	}
	
}
