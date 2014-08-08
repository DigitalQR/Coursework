import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;


public class GraphicContent extends JPanel implements Runnable{
	private static final long serialVersionUID = 1L;
	static int CurrentScreen = 2;
	static double Sleep = 1;
	static final int OverWorld = 1, Menu = 2, KeyAssignment = 3 ;
	static Thread Graphics;
	
	//Setups the JPanel
	public GraphicContent(){
		this.setOpaque(true);
		Graphics = new Thread(this);
		Graphics.start();
		DebugMenu.Log("[INFO]GraphicsContent thread started.");
	}
	
	
	public void paintComponent(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		//Decide what should be drawn based on current focus(CurrentScreen)
		switch(CurrentScreen){
			case OverWorld:
				OverWorldGraphics.paint(g);
				break;
			case Menu:
				MenuGraphics.paint(g);
				break;
			case KeyAssignment:
				KeyGraphics.paint(g);
			break;
		}
	}
	
	
	int FPSCap = 120;
	
	public void run(){
		long BeforeTime, AfterTime;
		BeforeTime = System.currentTimeMillis();
		while(true){
			//System.out.println(Maths.getTime());1
			repaint();
			AfterTime = System.currentTimeMillis();
			Sleep = AfterTime-BeforeTime;
			
			if(Sleep >= FPSCap){
				Sleep = FPSCap-1;
			}
			Sleep = FPSCap-Sleep;
			try{
				TimeUnit.MILLISECONDS.sleep((long) (1000/Sleep));
			}catch(InterruptedException e){
				System.err.println(Maths.getTime() + "[ERROR]GraphicsContent/run(): " + e.getMessage());
			}
			BeforeTime = System.currentTimeMillis();
			
			Track.FPS++;
		}
	}

}

//Graphics for the overworld (Playable area)3
class OverWorldGraphics{
	static boolean Loaded = false;
	static int LoadCount = 10;
	
	//Main routine for control over painting
	public static Graphics paint(Graphics g){
			if(Loaded){//Should a loading screen be drawn.
			
			CalculateCamera();
				
			DrawHitBoxes(g);
			DrawPlayers(g);
			DrawParticles(g);
			
			DrawPlayerInfo(g);
			
		}else{
			g.setFont(new Font("Ariel", 50, 50));
			g.setColor(Color.BLACK);
			
			g.drawString("Loading..", 50, 50);
			LoadCount--;
			if(LoadCount == 0){
			Loaded = true;
			}
			
		}
		return g;
	}
	
	
	//Calculates offset and scale 
	
