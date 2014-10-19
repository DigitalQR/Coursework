package Archive;

public class Joint {
	
	int x, y, Parent = -1, Length, Angle = 0;
	
	public Joint(int X, int Y){
		setLocation(X, Y);
	}
	
	public Joint(int parent){
		Parent = parent;
		setLocation(0, 0);
	}
	
	public void setLocation(int X, int Y){
		x = X;
		y = Y;
	}
	
	public int GenX(){
		return (int) (x+Math.round(Length*Math.cos(-Math.toRadians(Angle) )));
	}
	
	public int GenY(){
		return (int) (y+Math.round(Length*Math.sin(-Math.toRadians(Angle) )));
	}
	
	public void correction(){
		while(Angle>180){
			Angle-=360;
		}
		while(Angle<-180){
			Angle = 360+Angle;
		}
	}
	
	private int correction(int Angle){
		while(Angle>180){
			Angle-=360;
		}
		while(Angle<-180){
			Angle = 360+Angle;
		}
		return Angle;
	}
	
	
	public int AngleDirection(int Aim){
		Angle = Math.round(Angle);
		int Minus = Angle, Plus = Angle;
		
		if(Angle == Aim){
			return 0;
		}else{

			while(Minus != Aim || Plus != Aim){
				Minus--;
				Plus++;
				Minus = correction(Minus);
				Plus = correction(Plus);
			}
			if(Minus == Aim){
				return -1;
			}else{
				return 1;
			}
		}
	}
}
