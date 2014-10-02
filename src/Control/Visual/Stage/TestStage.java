package Control.Visual.Stage;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import Control.Visual.Font;
import RenderEngine.Loader;
import RenderEngine.Renderer;
import RenderEngine.Model.Model;
import Tools.Maths.Cubef;
import Tools.Maths.Vector3f;

public class TestStage extends Stage{
	Font font;
	float track = 0;
	Texture t;
	
	float r, theta, psi;
	float rotationy = 0;
	float rotationx = 0;
	
	public void prepare(){
		font = new Font("Font/Default");
		t = Loader.loadTexture("The arrow of certainty");
		r = 1;
		theta = 0;
		psi = 0;
	}
	
	public void update(){
		FloatBuffer Ambient = BufferUtils.createFloatBuffer(16);
		Ambient.put(new float[]{0f, 0f, 0f, 1f});
        Ambient.flip();
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, Ambient);
        
		FloatBuffer Location = BufferUtils.createFloatBuffer(16);
        Location.put(new float[]{0, 0 , 3,1});
        Location.flip();
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, Location);
        
		GL11.glTranslatef(0f, 0f, -3);
		font.drawText("r: " + Math.round(r) + "\nTheta: " + Math.round(theta) + "\nPsi: " + Math.round(psi), new Vector3f(-2.5f,1.5f,0), 0.01f, 8f);
	
		GL11.glRotatef(rotationy, 0, 1, 0);
		GL11.glRotatef(rotationx, 1, 0, 0);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			theta+=0.1f;
		}if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			theta-=0.1f;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			psi+=0.1f;
		}if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			psi-=0.1f;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
			r+=0.01f;
		}if(Keyboard.isKeyDown(Keyboard.KEY_Z)){
			r-=0.01f;
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			rotationy+=0.1f;
		}if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			rotationy-=0.1f;
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			rotationx+=0.1f;
		}if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			rotationx-=0.1f;
		}

		//float x = (float) (r*Math.sin(Math.toRadians(theta))*Math.cos(Math.toRadians(psi)));
	//	float y = (float) (r*Math.sin(Math.toRadians(theta))*Math.sin(Math.toRadians(psi)));
//		float z = (float) (r*Math.cos(Math.toRadians(theta)));
		

		float x = (float) (r*Math.sin(theta)*Math.cos(psi));
		float y = (float) (r*Math.sin(theta)*Math.sin(psi));
		float z = (float) (r*Math.cos(theta));
		
		float[] vert = {0,0,0,
				0.1f,0.1f,0.1f,
								x,y,z,
				x+0.1f,y+0.1f,z+0.1f};
		float[] tex = {0,0, 0,0, 0,0, 0,0, 0,0, 0,0};
		int[] ind = {0,2,1,0,3,1};
		Model m = new Model(vert, tex, ind);
		//m.setTexture(t);
		Renderer.render(m);
		

		Cubef xc = new Cubef(new Vector3f(0,0,0), new Vector3f(0.2f,0.02f,0.02f)); 
		Cubef yc = new Cubef(new Vector3f(0,0,0), new Vector3f(0.02f,0.2f,0.02f)); 
		Cubef zc = new Cubef(new Vector3f(0,0,0), new Vector3f(0.02f,0.02f,0.2f)); 

		Model mx = new Model(xc.getVertices(), xc.getTextureCoords(), xc.getIndices());
		mx.setRGBA(1, 0, 0, 1);
		Model my = new Model(yc.getVertices(), yc.getTextureCoords(), yc.getIndices());
		my.setRGBA(0, 1, 0, 1);
		Model mz = new Model(zc.getVertices(), zc.getTextureCoords(), zc.getIndices());
		mz.setRGBA(0, 0, 1, 1);

		Renderer.render(mx);
		Renderer.render(my);
		Renderer.render(mz);
	}

}
