package Editor.Model;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import Control.Visual.Stage.Stage;
import RenderEngine.Loader;
import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Toolkit;
import Tools.Maths.Vector3f;

public class ModelStage extends Stage{

	private static Vector3f rotation;
	private static Vector3f location;
	private Texture compareTexture, grassTexture;
	
	public static void setDefaultCamera(){
		rotation = new Vector3f(0,0,0);
		location = new Vector3f(0,-0.5f,-1);
	}
	
	public void prepare(){
		setDefaultCamera();
		compareTexture = Loader.loadTexture("Plane");
		grassTexture = Loader.loadTexture("Grass");
		
	}

	public void update(){
		processInput();
		GL11.glTranslatef(location.x, location.y, location.z);
		GL11.glRotatef(rotation.x, 1, 0, 0);
		GL11.glRotatef(rotation.y, 0, 1, 0);
		
		if(Option.floorAssist){
			drawFloor();
		}
		if(Option.drawNodes || Option.drawVertices){
			for(Node n: Node.node){
				if((n.getType() == Node.TYPE_NODE && Option.drawNodes) || (n.getType() == Node.TYPE_VERTEX && Option.drawVertices)){
					if(n.getType() == Node.TYPE_NODE){
						drawPoint(n.getLocation(), Option.nodeScale, n.getID(), new float[]{0,0,0});
					}else if(n.getParentID() == Option.getCurrentInterestNode() || !Option.drawPartOnly){
						drawPoint(n.getLocation(), Option.nodeScale/2, n.getID(), new float[]{0,1,1});
					}
					if(n.getParentID() != -1){
						Vector3f a = n.getLocation();
						Vector3f b = Node.getNode(n.getParentID()).getLocation();
						drawLine(a,b);
					}
				}
			}
		}
		
		
		if(Option.drawCross){
			drawCross();
		}
		for(Triangle t: Triangle.triangle){
			t.draw();
		}
		if(Option.KappaAssist){
			drawCompare();
		}
	}
	
	private long lastInput = System.nanoTime();
	
