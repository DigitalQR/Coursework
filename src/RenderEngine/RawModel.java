package RenderEngine;

public class RawModel {
	
	private int vaoID, vertexCount;
	private float[] rgba = {0f, 0f, 0f, 0f};
	
	public RawModel(int vaoID, int vertexCount){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}
	
	public int getVaoID(){
		return vaoID;
	}
	
	public int getVertextCount(){
		return vertexCount;
	}
	
	public void setRGBA(float r, float g, float b, float a){
		rgba[0] = r;
		rgba[1] = g;
		rgba[2] = b;
		rgba[3] = a;
	}
	
	public float[] getRGBA(){
		return rgba;
	}
}
