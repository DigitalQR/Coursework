package RenderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer{
	
	public void prepare(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.5f, 0.5f, 1f, 1f);
	}
	
	public void render(RawModel model){
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);

		float[] rgba = model.getRGBA();
		
		GL11.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertextCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		
		GL30.glBindVertexArray(0);
	}
}