	/*
	 * TODO
	 * Use:
	 * http://en.wikipedia.org/wiki/Lerp_%28computing%29
	 * for the camera
	 * */
	
	
	public static void CalculateCamera(){
		//Determine Offset
		int TempXOffset = 0;
		int TempYOffset = 0;
		
		try{
			Var.XOffset = 0;
			Var.YOffset = 0;
			
			for(int i = 0; i<Var.Player.length; i++){
				Var.XOffset+= (int)((MainClass.Window.getWidth()/2-Var.Player[i].x*Var.ActualScale - Var.Player[i].width/2));
				Var.YOffset+= (int)((MainClass.Window.getHeight()/2-Var.Player[i].y*Var.ActualScale - Var.Player[i].height/2));
			
				TempXOffset+= (int)((MainClass.Window.getWidth()/2-Var.Player[i].x*Var.ActualScale - Var.Player[i].width/2));
				TempYOffset+= (int)((MainClass.Window.getHeight()/2-Var.Player[i].y*Var.ActualScale - Var.Player[i].height/2));
			}
			
			//Mean average of co-ordinates
			Var.XOffset/= Var.Player.length;
			Var.YOffset/= Var.Player.length;
			
			TempXOffset/= Var.Player.length;
			TempYOffset/= Var.Player.length;
			
		}catch(Exception e){
			DebugMenu.Log("[ERROR]GraphicContent/paintComponent //Determine Offset: " + e.getMessage());
		}
		
	//Determine Scale
		try{
			Var.ScaleAim = 1.5;			
			
			Point[] PlayerCord = new Point[Var.Player.length];
			
			for(int i = 0; i<PlayerCord.length; i++){
				PlayerCord[i] = new Point(Var.Player[i].x, Var.Player[i].y);
			}
			
			for(int n = 0; n<PlayerCord.length; n++){
				for(int i = 1; i<PlayerCord.length; i++){
					if(PlayerCord[i].x > PlayerCord[i-1].x){
						int temp = PlayerCord[i-1].x;
						PlayerCord[i-1].x = PlayerCord[i].x;
						PlayerCord[i].x = temp;
					}
					if(PlayerCord[i].y > PlayerCord[i-1].y){
						int temp = PlayerCord[i-1].y;
						PlayerCord[i-1].y = PlayerCord[i].y;
						PlayerCord[i].y = temp;
					}
				}
			}
			
			int x = 250-TempXOffset;
			int y = 250-TempYOffset;
			int width = MainClass.Window.getWidth()-250;
			int height = MainClass.Window.getHeight()-250;
			
			if(!(PlayerCord[0].x >= x && PlayerCord[0].x <= x+width && PlayerCord[0].y >= y && PlayerCord[0].y <= y+height)){
				double Val = 0;
				Val += (PlayerCord[0].x*PlayerCord[0].x)/x;
				Val += (PlayerCord[0].y*PlayerCord[0].y)/y;
				Val /= 2;
				
				Var.ScaleAim = 1/Val;
			}
			
			double Rate = 0.01;
			if( Var.ActualScale < Var.ScaleAim+Var.ActualScale*0.1 && Var.ActualScale > Var.ScaleAim-Var.ActualScale*0.1){	
				Rate = 0.001;
			}
			
			if(Var.ActualScale < Var.ScaleAim){
				Var.ActualScale+=Rate;
			}
			if(Var.ActualScale > Var.ScaleAim){
				Var.ActualScale-=Rate;
			}
			
		}catch(Exception e){
			DebugMenu.Log("[ERROR]GraphicContent/paintComponent //Determine Scale: " + e.getMessage());
		}

	}
	
	//Painting all hitboxes
	public static Graphics DrawHitBoxes(Graphics g){
		
		//Repeat to apply black outline
		Color[] Colour = {Color.BLACK, new Color(204, 51, 51)};
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(8));
		
		for(int q = 0; q<2; q++){
		

			for(int i = 0; i<Var.HitBoxes.length; i++){
				g.setColor(Colour[q]);
				
				if(Var.HitBoxes[i].AreaIntersect((int)Math.round(-Var.XOffset/Var.ActualScale), (int)Math.round(-Var.YOffset/Var.ActualScale), 
												 (int)Math.round(MainClass.Window.getWidth()/Var.ActualScale), (int)Math.round(MainClass.Window.getHeight()/Var.ActualScale))){
					if(q == 0){
						g.drawRect((int)Math.round(Var.HitBoxes[i].x*Var.ActualScale+Var.XOffset), (int)Math.round(Var.HitBoxes[i].y*Var.ActualScale+Var.YOffset), 
								   (int)Math.round(Var.HitBoxes[i].width*Var.ActualScale), (int)Math.round(Var.HitBoxes[i].height*Var.ActualScale));
					}else{
						g.fillRect((int)Math.round(Var.HitBoxes[i].x*Var.ActualScale+Var.XOffset), (int)Math.round(Var.HitBoxes[i].y*Var.ActualScale+Var.YOffset), 
								   (int)Math.round(Var.HitBoxes[i].width*Var.ActualScale), (int)Math.round(Var.HitBoxes[i].height*Var.ActualScale));
					}
				}
			}
		}
		
