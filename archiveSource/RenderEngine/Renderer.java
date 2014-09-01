package RenderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import RenderEngine.Model.RawModel;
import RenderEngine.Model.TexturedModel;
import RenderEngine.Shaders.StaticShader;

public class Renderer{
	
	StaticShader shader = new StaticShader();
	
	public void prepare(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.5f, 0.5f, 1f, 1f);
	}
	
	public void render(TexturedModel texturedModel){
		/*GL11.glBegin(GL11.GL_QUADS);		
		
		//This is me just testing 
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());		
		
		//GL11.glColor4f(1, 1, 1, 1);
		//GL11.glNormal3f(0.0f, 0.0f, 1.0f);

		GL11.glTexCoord2d(0, 0);
		GL11.glVertex3f(-4.0f, -0.2f, -0.3f);
		
		GL11.glTexCoord2d(0, 1);
		GL11.glVertex3f(-4.0f, -4.2f, -0.3f);
		
		GL11.glTexCoord2d(1, 1);
		GL11.glVertex3f(-1.0f, -4.2f, -0.3f);
		
		GL11.glTexCoord2d(1, 0);
		GL11.glVertex3f(-1.0f, -0.2f, -0.3f);
		
		GL11.glEnd();*/
		
		shader.start();
		RawModel model = texturedModel.getRawModel();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());
		


		

		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);

		float[] rgba = model.getRGBA();
		
		//GL11.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
		//System.out.println(rgba[0] + ", " + rgba[1] + ", " + rgba[2] + ", " + rgba[3]);
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertextCount(), GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		
		
		
		
		
		
		GL13.glActiveTexture(0);
		GL11.glBindTexture(0, 0);
		
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void render(RawModel model){
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);

		float[] rgba = model.getRGBA();
		
		//GL11.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
		//System.out.println(rgba[0] + ", " + rgba[1] + ", " + rgba[2] + ", " + rgba[3]);
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertextCount(), GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
}
