package RenderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import RenderEngine.Model.Model;
import Tools.Maths.Cubef;

public class Renderer{
	
	
	public void prepare(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.5f, 0.5f, 1f, 1f);
	}
	
	public void render(Model model){
		model.getTexture().bind();
		
		float[] vertices = model.getVertices();
		int[] indices = model.getIndices();
		float[] textureCoords = model.getTextureCoords();
		
		for(int i = 0; i<indices.length; ){
			GL11.glBegin(GL11.GL_TRIANGLES);
			
			for(int n = 0; n<3; n++){
				int Current = indices[i];
				GL11.glTexCoord2f(textureCoords[i*2], textureCoords[i*2+1]);
				GL11.glVertex3f(vertices[Current*3], vertices[Current*3+1], vertices[Current*3+2]);
				i++;
			}
			
			GL11.glEnd();
		}
		
	}
	
	public void render(Cubef cube, int textureID){
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE, textureID);
		
		float[] vertices = cube.getVertices();
		int[] indices = cube.getIndices();
		float[] textureCoords = cube.getTextureCoords();
		
		for(int i = 0; i<indices.length; ){
			GL11.glBegin(GL11.GL_TRIANGLES);
			
			for(int n = 0; n<3; n++){
				int Current = indices[i];
				GL11.glTexCoord2f(textureCoords[i*2], textureCoords[i*2+1]);
				GL11.glVertex3f(vertices[Current*3], vertices[Current*3+1], vertices[Current*3+2]);
				i++;
			}
			
			GL11.glEnd();
		}
	}
}
