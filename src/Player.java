import java.awt.Color;


public class Player {
	static int IDTrack = 0;
	int x, y, width = 20, height = 54, Direction = 1, ID;
	double xvel, yvel, Health = 100;
	Color Colour;
	
	boolean UsingGamepad = false; 
			
	boolean TouchingFloor = false; 
	int TouchingWall = 0;
	
	int GamepadID = -1;
	
	boolean CanMove = true;
	Grapple GrapplingHook = new Grapple(0,0);
	int Attack = 0;
	
	
	Animation Walking = new Animation( Animation.Loop, "90 -90 -130 -75 -70 -50 -110 -110 -45 -105 }90 -90 -115 -55 -95 -40 -85 -130 -65 -105 }90 -90 -85 -40 -115 -50 -50 -130 -95 -120 }90 -90 -70 -15 -135 -65 -25 -110 -120 -160 }90 -90 -50 15 -155 -90 -10 -95 -130 175 }90 -90 -65 0 -135 -65 -45 -100 -115 -165 }90 -90 -80 -25 -110 -55 -65 -120 -90 -130 }90 -90 -100 -55 -85 -40 -90 -140 -60 -120 }90 -90 -125 -70 -70 -10 -110 -160 -45 -110 }90 -90 -155 -85 -55 5 -125 -180 -30 -100 }90 -90 -135 -65 -70 -10 -110 -145 -45 -100 }");
	Animation Falling = new Animation( Animation.Loop, "90 -90 -130 -75 -70 -40 -120 -130 -40 -125 }");
	Animation Still = new Animation( Animation.Loop, "90 -90 -120 -90 -60 -90 -120 -90 -60 -90 }");
	Animation WallTouch = new Animation( Animation.Loop, "90 -85 -105 -135 -35 -90 -115 -50 -80 -75 }");
	
	final int Punching = 1, Sliding = 2;
	Animation Punch = new Animation( Animation.Charge, "90 -95 -115 25 -60 45 -100 -105 -60 -95 }90 -95 -80 25 -65 50 -100 -105 -60 -95 }90 -95 -30 25 -70 50 -100 -105 -60 -95 }90 -95 -5 5 -70 50 -100 -105 -60 -95 }");
	Animation Slide = new Animation( Animation.Loop, "90 -70 -130 -75 -50 -40 -85 -120 -60 -65 }");
	double Charge = 0;
	
	
	public Player(int X, int Y, int up, int down, int left, int right, int primary, int secondary){
		setDefault(X, Y);
		setKeys(up, down, left, right, primary, secondary);
	}
	
	public Player(int X, int Y){
		setDefault(X, Y);
	}

	public Player(int X, int Y, int GID){
		setDefault(X, Y);
		GamepadID = GID;
		UsingGamepad = true;
		setKeys(5, 6, -3, 3, 9, 10);
	}
	
	
	public static Player[] clone(Player[] In){
		Player[] Temp = new Player[In.length];
		for(int i = 0; i<In.length; i++){
			Temp[i] = new Player(In[i].x, In[i].y);
		}
		
		return Temp;
	}
	
	
	public void setDefault(int X, int Y){
		ID = IDTrack++;
		while(HitBox.BatchCheckArea(Var.HitBoxes, X, Y, width, height)){
			X+=Maths.RandInt(-1, 1);
			Y+=Maths.RandInt(-1, 1);
		}
		Health = 100;
		Colour = new Color(Maths.RandInt(0, 255), Maths.RandInt(0, 255), Maths.RandInt(0, 255));
		x = X;
		y = Y;
		xvel = 0;
		yvel = 0;
		Direction = 0;
		
		setupLimbs();
	}

	
	//Joint/Limb management
	int Temp = 0;
	int Head = Temp++, //Shush; I know it's not a limb
		Torso = Temp++,
		Left_Elbow = Temp++,
		Left_Hand = Temp++,
		Right_Elbow = Temp++,
		Right_Hand = Temp++,
		Left_Thigh = Temp++,
		Left_Knee = Temp++,
		Right_Thigh = Temp++,
		Right_Knee = Temp++;
		;
	Joint[] Limb = new Joint[Temp];
	
