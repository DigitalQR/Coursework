package RenderEngine.Model;

import org.newdawn.slick.opengl.Texture;

import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;

public class Model {
	
	private float[] vertices, texCoords, normal;
	private float scale = 1f;
	private int[] indices;
	private Texture texture;
	private Vector3f location = new Vector3f(0,0,0);
	private float red = 1, green = 1, blue = 1, alpha = 1;
	
	public Model(float[] vertices, float[] textureCoords, int[] indices, float[] normal){
		this.vertices = vertices;
		this.texCoords = textureCoords;
		this.indices = indices;
		this.normal = normal;
	}

	public Model(Cubef cube){
		this.vertices = cube.getVertices();
		this.texCoords = cube.getTextureCoords();
		this.indices = cube.getIndices();
		this.normal = cube.getNormals();
	}
	
	public void setRGBA(float red, float green, float blue, float alpha){
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	public float[] getRGBA(){
		return new float[]{red, green, blue, alpha};
	}
	
	public Model clone(){
		Model m = new Model(vertices, texCoords, indices, normal);
		m.setLocation(getLocation().clone());
		m.scaleBy(scale);
		
		return m;
	}
	
	public void scaleBy(float s){
		scale*=s;
		for(int i = 0; i<vertices.length; i++){
			vertices[i] *= s;
		}
	}
	
	public void translate(Vector3f v){
		this.location.x += v.x;
		this.location.y += v.y;
		this.location.z += v.z;
	}
	
	public float[] getNormals() {
		return normal;
	}

	public void setNormals(float[] normal) {
		this.normal = normal;
	}

	public float getRed() {
		return red;
	}

	public float getGreen() {
		return green;
	}

	public float getBlue() {
		return blue;
	}

	public float getAlpha() {
		return alpha;
	}

	public Vector3f getLocation() {
		return location;
	}

	public void setLocation(Vector3f location) {
		this.location = location;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getTextureCoords() {
		return texCoords;
	}

	public int[] getIndices() {
		return indices;
	}

}