		return g;
	}
	
	//Draws the player boxes at the bottom of the screen
	public static Graphics DrawPlayerInfo(Graphics g){
		
		double Scale = MainClass.Window.getWidth()/1000;
		int XSpacing = MainClass.Window.getWidth()/Var.Player.length, width = 200, height = 100;
		int y = (int)Math.round(MainClass.Window.getHeight()-height);
		if(!MainClass.Window.Fullscreen){
			y-=20;
			XSpacing-=20;
		}else{
			y+=10;
		}
		
		for(int i = 0; i<Var.Player.length; i++){
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(2+(float)Var.ActualScale));
			
			//Get a white box with an outline
			g.setColor(Color.WHITE);
			g.fillRect((int)Math.round((i*XSpacing+(XSpacing)/4)*Scale), (int)Math.round((y+25)*Scale), (int)Math.round((width)*Scale), (int)Math.round((height)*Scale) );
			g.setColor(Color.GRAY);
			g.drawRect((int)Math.round((i*XSpacing+(XSpacing)/4)*Scale)+2, (int)Math.round((y+25)*Scale)+2, (int)Math.round((width)*Scale)-4, (int)Math.round((height)*Scale)-4 );
			g.setColor(Color.BLACK);
			g.drawRect((int)Math.round((i*XSpacing+(XSpacing)/4)*Scale), (int)Math.round((y+25)*Scale), (int)Math.round((width)*Scale), (int)Math.round((height)*Scale) );
		
			
			g2.setStroke(new BasicStroke(5+(float)Var.ActualScale));
			
			//Give a black outline to the players
			Color[] Colour = {Color.BLACK, Var.Player[i].Colour};
			
			int x = (int)Math.round((i*XSpacing+(XSpacing)/4)*Scale);
			
			//Repeat twice to apply the outline (Cycles though the colours in the array)
			for(int q = 0; q < 2; q++){
				if(q == 1){
					g2.setStroke(new BasicStroke(1+(float)Var.ActualScale));
				}
				g.setColor(Colour[q]);
				
				//Head
				g.drawOval((int)Math.round((x+10)*Scale), (int)Math.round((y+5)*Scale),
						   (int)Math.round((25)*Scale), (int)Math.round((25)*Scale) );
				g.fillOval((int)Math.round((x+10)*Scale), (int)Math.round((y+5)*Scale),
						   (int)Math.round((25)*Scale), (int)Math.round((25)*Scale) );
				
				//Torso
				g.drawLine((int)Math.round((x+10+25/2)*Scale), (int)Math.round((y+30)*Scale),
						   (int)Math.round((x+10+25/2)*Scale), (int)Math.round((y+90)*Scale) );
				
				//Arms
				g.drawLine((int)Math.round((x+10+25/2)*Scale), (int)Math.round((y+30)*Scale),
						   (int)Math.round((x+10+25/2-10)*Scale), (int)Math.round((y+55)*Scale) );
				g.drawLine((int)Math.round((x+10+25/2-10)*Scale), (int)Math.round((y+55)*Scale),
						   (int)Math.round((x+10+25/2-10)*Scale), (int)Math.round((y+80)*Scale) );
				
				g.drawLine((int)Math.round((x+10+25/2)*Scale), (int)Math.round((y+30)*Scale),
						   (int)Math.round((x+10+25/2+10)*Scale), (int)Math.round((y+55)*Scale) );
				g.drawLine((int)Math.round((x+10+25/2+10)*Scale), (int)Math.round((y+55)*Scale),
						   (int)Math.round((x+10+25/2+10)*Scale), (int)Math.round((y+80)*Scale) );
				
				//Legs
				g.drawLine((int)Math.round((x+10+25/2)*Scale), (int)Math.round((y+90)*Scale),
						   (int)Math.round((x+10+25/2-10)*Scale), (int)Math.round((y+120)*Scale) );
				g.drawLine((int)Math.round((x+10+25/2)*Scale), (int)Math.round((y+90)*Scale),
						   (int)Math.round((x+10+25/2+10)*Scale), (int)Math.round((y+120)*Scale) );
				
			}

			g.setColor(Color.BLACK);
			g.setFont(new Font("Ariel", 30, 30));
			g.drawString("Player " + i + " " + Maths.Round(Var.Player[i].Charge, 2), (int) ((i*XSpacing+50+(XSpacing)/4)*Scale), (int)Math.round((y+25+40)*Scale));
			
			g.setColor(Var.Player[i].Colour);
			g.setFont(new Font("Ariel", 30, 30));
			g.drawString("Player " + i + " " + Maths.Round(Var.Player[i].Charge, 2), (int) ((i*XSpacing+52+(XSpacing)/4)*Scale), (int)Math.round((y+25+42)*Scale));
		
			
			g.setColor(Color.BLACK);
			g.fillRect((int) ((i*XSpacing+50+(XSpacing)/4)*Scale), (int)Math.round((y+25+40)*Scale), (int)Math.round((Var.Player[i].Health)*Scale), (int)Math.round(10*Scale));
			
			g.setColor(new Color(51, 204, 51));
			g.fillRect((int) ((i*XSpacing+52+(XSpacing)/4)*Scale), (int)Math.round((y+25+42)*Scale), (int)Math.round((Var.Player[i].Health)*Scale), (int)Math.round(10*Scale));
			
		}
		
		return g;
	}
	
	
	//Draw everything relevent to the player
	public static Graphics DrawPlayers(Graphics g){
		
		//Draw all of the players
		for(int i = 0; i<Var.Player.length; i++){
			//Draws the player's hitboxes
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(1));
			g.setColor(Color.RED);
			g.drawRect((int)Math.round(Var.Player[i].x*Var.ActualScale+Var.XOffset), (int)Math.round(Var.Player[i].y*Var.ActualScale+Var.YOffset), 
					   (int)Math.round(Var.Player[i].width*Var.ActualScale), (int)Math.round(Var.Player[i].height*Var.ActualScale));
			
			
			
			//Set the thickness of the line, so it can be seen at a distance
			g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(5+(float)Var.ActualScale));
			
			//Give a black outline to the players
			//Increases the players colour based on charge
			int R = Var.Player[i].Colour.getRed()+(int)Maths.Modulus(Var.Player[i].Charge), G = Var.Player[i].Colour.getGreen()+(int)Maths.Modulus(Var.Player[i].Charge), B = Var.Player[i].Colour.getBlue()+(int)Maths.Modulus(Var.Player[i].Charge);
			
			if(R > 255){
				R = 255;
			}if(G > 255){
				G = 255;
			}if(B > 255){
				B = 255;
			}
			
			Color[] Colour = {Color.BLACK, new Color(R, G, B)};
			
			//Repeat twice to apply the outline (Cycles though the colours in the array)
			for(int q = 0; q < 2; q++){
				if(q == 1){
					g2.setStroke(new BasicStroke(1+(float)Var.ActualScale));
				}
				g.setColor(Colour[q]);
					
					for(int n = 0; n<Var.Player[i].Limb.length; n++){
						
						Joint Temp = Var.Player[i].getLimb(n);
						if(n == Var.Player[i].Head){
							g.drawOval((int)Math.round((Temp.x-Temp.Length/2)*Var.ActualScale+Var.XOffset), (int)Math.round((Temp.y-Temp.Length)*Var.ActualScale+Var.YOffset),
									   (int)Math.round(Temp.Length*Var.ActualScale), (int)Math.round(Temp.Length*Var.ActualScale));
						
							g.fillOval((int)Math.round((Temp.x-Temp.Length/2)*Var.ActualScale+Var.XOffset), (int)Math.round((Temp.y-Temp.Length)*Var.ActualScale+Var.YOffset),
									   (int)Math.round(Temp.Length*Var.ActualScale), (int)Math.round(Temp.Length*Var.ActualScale));
						
						}else{
							g.drawLine((int)Math.round(Temp.x*Var.ActualScale+Var.XOffset), (int)Math.round(Temp.y*Var.ActualScale+Var.YOffset),
									   (int)Math.round(Temp.GenX()*Var.ActualScale+Var.XOffset), (int)Math.round(Temp.GenY()*Var.ActualScale+Var.YOffset));
						}
						
						
					}
					
					//DrawGrapple point
					g.drawOval((int)Math.round((Var.Player[i].GrapplingHook.x)*Var.ActualScale+Var.XOffset), (int)Math.round((Var.Player[i].GrapplingHook.y)*Var.ActualScale+Var.YOffset),
							   (int)Math.round(10*Var.ActualScale), (int)Math.round(10*Var.ActualScale));
				
					g.fillOval((int)Math.round((Var.Player[i].GrapplingHook.x)*Var.ActualScale+Var.XOffset), (int)Math.round((Var.Player[i].GrapplingHook.y)*Var.ActualScale+Var.YOffset),
							   (int)Math.round(10*Var.ActualScale), (int)Math.round(10*Var.ActualScale));
					
					 if(Var.Player[i].GrapplingHook.InUse){
					g.drawLine((int)Math.round((Var.Player[i].x+Var.Player[i].width/2)*Var.ActualScale+Var.XOffset), (int)Math.round((Var.Player[i].y+Var.Player[i].height/2)*Var.ActualScale+Var.YOffset), 
							   (int)Math.round((Var.Player[i].GrapplingHook.x+5)*Var.ActualScale+Var.XOffset), (int)Math.round((Var.Player[i].GrapplingHook.y+5)*Var.ActualScale+Var.YOffset) );
					}
				}
				
			
		}
		return g;
	}
	
	
	//Draws everything related to particles
	public static Graphics DrawParticles(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(5+(float)Var.ActualScale));
		
		//Draws twice to add black boarder
		for(int q = 0; q < 2; q++){

			for(int i = 0; i<Var.Particles.length; i++){
				try{
					if(q == 1){
						g.setColor(Var.Particles[i].Colour);	
						g2.setStroke(new BasicStroke(1));
					}else{		
						g.setColor(Color.BLACK);
					}	
					
					g.drawRect((int)Math.round(Var.Particles[i].x*Var.ActualScale+Var.XOffset), (int)Math.round(Var.Particles[i].y*Var.ActualScale+Var.YOffset), 
							   (int)Math.round(Var.Particles[i].width*Var.ActualScale), (int)Math.round(Var.Particles[i].height*Var.ActualScale));
					g.fillRect((int)Math.round(Var.Particles[i].x*Var.ActualScale+Var.XOffset), (int)Math.round(Var.Particles[i].y*Var.ActualScale+Var.YOffset), 
							   (int)Math.round(Var.Particles[i].width*Var.ActualScale), (int)Math.round(Var.Particles[i].height*Var.ActualScale));
				}catch(Exception e){
					DebugMenu.Log("[ERROR] GraphicContent/OverworldGraphics/DrawParticles(): " + e.getMessage());
				}
			}
		
		}
		return g;
	}
	
}

