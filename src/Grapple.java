
public class Grapple{

	boolean InUse = false;
	static final int XVEL = 0, YVEL = 1;
	int x, y;
	
	public Grapple(int X, int Y){
		x = X;
		y = Y;
	}
	
	
	public double Interpret(int Aim, int X, int Y, int Direction){
		if(Aim == XVEL){
			return Maths.Sign(x-X);
		}else if(Aim == YVEL){
			return Maths.Sign(y-Y);
		}
		return 0;
	}
	
	
	public static Grapple GenerateGrapple(HitBox[] HB, int x, int y, double xvel, double yvel){
		
		Main: for(int i = 0; i<1000; i++){
			x+=xvel;
			y+=yvel;
			if(HitBox.BatchCheckArea(HB, x, y, 10, 10)){
				break Main;
			}
		}
		
		return new Grapple(x, y);
	}
	
}
