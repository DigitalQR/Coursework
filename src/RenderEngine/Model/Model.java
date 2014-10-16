package RenderEngine.Model;

import org.newdawn.slick.opengl.Texture;

import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;

public class Model {
	
	private float[] vertices, texCoords, normal;
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
	
	public void scaleBy(float s){
		for(int i = 0; i<vertices.length; i++){
			vertices[i] *= s;
		}
	}
	
	public float[] getNormal() {
		return normal;
	}

	public void setNormal(float[] normal) {
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
/*
	public void saveData(String fileName){
		fileName = fileName.toUpperCase();
		File path = new File("Res/Model/" + fileName);
		path.mkdirs();
		System.out.println();

		try{
			//Vertex
			String vert = "";
			for(float v : vertices){
				vert += v + ";";
			}
			Formatter vertexScribe = new Formatter(path + "/vertex.rm");//raw model
			vertexScribe.format("%s", vert);
			vertexScribe.close();
			System.out.println(vertices.length + " vertices data written..");
	
			//Texture
			String tex = "";
			for(float t : texCoords){
				tex += t + ";";
			}
			Formatter texScribe = new Formatter(path + "/texture.rm");//raw model
			texScribe.format("%s", tex);
			texScribe.close();
			System.out.println(texCoords.length + " texture data written..");
	
			//Index
			String ind = "";
			for(int i : indices){
				ind += i + ";";
			}
			Formatter indexScribe = new Formatter(path + "/index.rm");//raw model
			indexScribe.format("%s", ind);
			indexScribe.close();
			System.out.println(indices.length + " indices data written..");
	
		}catch(FileNotFoundException e){
			ErrorPopup.createMessage(e, true);
		}
	}

	public static Model loadData(String fileName){
		fileName = fileName.toUpperCase();
		File path = new File("Res/Model/" + fileName);
		System.out.println();

		try{
			if(path.exists() && new File(path + "/vertex.rm").exists() &&  new File(path + "/texture.rm").exists() &&  new File(path + "/index.rm").exists()){
				//Vertex
				Scanner vertexScan = new Scanner(new File(path + "/vertex.rm"));
				String rawVertex = "";
				
				while(vertexScan.hasNext()){
					rawVertex += vertexScan.next();
				}
				vertexScan.close();
				
				String[] vertexInfo = Parametres.getParameters(rawVertex, ';');
				float[] vert = new float[vertexInfo.length];
				for(int i = 0; i<vert.length; i++){
					vert[i] = Float.valueOf(vertexInfo[i]);
				}
				System.out.println(vert.length + " vertices data found..");
				
				//Texture
				Scanner texScan = new Scanner(new File(path + "/texture.rm"));
				String rawTex = "";
				
				while(texScan.hasNext()){
					rawTex += texScan.next();
				}
				texScan.close();
				
				String[] texInfo = Parametres.getParameters(rawTex, ';');
				float[] tex = new float[texInfo.length];
				for(int i = 0; i<tex.length; i++){
					tex[i] = Float.valueOf(texInfo[i]);
				}
				System.out.println(tex.length + " texture data found..");
				
				//Index
				Scanner indexScan = new Scanner(new File(path + "/index.rm"));
				String rawIndex = "";
				
				while(indexScan.hasNext()){
					rawIndex += indexScan.next();
				}
				indexScan.close();
				
				String[] indexInfo = Parametres.getParameters(rawIndex, ';');
				int[] ind = new int[indexInfo.length];
				for(int i = 0; i<ind.length; i++){
					ind[i] = Integer.valueOf(indexInfo[i]);
				}
				System.out.println(ind.length + " indices data found..");
				
				return new Model(vert, tex, ind);
			}else{
				ErrorPopup.createMessage("Unable to import: " + fileName, true);
			}
		}catch(FileNotFoundException | NumberFormatException e){
			ErrorPopup.createMessage(e, true);
		}
		
		ErrorPopup.createMessage("Unable to import: " + fileName, true);
		return null;
		
	}
	*/
}