	public void setupLimbs(){
		
		for(int i = 0; i<Limb.length; i++){
			Limb[i] = new Joint(0,0);
			Limb[i].Length = 10;
		}
		Limb[Torso].setLocation(x+width/2, y);
		Limb[Torso].Length = 20;
		Limb[Torso].Angle = -90;
		
		Limb[Head].Angle = 90;
		
		//Arms
		Limb[Left_Elbow].Angle = -120;
		
		Limb[Left_Hand].Parent = Left_Elbow;
		Limb[Left_Hand].Angle = -90;

		Limb[Right_Elbow].Angle = -60;
		
		Limb[Right_Hand].Parent = Right_Elbow;
		Limb[Right_Hand].Angle = -90;
		
		//Legs
		Limb[Left_Thigh].Parent = Torso;
		Limb[Left_Thigh].Angle = -120;
		
		Limb[Left_Knee].Parent = Left_Thigh;
		Limb[Left_Knee].Length = 15;
		Limb[Left_Knee].Angle = -90;
		
		Limb[Right_Thigh].Parent = Torso;
		Limb[Right_Thigh].Angle = -60;
		
		Limb[Right_Knee].Parent = Right_Thigh;
		Limb[Right_Knee].Length = 15;
		Limb[Right_Knee].Angle = -90;
	}
	
	//Gets the limb of the given ID
	public Joint getLimb(int n){
		return Limb[n];
	}
	
	
	//Updates the limb's states i.e. Animates each limb
	public void processLimbs(){
		if(Health < 1){
			Var.Particles = Particle.createCloud(Var.Particles, 100, x, y, 10, 10);
			setDefault(0,0);
		}
		
		//Determining attack
		
		if((getState(Primary) && TouchingFloor) || Maths.Modulus(Charge) > 0){
			CanMove = false;
			
			if(getState(Left) || getState(Right) || Maths.Modulus(Charge) > 0){
					Attack = Sliding;
					if(Charge == 0){
						Charge = Direction;
					}
					if(Maths.Modulus(Charge) < 30){
						Charge+=0.05*Direction;
					}
					//Launch player
					if(!getState(Primary)){
						xvel+=Charge;
						CanMove = true;
						Attack = 0;
						Charge = 0;
					}
			}else{
				Attack = Punching;
			}
		}
		
		if(TouchingFloor){
			if((int)Maths.Modulus(xvel) > 0){
				Walking.animate(Limb, Direction);
			}else{
				if(Attack == Punching){
					Punch.animate(Limb, Direction);
					if(Math.round(Punch.Track) == Punch.Frame.length){
						Attack = 0;
						CanMove = true;
						Punch.Track = 0;
					}
				}else if(Attack == Sliding){
					Slide.animate(Limb, Direction);
				}else{
					Still.animate(Limb, Direction);
				}
			}
		}else{
			if(TouchingWall != 0){
				WallTouch.animate(Limb, Direction);
			}else{
				Falling.animate(Limb, Direction);
			}
			
			
		}
		
		
		
		
		for(int i = 0; i<Limb.length; i++){
			Limb[i].correction();
			if(Limb[i].Parent == -1){
				Limb[i].setLocation(x+width/2, y+Limb[Head].Length);
			}else{
				Limb[i].setLocation( Limb[Limb[i].Parent].GenX(), Limb[Limb[i].Parent].GenY());
				Limb[i].setLocation( Limb[Limb[i].Parent].GenX(), Limb[Limb[i].Parent].GenY());
			}
		}
	}
	
	
	
	//KeyBindings
	int Up = 87, Left = 65, Down = 83, Right = 68, Primary = 70, Secondary = 71;
				
	public void setKeys(int up, int down, int left, int right, int primary, int secondary){
		Up = up;
		Left = left;
		Down = down;
		Right = right;
		Primary = primary;
		Secondary = secondary;
	}
	
