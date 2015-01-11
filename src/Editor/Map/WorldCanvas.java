package Editor.Map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import Level.RandomWorld;
import Level.World;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;
import Collision.Hitbox;
import Collision.SquareHitbox;
import Control.Settings;

public class WorldCanvas extends JPanel{
	
	private static final long serialVersionUID = -1718433555125590002L;
	public static final int FLOAT_FACTOR = 1000000;
	public int GRID_SPACING = 30;
	public boolean gridSnapping = false;
	
	private final int X = 380, Y = 10, WIDTH = 700, HEIGHT = 450;
	private SquareHitbox canvas = new SquareHitbox(new Vector2f(X, Y), new Vector2f(WIDTH, HEIGHT));
	
	private int x = -100, y = -100;
	
	private int currentID = -786;
	
	public int getCurrentID() {
		return currentID;
	}

	public void setCurrentID(int currentID) {
		this.currentID = currentID;
		HitboxToolPane.change = true;
	}

	
	World world = new RandomWorld();
	private MapToolPane toolPane = new MapToolPane(this);
	private HitboxToolPane toolPane1 = new HitboxToolPane(this);
	
	private float[] BrushColour = {1,1,1,1};
	
	public float[] getBrushColour() {
		return BrushColour;
	}

	public void setBrushColour(float[] brushColour) {
		BrushColour = brushColour;
	}

	public Point getLocationOnScreen(){
		return super.getLocationOnScreen();
	}
	
	public WorldCanvas(){
		this.setLayout(null);
		this.setOpaque(false);
		this.setBackground(new Color(0f,0f,0f,0f));
		this.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0){
			}

			public void mouseEntered(MouseEvent arg0){
			}

			public void mouseExited(MouseEvent arg0){
			}

			public void mousePressed(MouseEvent m){
				if(m.isControlDown()){
					mousePress(m);
				}else{
					
					int i = 0;
					int xi = m.getXOnScreen() - getLocationOnScreen().x;
					int yi = m.getYOnScreen() - getLocationOnScreen().y;

					final int worldWidth = Math.round(Settings.boundary.EndLocation.x-Settings.boundary.StartLocation.x);
					final int worldHeight = Math.round(Settings.boundary.EndLocation.y-Settings.boundary.StartLocation.y);
					
					float locX = Math.round((xi-X-WIDTH/2)*FLOAT_FACTOR)/(WIDTH/worldWidth);
					float locY = Math.round((yi-Y-HEIGHT/2)*FLOAT_FACTOR)/(HEIGHT/worldHeight);
					locX/=FLOAT_FACTOR;
					locY/=FLOAT_FACTOR;
					
					for(Hitbox hb: world.getHitboxList()){
						if(hb.AreaIntersect(new Vector2f(locX, locY), new Vector2f(0,0))){
							currentID = i;
							break;
						}
						i++;
					}
					if(i >= world.getHitboxList().size()){
						currentID = -2;
					}
				}
			}