class MenuGraphics{
	
	static int CurrentButton = 0;
	static Button[] Buttons = {new Button("Start.", 25, 25, 200, 50), new Button("Test.", 25, 80, 200, 50), new Button("Key assignment.", 25, 135, 200, 50)};
	
	public static Graphics paint(Graphics g){
		
		drawButtons(g);
		
		return g;
	}
	
	public static Graphics drawButtons(Graphics g){
		ButtonProcess();
		
		for(int i = 0; i<Buttons.length; i++){
			if(i == CurrentButton){
				g.setColor(Color.YELLOW);
			}else{
				g.setColor(Color.WHITE);
			}
			g.fillRect(Buttons[i].x, Buttons[i].y, Buttons[i].width, Buttons[i].height);

			g.setColor(Color.BLACK);
			g.drawRect(Buttons[i].x, Buttons[i].y, Buttons[i].width, Buttons[i].height);

			g.setColor(Color.GRAY);
			g.drawRect(Buttons[i].x+1, Buttons[i].y+1, Buttons[i].width-2, Buttons[i].height-2);
			
			g.setColor(Color.BLACK);
			g.drawString(Buttons[i].Text, Buttons[i].x+10, Buttons[i].y+Buttons[i].height/2 );
			
		}
		
		return g;
	}
	