	public boolean getState(int i){
		if(UsingGamepad){
			if(Gamepad.Pad[GamepadID].Input[(int)Maths.Modulus(i)] == Maths.Sign(i)){
				return true;
			}else{
				return false;
			}
		}else{
			return Var.KeyBoard[i];
		}
	}
	
	
	
	
	public void update(){
		if(getState(Secondary) && !GrapplingHook.InUse){
			GrapplingHook = Grapple.GenerateGrapple(Var.HitBoxes, x, y, Direction*2, -1);
			GrapplingHook.InUse = true;
		}else if(!getState(Secondary) && getState(Primary)){
			GrapplingHook.InUse = false;
		}
		
		if(GrapplingHook.InUse){
			xvel+= GrapplingHook.Interpret(Grapple.XVEL, x, y, Direction)/2;
			yvel+= GrapplingHook.Interpret(Grapple.YVEL, x, y, Direction)/2;
		}
		
		//vel standard unit is 0.1
		
		//Y	
		
		//Playing in air or on the ground?
		if(!HitBox.BatchCheckArea(Var.HitBoxes, x, (int)(y+yvel+0.1), width, height)){
			if(!GrapplingHook.InUse){
				yvel+=0.1;
			}
			TouchingFloor = false;
		}else{
			TouchingFloor = true;
		}
		
		//yvel caps at 20
		if(yvel > 20){
			yvel = 20;
		}
		
		
		//Regular jump
		if(getState(Up) && TouchingFloor){
			yvel = -6;
		}
		
		//Wall jump
		if(getState(Up) && TouchingWall != 0 && yvel > 3){
			yvel = -6;
			xvel = 10*-TouchingWall;
		}
		
		//Ground smash
		if(getState(Down)){
			yvel = 20;
		}
		
		//Hitbox checks
		Check: for(double i = 0; i<Maths.Modulus(yvel); i+=0.1){
			if(HitBox.BatchCheckArea(Var.HitBoxes, x, (int)(y+i*Maths.Sign(yvel)), width, height)){
				yvel = (i-0.1)*Maths.Sign(yvel);
				
				break Check;
			}
		}
		
		if(!HitBox.BatchCheckArea(Var.HitBoxes, x, (int)(y+yvel), width, height)){
			y+=yvel;
		}
		
		
		//X
			
			//Standard decrease amount
			xvel-=0.1*Maths.Sign(xvel);
			if(Math.round(Maths.Modulus(xvel)+0.3) == 0){
				xvel = 0;
			}
		
			//Player input for xvel caps at 5
			if(getState(Left) && xvel >= -5 && Charge == 0){
				Direction = -1;
				xvel-=0.3;
			}else if(getState(Right) && xvel <= 5 && Charge == 0){
				Direction = 1;
				xvel+=0.3;
			}
			
			//Hitbox collision check
			TouchingWall = 0;
			
			Check: for(double i = 0; i<Maths.Modulus(xvel); i+=0.1){
				if(HitBox.BatchCheckArea(Var.HitBoxes, (int)(x+i*Maths.Sign(xvel)), y, width, height)){
					xvel = (i-0.1)*Maths.Sign(xvel);
					TouchingWall = Direction;
					
					break Check;

				//xvelocity transfer from player to player	
				}else{
					for(int n = 0; n<Var.Player.length; n++){
						if(Var.Player[n].ID != ID && Var.Player[n].AreaInside((int)(x+i*Maths.Sign(xvel)), y, width, height) && Maths.Modulus(xvel)>5 ){
							Var.Player[n].xvel+= xvel-(i-0.1)*Maths.Sign(xvel)/4;
							Var.Player[n].Health-=30;
							xvel = (i-0.1)*Maths.Sign(xvel)/4;
							
							break Check;
						}
					}
				}
				
			}
			
			
			
			
			if(!HitBox.BatchCheckArea(Var.HitBoxes, (int)(x+xvel), y, width, height)){
				x+=xvel;
			}
			
		processLimbs();
	}
	
	
	//Checks whether the player is inside of an area
	public boolean AreaInside(int X, int Y, int WIDTH, int HEIGHT){
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
	
	private boolean PointIntersect(int X, int Y){
		if(X >= x && X <= x+width && Y >= y && Y <= y+height){
			return true;
		}
		return false;
	}
	
}
