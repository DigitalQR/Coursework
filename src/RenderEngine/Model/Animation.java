package RenderEngine.Model;

import java.io.File;

import Tools.Maths.Toolkit;
import Tools.Maths.Vector2f;
import Tools.Maths.Vector3f;

public class Animation{

	private int frame = 0;
	private int length = 0;
	private float lastUpdate = 0;
	private int delay = 0;
	private float scale = 1;
	private Vector3f location = new Vector3f(0,0,0); 
	
	private int[] indices;
	private float[] textureCoords;
	private float[][] vertices;
	private float[][] normals;
	
	private Model[] strip;
	
	public Animation(String name, int delay){
		this.delay = delay;
		
		length = new File("Res/Model/" + name + "/").list().length;
		strip = new Model[length];
		
		for(int i = 0; i<length; i++){
			strip[i] = OBJLoader.loadObjModel(name + "/Frame_" + String.format("%06d", i));
		}
		
		indices = strip[0].getIndices();
		textureCoords = strip[0].getTextureCoords();

		vertices = new float[length][strip[0].getVertices().length];
		normals = new float[length][strip[0].getNormals().length];
		
		for(int i = 0; i<length; i++){
			vertices[i] = strip[i].getVertices();
			normals[i] = strip[i].getNormals();
		}
	}
	
	public Animation(float[][] vertices, float[] textureCoords, int[] indices, float[][] normals){
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.indices = indices;
		this.normals = normals;
	}
	
	public void scaleBy(float scale){
		this.scale = scale;
	}
	
	public void setLocation(Vector3f loc){
		this.location = loc;
	}
	
	public Vector3f getLocation(){
		return location;
	}
	
	public void translate(Vector3f loc){
		this.location.x += loc.x;
		this.location.y += loc.y;
		this.location.z += loc.z;
	}
	
	public void setDelay(int i){
		this.delay = i;
	}
	
	public int getDelay(){
		return delay;
	}
	
	public void reset(){
		frame = 0;
	}
	
	private int lastFrame(){
		if(frame == 0){
			return length-1;
		}else{
			return frame-1;
		}
	}
	
	public void incrementFrame(int i){
		frame+=i; 
		while(frame >= length){
			frame-=length; 
		}
		while(frame < 0){
			frame = length+frame; 
		}
	}
	
	public Model getCurrentFrame(){
		float currentTime = System.nanoTime();
		
		if(currentTime - lastUpdate >= delay*1000000){
			lastUpdate = currentTime;
			incrementFrame(1);
		}
		float nextUpdate = lastUpdate + delay*1000000;
		int lastFrame = lastFrame();
		
		float[] vertex = new float[vertices[frame].length];
		float[] normal = new float[vertices[frame].length];
		
		for(int i = 0; i<vertices[frame].length ; i++){
			Vector2f v1 = new Vector2f(lastUpdate, vertices[lastFrame][i]);
			Vector2f v2 = new Vector2f(nextUpdate, vertices[frame][i]);
			vertex[i] = Toolkit.LERP(v1, v2, currentTime);
			
			Vector2f n1 = new Vector2f(lastUpdate, normals[lastFrame][i]);
			Vector2f n2 = new Vector2f(nextUpdate, normals[frame][i]);
			normal[i] = Toolkit.LERP(n1, n2, currentTime);
		}
		
		Model m = new Model(vertex, textureCoords.clone(), indices.clone(), normal);
		m.scaleBy(scale);
		m.setLocation(location);
		
		return m;
	}
}