	private void processInput(){
		
		if(System.nanoTime() - lastInput >= (1000/80)*1000000){
			lastInput = System.nanoTime();
			float move = 0.01f;
			float rotate = 0.5f;
			float amp = 50f;
			Node n = new Node(new Vector3f(0,0,0), "");
			try{
				n = Node.getNode(Integer.valueOf(Option.current[Option.FIELD_ID].getText()));
				if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
					Option.setCurrentNode(n.getID());
				}
			}catch(NullPointerException | NumberFormatException e){	}		
			
			if(Keyboard.isKeyDown(Keyboard.KEY_W)){
				
				if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
					rotation.x+=rotate;
				}else if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
					if(n.getParentID() == -1){
						n.setLocation(new Vector3f(n.getLocation().x, n.getLocation().y, n.getLocation().z-move));
					}else{
						n.setTheta(n.getTheta()-move*amp);
					}
				}else{
					Vector3f loc = Toolkit.toCartesian(new Vector3f(move, rotation.z, rotation.x));
					location.x+=loc.x;
					location.y+=loc.y;
					location.z+=loc.z;
					
				}
			}if(Keyboard.isKeyDown(Keyboard.KEY_S)){
				
				if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
					rotation.x-=rotate;
				}else if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
					if(n.getParentID() == -1){
						n.setLocation(new Vector3f(n.getLocation().x, n.getLocation().y, n.getLocation().z+move));
					}else{
						n.setTheta(n.getTheta()+move*amp);
					}
				}else{
					Vector3f loc = Toolkit.toCartesian(new Vector3f(-move, rotation.z, rotation.x));
					location.x+=loc.x;
					location.y+=loc.y;
					location.z+=loc.z;
				}
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_A)){
				
				if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
					rotation.y+=rotate;
				}else if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
					if(n.getParentID() == -1){
						n.setLocation(new Vector3f(n.getLocation().x-move, n.getLocation().y, n.getLocation().z));
					}else{
						n.setPsi(n.getPsi()-move*amp);
					}
				}else{
					location.x+=move;	
				}
			}if(Keyboard.isKeyDown(Keyboard.KEY_D)){
				
				if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
					rotation.y-=rotate;
				}else if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
					if(n.getParentID() == -1){
						n.setLocation(new Vector3f(n.getLocation().x+move, n.getLocation().y, n.getLocation().z));
					}else{
						n.setPsi(n.getPsi()+move*amp);
					}
				}else{
					location.x-=move;
				}
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_Z)){
				
				if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
					if(n.getParentID() == -1){
						n.setLocation(new Vector3f(n.getLocation().x, n.getLocation().y-move, n.getLocation().z));
					}else{
						n.setR(n.getR()-move);
					}
				}else{
					location.y+=move;
				}
			}if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
				
				if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
					if(n.getParentID() == -1){
						n.setLocation(new Vector3f(n.getLocation().x, n.getLocation().y+move, n.getLocation().z));
					}else{
						n.setR(n.getR()+move);
					}
				}else{
					location.y-=move;
				}
			}
		}
	}
	
	private void drawLine(Vector3f a, Vector3f b){
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
		float[] vert = {
				a.x, a.y, a.z,
				b.x, b.y, b.z
		};
		float[] tex = {
				0,0,0,0,0,0
		};
		int[] ind = {
				0,1,0
		};
		
		Model m = new Model(vert, tex, ind);
		m.setRGBA(1, 1, 1, 1);
		Renderer.render(m);
		
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}
	
	private void drawFloor(){
		Cubef floor = new Cubef(new Vector3f(-10, -0.1f, -10), new Vector3f(10, 0, 10));
		Model m = new Model(floor.getVertices(), floor.getTextureCoords(), floor.getIndices());
		m.setTexture(grassTexture);
		
		Renderer.render(m);
	}
	
	
	private void drawCompare(){
		Cubef temp1 = new Cubef(new Vector3f(0,0,0), new Vector3f(0.2f, 0.6f, 0.2f));
		Model m = new Model(temp1);
		m.setTexture(compareTexture);
		m.setRGBA(1, 1, 1, 0.6f);
		
		Renderer.render(m);
	}
	
	private void drawCross(){
		Cubef xc = new Cubef(new Vector3f(0,0,0), new Vector3f(1f,0.02f,0.02f)); 
		Cubef yc = new Cubef(new Vector3f(0,0,0), new Vector3f(0.02f,1f,0.02f)); 
		Cubef zc = new Cubef(new Vector3f(0,0,0), new Vector3f(0.02f,0.02f,1f)); 

		Model mx = new Model(xc.getVertices(), xc.getTextureCoords(), xc.getIndices());
		mx.setRGBA(1, 0, 0, 0.5f);
		Model my = new Model(yc.getVertices(), yc.getTextureCoords(), yc.getIndices());
		my.setRGBA(0, 1, 0, 0.5f);
		Model mz = new Model(zc.getVertices(), zc.getTextureCoords(), zc.getIndices());
		mz.setRGBA(0, 0, 1, 0.5f);

		Renderer.render(mx);
		Renderer.render(my);
		Renderer.render(mz);
	}
	
	public void drawPoint(Vector3f v, float size, int ID, float[] colour){
		int[] index = Option.getSelectedIndices();
		
		Cubef xc = new Cubef(new Vector3f(v.x-0.1f*size,v.y-0.01f*size,v.z-0.01f*size), new Vector3f(v.x+0.1f*size,v.y+0.01f*size,v.z+0.01f*size)); 
		Cubef yc = new Cubef(new Vector3f(v.x-0.01f*size,v.y-0.1f*size,v.z-0.01f*size), new Vector3f(v.x+0.01f*size,v.y+0.1f*size,v.z+0.01f*size)); 
		Cubef zc = new Cubef(new Vector3f(v.x-0.01f*size,v.y-0.01f*size,v.z-0.1f*size), new Vector3f(v.x+0.01f*size,v.y+0.01f*size,v.z+0.1f*size)); 
		
		Model mx = new Model(xc.getVertices(), xc.getTextureCoords(), xc.getIndices());
		mx.setRGBA(colour[0], colour[1], colour[2], 1);
		Model my = new Model(yc.getVertices(), yc.getTextureCoords(), yc.getIndices());
		my.setRGBA(colour[0], colour[1], colour[2], 1);
		Model mz = new Model(zc.getVertices(), zc.getTextureCoords(), zc.getIndices());
		mz.setRGBA(colour[0], colour[1], colour[2], 1);

		if(Option.current[Option.FIELD_ID].getText().equals("" +ID)){
			mx.setRGBA(1, 1, 1, 1);
			my.setRGBA(1, 1, 1, 1);
			mz.setRGBA(1, 1, 1, 1);

			drawLine(v, new Vector3f(v.x, v.y, 0));
			drawLine(v, new Vector3f(v.x, 0, v.z));
			drawLine(v, new Vector3f(0, v.y, v.z));
		}else if(index[0] == ID || index[1] == ID || index[2] == ID){
			mx.setRGBA(0, 1, 0, 1);
			my.setRGBA(0, 1, 0, 1);
			mz.setRGBA(0, 1, 0, 1);
		}
		
		
		Renderer.render(mx);
		Renderer.render(my);
		Renderer.render(mz);
	}
	
}
