package Archive;
import java.awt.Color;


public class Particle{
	static int IDTrack = 0;
	
	int ID;
	int Life;
	int x, y, width, height;
	Color Colour;
	double xvel = 0, yvel = 0;
	boolean SizeVaries = true, Falls = false;
	
	
	public Particle(int X, int Y){
		ID = IDTrack++;
		setLocation(X,Y);
		setSize(1, 1);
		Life = 100;
		xvel = 4*Maths.RandInt(-1, 1);
		yvel = 4*Maths.RandInt(-1, 1);
	}
	
	public Particle(int X, int Y, int Width, int Height){
		ID = IDTrack++;
		setLocation(X,Y);
		setSize(Width, Height);
		Life = 100;
		xvel = 4*Maths.RandInt(-1, 1);
		yvel = 4*Maths.RandInt(-1, 1);
	}
	
	public void setLocation(int X, int Y){
		x = X;
		y = Y;
	}
	
	public void setSize(int Width, int Height){
		width = Width;
		height = Height;
	}
	
	
	public void update(){
		if(SizeVaries){
			int wChange = Maths.RandInt(-1, 1);
			int hChange = Maths.RandInt(-1, 1);
			if(width+wChange*2 > 1 && height+hChange*2 > 1){
				x-=wChange;
				y-=hChange;

				width+=wChange*2;
				height+=hChange*2;
			}
		}
		
		if(!Falls){
			xvel-=Maths.Sign(xvel)*0.1;
			yvel-=Maths.Sign(xvel)*0.1;
		}
		
		x+=xvel;
		y+=yvel;
		Life--;
	}
	
	
	public static Particle[] remove(Particle[] p, int ID){
		Particle[] Temp = p;
		p = new Particle[p.length-1];
		
		int Track = 0;
		for(int i = 0; i<Temp.length; i++){
			if(Temp[i].ID != ID){
				p[Track] = Temp[i];
				Track++;
			}
		}
		
		return p;
	}
	
	
	public static Particle[] add(Particle[] p, Particle n){
		Particle[] Temp = p;
		p = new Particle[p.length+1];
		
		for(int i = 0; i<Temp.length; i++){
				p[i] = Temp[i];
		}
		
		p[p.length-1] = n;
		
		DebugMenu.Log("[INFO]Particle " + n.ID + " added; " + p.length + ".");
		return p;
	}
	
	public static Particle[] createCloud(Particle[] Part, int n, int x, int y, int width, int height){
		for(int i = 0; i<n; i++){
			Particle Cloud = new Particle(x+Maths.RandInt(1, width),y+Maths.RandInt(1, height));
			Cloud.Colour = new Color(255, 255, 255);
			Part = Particle.add(Part, Cloud);
		}
		return Part;
	}
	
}