	private static void ButtonProcess(){
		if(Var.KeyBoard[87]){
			Var.KeyBoard[87] = false;
			CurrentButton--;
			if(CurrentButton < 0){
				CurrentButton = Buttons.length-1;
			}
		}
		if(Var.KeyBoard[83]){
			Var.KeyBoard[83] = false;
			CurrentButton++;
			if(CurrentButton > Buttons.length-1){
				CurrentButton = 0;
			}
		}
		
		if(Var.KeyBoard[10]){
			Issue(CurrentButton);
		}
	}
	
	//Issues the commands assigned to each button
	private static void Issue(int i){
		switch(i){
		case 0:
				GraphicContent.CurrentScreen = GraphicContent.OverWorld;
			break;
		case 2:
				KeyGraphics.Player = 0;
				KeyGraphics.Key = 0;
				GraphicContent.CurrentScreen = GraphicContent.KeyAssignment;
		break;
		}
	}
}

class Button{
	String Text;
	int x, y, width, height;
	
	public Button(String In, int X, int Y, int Width, int Height){
		setText(In);
		setLocation(X,Y);
		setSize(Width, Height);
	}
	
	public void setText(String In){
		Text = In;
	}
	
	public void setLocation(int X, int Y){
		x = X;
		y = Y;
	}
	