			public void mouseReleased(MouseEvent arg0){
			}
			
		});
	}
	
	private void mousePress(MouseEvent m){
		int x = m.getXOnScreen() - this.getLocationOnScreen().x;
		int y = m.getYOnScreen() - this.getLocationOnScreen().y;

		x = convert(x, X);
		y = convert(y, Y);
		
		if(canvas.AreaIntersect(new Vector2f(x,y), new Vector2f(1,1))){
			if(currentID != -1){
				this.x = x;
				this.y = y;
				currentID = -1;
			}else if(currentID == -1){
				final int worldWidth = Math.round(Settings.boundary.EndLocation.x-Settings.boundary.StartLocation.x);
				final int worldHeight = Math.round(Settings.boundary.EndLocation.y-Settings.boundary.StartLocation.y);
				
				
				float locX = Math.round((this.x-X-WIDTH/2)*FLOAT_FACTOR)/(WIDTH/worldWidth);
				float locY = Math.round((this.y-Y-HEIGHT/2)*FLOAT_FACTOR)/(HEIGHT/worldHeight);
				locX/=FLOAT_FACTOR;
				locY/=FLOAT_FACTOR;
				
				int xi = convert(x, X);
				int yi = convert(y, Y);

				float locXi = Math.round((xi-X-WIDTH/2)*FLOAT_FACTOR)/(WIDTH/worldWidth);
				float locYi = Math.round((yi-Y-HEIGHT/2)*FLOAT_FACTOR)/(HEIGHT/worldHeight);
				locXi/=FLOAT_FACTOR;
				locYi/=FLOAT_FACTOR;
				
				if(locXi < locX){
					float temp = locXi;
					locXi = locX;
					locX = temp;
				}
				if(locYi < locY){
					float temp = locYi;
					locYi = locY;
					locY = temp;
				}
				world.addHitbox(new SquareHitbox(new Vector2f(locX, locY), new Vector2f(locXi-locX, locYi-locY)));
				
				currentID = -2;
			}
		}
	}
	
	private int convert(int v, int O){
		if(gridSnapping){
			v-=O;
			v = Math.round(v/GRID_SPACING);
			v*= GRID_SPACING;
			v+=O;
		}
		return v;
	}
	
	public void paintComponent(Graphics g){
		if(gridSnapping){
			drawAxis(g);
		}
		float[] RGB = getRandomLighting();
		drawWorld(g, X, Y, WIDTH, HEIGHT, RGB);
		drawWorld(g, X+WIDTH/2, Y+HEIGHT+10, WIDTH/2, HEIGHT/2, RGB);
		
		drawCurrentElement(g);
		if(gridSnapping){
			drawAxis(g);
		}
		
		toolPane.paint(g);
		toolPane1.paint(g);
	}
	
	public void drawCurrentElement(Graphics g){
		if(currentID == -1){
			g.setColor(new Color(BrushColour[0], BrushColour[1], BrushColour[2], BrushColour[3]));
			int width = convert(MouseInfo.getPointerInfo().getLocation().x-this.getLocationOnScreen().x, X)-x;
			int height = convert(MouseInfo.getPointerInfo().getLocation().y-this.getLocationOnScreen().y, Y)-y;

			if(x+width < X){
				width = X-x;
			}
			if(x + width >= X+WIDTH){
				width = X+WIDTH-x;
			}

			if(y+height < Y){
				height = Y-y;
			}
			if(y + height >= Y+HEIGHT){
				height = Y+HEIGHT-y;
			}
			
			g.fillRect(x, y, width, height);
		}
	}
	
	public void drawCanvas(Graphics g, int X, int Y, int WIDTH, int HEIGHT, float[] RGB){
		g.setColor(new Color(RGB[0], RGB[1], RGB[2]));
		g.fillRect(X, Y, WIDTH, HEIGHT);
		
		g.setColor(new Color(0f,0f,0f));
		g.drawRect(X, Y, WIDTH, HEIGHT);

		g.setColor(new Color(RGB[0]/4,RGB[1]/4,RGB[2]/4));
		g.drawRect(X+1, Y+1, WIDTH-2, HEIGHT-2);

		g.setColor(new Color(RGB[0]/3,RGB[1]/3,RGB[2]/3));
		g.drawRect(X+2, Y+2, WIDTH-4, HEIGHT-4);
		
		g.setColor(new Color(RGB[0]/2,RGB[1]/2,RGB[2]/2));
		g.drawRect(X+3, Y+3, WIDTH-6, HEIGHT-6);
	}
	
	private float r = 0.5f, g = 1f, b = 0.5f;
	
	public float[] getRandomLighting(){
		float[] RGBA = new float[3];
		float speed = 1.5f;
		r+=0.01f*speed;
		g+=0.015f*speed;
		b+=0.02f*speed;
		
		RGBA[0] = Toolkit.Modulus((float)Math.sin(r));
		RGBA[1] = Toolkit.Modulus((float)Math.sin(g));
		RGBA[2] = Toolkit.Modulus((float)Math.sin(b));
		
		for(int i = 0; i<3; i++){
			if(RGBA[i] > 0.7f){
				RGBA[i] = 0.7f;
			}
		}
		
		return RGBA;
	}
	
	public void drawWorld(Graphics g, int X, int Y, int WIDTH, int HEIGHT, float[] RGB){
		drawCanvas(g, X, Y, WIDTH, HEIGHT, RGB);
		
		final int worldWidth = Math.round(Settings.boundary.EndLocation.x-Settings.boundary.StartLocation.x);
		final int worldHeight = Math.round(Settings.boundary.EndLocation.y-Settings.boundary.StartLocation.y);
		
		for(Hitbox hb: world.getHitboxList()){
			int x = Math.round(hb.getLocation().x*WIDTH/worldWidth)+X+WIDTH/2;
			int y = Math.round(hb.getLocation().y*HEIGHT/worldHeight)+Y+HEIGHT/2;
			int width = Math.round(hb.getSize().x*WIDTH/worldWidth);
			int height = Math.round(hb.getSize().y*HEIGHT/worldHeight);
			
			g.setColor(new Color(0f,0f,0f));
			g.fillRect(x, y, width, height);
		}
		
		int i = 0;
		for(Hitbox hb: world.getHitboxList()){
			if(i == currentID){
				int x = Math.round(hb.getLocation().x*WIDTH/worldWidth)+X+WIDTH/2;
				int y = Math.round(hb.getLocation().y*HEIGHT/worldHeight)+Y+HEIGHT/2;
				int width = Math.round(hb.getSize().x*WIDTH/worldWidth);
				int height = Math.round(hb.getSize().y*HEIGHT/worldHeight);
				
				g.setColor(new Color(0f,0f,0f));
				g.drawRect(x-1, y-1, width+2, height+2);
				g.drawRect(x+1, y+1, width-2, height-2);
				g.setColor(new Color(1f,1f,1f));
				g.drawRect(x, y, width, height);
			}
			i++;
		}
	}
	
	public void drawAxis(Graphics g){
		g.setColor(new Color(0f, 0f, 0f));
		for(int x = 0; x<WIDTH; x+=GRID_SPACING){
			g.drawLine(X+x, Y, X+x, Y+HEIGHT);
		}
		for(int y = 0; y<HEIGHT; y+=GRID_SPACING){
			g.drawLine(X, Y+y, X+WIDTH, Y+y);
		}
	}
}
