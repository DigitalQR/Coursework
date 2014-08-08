
public class Animation{

	static final int Loop = 0, Charge = 1;
	
	
	double Track = 0;
	double Speed = 0.1;
	int Type;
	
	//Format Animation Frame} Frame} e.t.c
		//Format Frame Angle Angle
		public Animation(int type, String in){
			Type = type;
			String[] States = Maths.getParameters(in, '}');
			for(int i = 0; i<States.length; i++){
				addFrame(States[i]);
			}
		}
	
	int[][] Frame = new int[0][10];
		
	//Adds an animation Frame
	public void addFrame(String In){
		String[] Para = Maths.getParameters(In, ' ');
		
		int[][] Temp = Frame.clone();
		Frame = new int[Temp.length+1][10];
		
		for(int i = 0; i<Temp.length; i++){
			Frame[i] = Temp[i];
		}
		for(int i = 0; i<10; i++){
			Frame[Temp.length][i] = Integer.valueOf(Para[i]);
		}
	}
	
	
	public Joint[] animate(Joint[] Limb, int Direction){
		Track+=Speed;
		if(Track >= Frame.length){
			Track = 0;
		}
		int[][] frame = Frame.clone();
		for(int i = 0; i<Limb.length; i++){
			if(Direction == -1){
				Limb[i].Angle = 180*Maths.Sign(frame[(int)Track][i])-frame[(int)Track][i];
			}else{
				Limb[i].Angle = frame[(int)Track][i];
			}
		}
		
		return Limb;
	}
	
}