	public void setSize(int Width, int Height){
		width = Width;
		height = Height;
	}
}



class KeyGraphics{
	
	static int cooldown = 100;
	static int Player = 0;
	static boolean Controller = false;
	static int Key = 0;
	static int[] Bindings = new int[6];
	static String[] List = {"Up", "Down", "Left", "Right", "Primary", "Secondary"};
	
	public static Graphics paint(Graphics g){
		g.setColor(Color.BLACK);
		g.drawString("Player " + Player, 25, 25);
		if(cooldown >= 0 ){
			g.setColor(Color.RED);
		}else{
			g.setColor(Color.GREEN);
		}
		g.drawString(List[Key], 25, 40);
		
		
		cooldown--;
		if(cooldown < 0){
			int[] Input = getInput();
			if(Input[0] != 0){
				if(Key != 0 && Input[0] == 2){
					Controller = true;
				}
				
				Bindings[Key] = Input[1];
				cooldown = 100;
				Key++;
				if(Key>5){
					Key = 0;
					Var.Player[Player].Up = Bindings[0];
					Var.Player[Player].Down = Bindings[1];
					Var.Player[Player].Left = Bindings[2];
					Var.Player[Player].Right = Bindings[3];
					Var.Player[Player].Primary = Bindings[4];
					Var.Player[Player].Secondary = Bindings[5];
					Player++;
				}if(Player >= Var.Player.length){
					GraphicContent.CurrentScreen = GraphicContent.Menu;
				}
			}
		}
		
		return g;
	}
	
	//First index is type, second is identifier: 0 - null, 1 - Keyboard, 2 - Gamepad
	public static int[] getInput(){
		int[] Temp = {0,0};
		if(Var.Player[Player].GamepadID != -1){
			for(int n = 0; n<Gamepad.Pad[Var.Player[Player].GamepadID].Input.length; n++){
				if(Gamepad.Pad[Var.Player[Player].GamepadID].Input[n] != 0){
					Temp[0] = 2;
					Temp[1] = n*Maths.Sign(Gamepad.Pad[Var.Player[Player].GamepadID].Input[n]);
				}
			}
		}else{
			for(int i = 0; i<Var.KeyBoard.length; i++){
				if(Var.KeyBoard[i]){
					Temp[0] = 1;
					Temp[1] = i;
					return Temp;
				}
			}
		}
		return Temp;
	}
	
	
}