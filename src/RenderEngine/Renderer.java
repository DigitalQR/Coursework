package RenderEngine;

import org.lwjgl.opengl.GL11;

import Control.Camera;
import RenderEngine.Model.Model;
import Tools.Maths.Vector3f;

public class Renderer{
	
	
	public static void prepare(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
		
		float[] RGBA = Camera.getRGBA().clone();
		for(int i = 0; i<3; i++){
			RGBA[i]-=0.2f;
			if(RGBA[i] < 0){
				RGBA[i] = 0;
			}
		}
		GL11.glClearColor(RGBA[0], RGBA[1], RGBA[2], 1f);
		
	}
	
	public static void render(Model model){
		try{
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			model.getTexture().bind();
		}catch(NullPointerException e){
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		};
		
		float[] vertices = model.getVertices();
		int[] indices = model.getIndices();
		float[] textureCoords = model.getTextureCoords();
		float[] normals = model.getNormals();
		
		Vector3f Location = model.getLocation();
		GL11.glTranslatef(Location.x, Location.y, Location.z);
		
		GL11.glColor4f(model.getRed(), model.getGreen(), model.getBlue(), model.getAlpha());
		
		for(int i = 0; i<indices.length; ){
			GL11.glBegin(GL11.GL_TRIANGLES);
			
			for(int n = 0; n<3; n++){
				int Current = indices[i];
				GL11.glNormal3f(normals[Current*3], normals[Current*3+1], normals[Current*3+2]);
				GL11.glTexCoord2f(textureCoords[Current*2], textureCoords[Current*2+1]);
				GL11.glVertex3f(vertices[Current*3], vertices[Current*3+1], vertices[Current*3+2]);
				i++;
			}
			
			GL11.glEnd();
		}

		GL11.glTranslatef(-Location.x, -Location.y, -Location.z);
	}
	
}
