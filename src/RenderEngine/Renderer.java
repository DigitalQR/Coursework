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
		shader.start();
		RawModel model = texturedModel.getRawModel();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertextCount(), GL11.GL_UNSIGNED_INT, 0);
		GL13.glActiveTexture(0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void render(RawModel model){
		//shader.start();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);

		float[] rgba = model.getRGBA();
		
		GL11.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertextCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		
		GL30.glBindVertexArray(0);
		//shader.stop();
	}
}
