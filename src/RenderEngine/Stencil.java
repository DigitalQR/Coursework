package RenderEngine;

import org.lwjgl.opengl.GL11;

public class Stencil {
	
	public static void enable(){
		GL11.glEnable(GL11.GL_STENCIL_TEST);
				
				GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 1);
				GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
				GL11.glStencilMask(0);
				GL11.glDepthMask(false);
				GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
	}
	
	public static void cylce(){
		GL11.glStencilFunc(GL11.GL_EQUAL, 0, 1);
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
		GL11.glStencilMask(0);
		GL11.glDepthMask(true);
	}
	
	public static void disable(){
		GL11.glDisable(GL11.GL_STENCIL_TEST);
	}
}
