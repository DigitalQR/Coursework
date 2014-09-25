package RenderEngine.Model;

import org.newdawn.slick.opengl.Texture;

import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;

public class Model {
	
	private float[] vertices, texCoords;
	private int[] indices;
	private Texture texture;
	private Vector3f Location = new Vector3f(0,0,0);
	private float red = 1, green = 1, blue = 1, alpha = 1;
	
	public void setRGBA(float red, float green, float blue, float alpha){
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	public float[] getRGBA(){
		return new float[]{red, green, blue, alpha};
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
		return Location;
	}

	public void setLocation(Vector3f location) {
		Location = location;
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

	public Model(float[] vertices, float[] textureCoords, int[] indices){
		this.vertices = vertices;
		this.texCoords = textureCoords;
		this.indices = indices;
	}

	public Model(Cubef cube){
		this.vertices = cube.getVertices();
		this.texCoords = cube.getTextureCoords();
		this.indices = cube.getIndices();
	}
}
